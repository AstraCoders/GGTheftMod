package rok.theft.platform.fabric;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import rok.theft.Events;

final class FabricEvents {
    private FabricEvents() {}

    static void register() {
        ServerTickEvents.END_SERVER_TICK.register(Events::serverTick);
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> Events.onDeath(entity));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> Events.onLoggedOut(handler.player));
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {
            Events.onDayPass(player.level());
            return true;
        });
        FabricLoot.register();
    }
}
