package defaulttotemstacksizechanger;

import defaulttotemstacksizechanger.config.Config;
import defaulttotemstacksizechanger.config.ConfigLoader;
import defaulttotemstacksizechanger.handler.TotemStackSizeHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTotemStackSizeChanger implements ModInitializer {
    public static final String MOD_ID = "defaulttotemstacksizechanger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Config CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = ConfigLoader.load("default_totem_stack_size_changer.json", Config.CODEC, Config.DEFAULT);
        DefaultItemComponentEvents.MODIFY.register(new TotemStackSizeHandler());
    }
}