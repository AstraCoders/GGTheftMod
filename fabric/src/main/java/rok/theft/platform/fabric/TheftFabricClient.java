package rok.theft.platform.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import rok.theft.Events;
import rok.theft.client.TheftChestScreen;
import rok.theft.init.Registry;

public final class TheftFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(Registry.CUSTOM_CHEST.get(), TheftChestScreen::new);
        FabricNetwork.registerClient();
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            if (Events.cancelTooltip(stack)) lines.clear();
        });
    }
}
