package defaulttotemstacksizechanger.handler;

import defaulttotemstacksizechanger.DefaultTotemStackSizeChanger;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class TotemStackSizeHandler implements DefaultItemComponentEvents.ModifyCallback, Consumer<ComponentMap.Builder> {
    @Override
    public void modify(DefaultItemComponentEvents.ModifyContext context) {
        context.modify(Items.TOTEM_OF_UNDYING, this);
    }

    @Override
    public void accept(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.MAX_STACK_SIZE, DefaultTotemStackSizeChanger.CONFIG.maxStackSize());
    }
}
