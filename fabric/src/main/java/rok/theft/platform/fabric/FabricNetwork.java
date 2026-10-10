package rok.theft.platform.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import rok.theft.client.ClientStuff;

final class FabricNetwork {
    private FabricNetwork() {}

    static void register() {
        PayloadTypeRegistry.playS2C().register(StartTheftPayload.TYPE, StartTheftPayload.STREAM_CODEC);
    }

    static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                StartTheftPayload.TYPE, (payload, context) -> context.client().execute(
                        () -> ClientStuff.theftTime = payload.time()));
    }

    static void send(ServerPlayer player, int time) {
        ServerPlayNetworking.send(player, new StartTheftPayload(time));
    }
}
