package rok.theft.platform.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import rok.theft.Theft;
import rok.theft.client.ClientStuff;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
final class NeoForgeNetwork {
    private NeoForgeNetwork() {}

    static void register(IEventBus modBus) {
        modBus.addListener(NeoForgeNetwork::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar(Theft.PROTOCOL_VERSION).playToClient(
                StartTheftPayload.TYPE,
                StartTheftPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientStuff.theftTime = payload.time())
        );
    }

    static void send(ServerPlayer player, int time) {
        PacketDistributor.sendToPlayer(player, new StartTheftPayload(time));
    }
}
