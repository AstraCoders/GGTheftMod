package rok.theft.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import rok.theft.client.ClientStuff;

import java.util.function.Supplier;

/*
 * @author Rok, Pedro Lucas N M Machado created on 15/08/2022
 */
public class StartTheftPacket {
	int time;

	public StartTheftPacket(FriendlyByteBuf buf) {
		this.time = buf.readInt();
	}

	public StartTheftPacket(int time) {
		this.time = time;
	}

	public static void buffer(StartTheftPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.time);
	}

	public static void handle(StartTheftPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context ctx = contextSupplier.get();

		ctx.enqueueWork(() -> startAction(ctx.getSender(), packet));
		ctx.setPacketHandled(true);
	}

	private static void startAction(ServerPlayer entity, StartTheftPacket packet) {
		ClientStuff.theftTime = packet.time;
	}
}
