package defaulttotemstacksizechanger.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Config(int maxStackSize) {
    public static final Config DEFAULT = new Config(1);
    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.ap(
                    Config::new,
                    Codec.intRange(1, 99).fieldOf("maxTotemStackSize").forGetter(Config::maxStackSize)
            )
    );

    public Config {
        if (maxStackSize < 1 || maxStackSize > 99) {
            throw new IllegalArgumentException("Max stack size must be between 1 and 99");
        }
    }
}
