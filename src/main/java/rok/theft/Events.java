package rok.theft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import rok.theft.packets.StartTheftPacket;
import rok.theft.utils.CooldownUtils;
import rok.theft.utils.TraceUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/*
 * @author Rok created on 11/08/2022
 */
public class Events {
	// TARGET | WATCHING
	private static final ConcurrentHashMap<UUID, UUID> watching = new ConcurrentHashMap<>();

	private int tickCount;

	@SubscribeEvent
	public void serverTickEvent(TickEvent.ServerTickEvent e) {
		tickCount++;
		if (tickCount % 4 == 0) {
			handleTickEvent(tickCount);
		}
		if (tickCount == 40) tickCount = 0;
	}

	private void handleTickEvent(int tickCount) {
		if (watching.isEmpty()) return;
		watching.forEach((tarUUID, plaUUID) -> {
			ServerPlayerEntity target = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(tarUUID);
			ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(plaUUID);
			if (target == null || player == null) {
				watching.remove(tarUUID);
				return;
			}
			final EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
			if (rtr != null) {
				player.closeContainer();
				watching.remove(tarUUID);
			}
			if (!Config.COMMON.STEAL_TIMER.get()) {
				return;
			}
			long cdTime = CooldownUtils.getCooldownTimeSec(player, "stealing");
			Theft.PACKET_HANDLER.sendTo(new StartTheftPacket((int) cdTime), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
			if (!CooldownUtils.isInCooldown(player, "stealing")) {
				player.closeContainer();
				watching.remove(tarUUID);
			}

			// PlaySound
			if (tickCount % 40 == 0 && cdTime >= 3) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO, 0.1F, 1F);
			} else
			if (tickCount % 20 == 0 && cdTime < 3) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO, 0.1F, 1F);
			} else
			if (tickCount % 4 == 0 && cdTime < 1) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO, 0.1F, 1F);
			}
		});
	}

	public static void addToWatchingMap(ServerPlayerEntity target, ServerPlayerEntity player) {
		if (watching.containsKey(target.getUUID())) {
			UUID uuid = watching.get(target.getUUID());
			ServerPlayerEntity pClose = target.getEntity().getServer().getPlayerList().getPlayer(uuid);
			if (pClose == null) return;
			pClose.closeContainer();
			watching.remove(target.getUUID());
		}
		watching.put(target.getUUID(), player.getUUID());
	}

	// Preventing bugs...
	@SubscribeEvent
	public void closeInventory(PlayerContainerEvent.Close e) {
		if (!watching.containsValue(e.getPlayer().getUUID())) return;
		UUID uuid = watching.entrySet().stream().filter(it -> it.getValue().equals(e.getPlayer().getUUID())).map(Map.Entry::getKey).findAny().orElse(null);
		if (uuid == null) return;
		watching.remove(uuid);

		// Removing CD if it has
		CooldownUtils.removeCooldown(e.getPlayer(), "stealing");

		// Play soundEffect
		e.getPlayer().level.playSound(null, e.getPlayer(), SoundEvents.LEASH_KNOT_BREAK, SoundCategory.PLAYERS, 0.5F, 1F);

		e.getPlayer().setForcedPose(null);
	}

	@SubscribeEvent
	public void onDie(LivingDeathEvent e) {
		if (e.getEntity().level.isClientSide) return;
		if (!e.getEntity().getType().equals(EntityType.PLAYER)) return;
		if (!watching.containsKey(e.getEntity().getUUID())) return;
		UUID uuid = watching.get(e.getEntity().getUUID());
		ServerPlayerEntity player = e.getEntity().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}

	@SubscribeEvent
	public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
		if (e.getEntity().level.isClientSide) return;
		if (!watching.containsKey(e.getPlayer().getUUID())) return;
		UUID uuid = watching.get(e.getPlayer().getUUID());
		ServerPlayerEntity player = e.getPlayer().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}
}
