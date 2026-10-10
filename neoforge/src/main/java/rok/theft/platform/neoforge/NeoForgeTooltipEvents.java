package rok.theft.platform.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import rok.theft.Events;
import rok.theft.Theft;

@EventBusSubscriber(modid = Theft.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
final class NeoForgeTooltipEvents {
    @SubscribeEvent
    public static void onTooltip(RenderTooltipEvent.Pre event) {
        if (Events.cancelTooltip(event.getItemStack())) event.setCanceled(true);
    }
}
