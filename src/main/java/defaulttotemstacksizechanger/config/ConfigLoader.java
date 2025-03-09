package defaulttotemstacksizechanger.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import defaulttotemstacksizechanger.DefaultTotemStackSizeChanger;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

import static defaulttotemstacksizechanger.util.DataResultUtils.exceptionToResult;
import static defaulttotemstacksizechanger.util.DataResultUtils.fromBool;

public interface ConfigLoader {
    Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir();

    Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setStrictness(Strictness.LENIENT)
            .create();

    //region load
    static <T> T load(String path, Codec<T> codec, T defaultConfig) {
        return DataResult.success(CONFIG_FOLDER.resolve(path).toFile())
                .flatMap(
                        file -> fromBool(file.getParentFile().exists() || file.getParentFile().mkdirs(), "Failed to make config folder")
                                .map(ignored -> file)
                )
                .flatMap(file -> exceptionToResult(() -> new FileReader(file)))
                .flatMap(reader ->
                        exceptionToResult(() -> JsonParser.parseReader(reader))
                                .ifSuccess(ignored -> exceptionToResult(reader::close).mapError(str -> "Failed to close Reader: " + str).getOrThrow())
                                .ifError(ignored -> exceptionToResult(reader::close).mapError(str -> "Failed to close Reader: " + str).getOrThrow())
                )
                .flatMap(
                        jsonElement -> codec.decode(JsonOps.INSTANCE, jsonElement)
                                .mapError(str -> "Error Decoding Config (Will Be Defaulted): " + str)
                )
                .map(Pair::getFirst)
                .ifError(err -> DefaultTotemStackSizeChanger.LOGGER.debug("Error Loading Config: {}", err.message()))
                .result()
                .orElseGet(() -> save(path, codec, defaultConfig).result().orElse(defaultConfig));
    }

    //endregion

    //region save
    static <T> DataResult<T> save(String path, Codec<T> codec, T config) {
        return DataResult.success(CONFIG_FOLDER.resolve(path).toFile())
                .flatMap(
                        file -> fromBool(file.getParentFile().exists() || file.getParentFile().mkdirs(), "Failed to make config folder")
                                .map(ignored -> file)
                )
                .flatMap(file -> exceptionToResult(() -> new FileWriter(file)))
                .flatMap(
                        writer -> codec.encodeStart(JsonOps.INSTANCE, config)
                                .flatMap(jsonElement -> exceptionToResult(() -> GSON.toJson(jsonElement)))
                                .flatMap(json -> exceptionToResult(() -> writer.write(json)))
                                .ifError(ignored -> exceptionToResult(writer::close).mapError(str -> "Failed to close Writer: " + str).getOrThrow())
                                .ifSuccess(ignored -> exceptionToResult(writer::close).mapError(str -> "Failed to close Writer: " + str).getOrThrow())
                )
                .ifError(err -> DefaultTotemStackSizeChanger.LOGGER.debug("Error Saving Config: {}", err.message()))
                .map(ignored -> config);
    }
    //endregion
}
