package rok.theft.platform.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import rok.theft.Theft;
import rok.theft.client.TheftChestScreen;
import rok.theft.init.Registry;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
@EventBusSubscriber(modid = Theft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
final class NeoForgeClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(Registry.CUSTOM_CHEST.get(), TheftChestScreen::new);
    }
}
