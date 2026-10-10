package rok.theft.platform.neoforge;

import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import rok.theft.Events;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
final class NeoForgeEvents {
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        Events.serverTick(event.getServer());
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        Events.onDeath(event.getEntity());
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onLoggedOut(event.getEntity());
    }

    @SubscribeEvent
    public void onDrop(ItemTossEvent event) {
        if (Events.cancelDrop(event.getEntity().getItem())) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onDayPass(SleepFinishedTimeEvent event) {
        Events.onDayPass(event.getLevel());
    }
}
