package rok.theft;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.server.ServerLifecycleHooks;
import rok.theft.init.Registry;
import rok.theft.packets.StartTheftPacket;
import rok.theft.utils.CooldownUtils;
import rok.theft.utils.TraceUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * @author Rok created on 11/08/2022
 */
@Mod.EventBusSubscriber(modid = Theft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
			ServerPlayer target = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(tarUUID);
			ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(plaUUID);
			if (target == null || player == null) {
				watching.remove(tarUUID);
				return;
			}
			final EntityHitResult rtr = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
			if (rtr != null) {
				player.closeContainer();
				watching.remove(tarUUID);
			}
			if (!Config.COMMON.STEAL_TIMER.get()) {
				return;
			}
			long cdTime = CooldownUtils.getCooldownTimeSec(player, "stealing");
			Theft.PACKET_HANDLER.sendTo(new StartTheftPacket((int) cdTime), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			if (!CooldownUtils.isInCooldown(player, "stealing")) {
				player.closeContainer();
				watching.remove(tarUUID);
			}

			// Remove if is distant
			if (target.distanceTo(player) > 6) {
				player.closeContainer();
				watching.remove(tarUUID);
			}

			// PlaySound
			if (tickCount % 40 == 0 && cdTime >= 3) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO.get(), 0.1F, 1F);
			} else
			if (tickCount % 20 == 0 && cdTime < 3) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO.get(), 0.1F, 1F);
			} else
			if (tickCount % 4 == 0 && cdTime < 1) {
				target.playSound(SoundEvents.NOTE_BLOCK_BANJO.get(), 0.1F, 1F);
			}
		});
	}

	public static void addToWatchingMap(ServerPlayer target, ServerPlayer player) {
		if (watching.containsKey(target.getUUID())) {
			UUID uuid = watching.get(target.getUUID());
			ServerPlayer pClose = target.getServer().getPlayerList().getPlayer(uuid);
			if (pClose == null) return;
			pClose.closeContainer();
			watching.remove(target.getUUID());
		}
		watching.put(target.getUUID(), player.getUUID());
	}

	// Preventing bugs...
	@SubscribeEvent
	public void closeInventory(PlayerContainerEvent.Close e) {
		if (!watching.containsValue(e.getEntity().getUUID())) return;
		UUID uuid = watching.entrySet().stream().filter(it -> it.getValue().equals(e.getEntity().getUUID())).map(Map.Entry::getKey).findAny().orElse(null);
		if (uuid == null) return;
		watching.remove(uuid);

		// Removing CD if it has
		CooldownUtils.removeCooldown(e.getEntity(), "stealing");

		// Getting if is damageable
		if (Config.COMMON.DAMAGE_ITEM.get()) {
			boolean hurt = e.getEntity().getMainHandItem().hurt(1, RandomSource.create(), ((ServerPlayer) e.getEntity()));
			if (hurt) {
				e.getEntity().getMainHandItem().shrink(1);
				e.getEntity().broadcastBreakEvent(e.getEntity().getUsedItemHand());
			}
		}

		// Play soundEffect
		e.getEntity().playSound(SoundEvents.LEASH_KNOT_BREAK,0.5F, 1F);

		e.getEntity().setForcedPose(null);
	}

	@SubscribeEvent
	public void onDie(LivingDeathEvent e) {
		if (e.getEntity().level().isClientSide) return;
		if (!e.getEntity().getType().equals(EntityType.PLAYER)) return;
		if (!watching.containsKey(e.getEntity().getUUID())) return;
		UUID uuid = watching.get(e.getEntity().getUUID());
		ServerPlayer player = e.getEntity().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}

	@SubscribeEvent
	public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
		if (e.getEntity().level().isClientSide) return;
		if (!watching.containsKey(e.getEntity().getUUID())) return;
		UUID uuid = watching.get(e.getEntity().getUUID());
		ServerPlayer player = e.getEntity().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}
	@SubscribeEvent
	public void onDropItem(ItemTossEvent e) {
		if (ItemStack.isSameItemSameTags(Registry.MISSING.get().getDefaultInstance(), e.getEntity().getItem())) {
			e.setCanceled(true);
		}
	}
	@SubscribeEvent
	public void onHoverEvent(RenderTooltipEvent e) {
		if (Config.COMMON.SHOW_MISSING_HOVER.get()) return;
		if (ItemStack.isSameItemSameTags(Registry.MISSING.get().getDefaultInstance(), e.getItemStack())) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onDayPassEvent(SleepFinishedTimeEvent e) {
		if (!Config.COMMON.COOLDOWN_RESET_SLEEP.get()) return;

//		long timePass = e.getNewTime()-e.getLevel().dayTime();
		for (Player player : e.getLevel().players()) {
			player.getCooldowns().removeCooldown(Registry.THIEF_GLOVE.get());
		}
	}
}
