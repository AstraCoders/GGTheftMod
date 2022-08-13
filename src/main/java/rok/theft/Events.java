package rok.theft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;
import rok.theft.items.ThiefGlooveItem;
import rok.theft.server.TheftAction;
import rok.theft.utils.TraceUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * @author Rok created on 11/08/2022
 */
public class Events {
	// TARGET | WATCHING
	private static final ConcurrentHashMap<UUID, UUID> watching = new ConcurrentHashMap<>();

	/*@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteractSpecific e) {
		if (e.getPlayer().level.isClientSide) return;
		if (e.getTarget().getType() != EntityType.PLAYER) return;

		final EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(e.getTarget(), entity -> entity == e.getPlayer(), 6);
		if (rtr != null) return;
		TheftAction.doTheft((ServerPlayerEntity) e.getTarget(), (ServerPlayerEntity) e.getPlayer());
	}*/

	@SubscribeEvent
	public void serverTickEvent(TickEvent.ServerTickEvent e) {
		if (watching.isEmpty()) return;
		watching.forEach((tarUUID, plaUUID) -> {
			ServerPlayerEntity target = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(tarUUID);
			ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(plaUUID);
			if (target == null || player == null) {
				watching.remove(tarUUID);
				return;
			}
			player.setPose(Pose.CROUCHING);
			final EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
			if (rtr != null) {
				player.closeContainer();
				watching.remove(tarUUID);
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
		if (watching.containsValue(e.getPlayer().getUUID())) {
			UUID uuid = watching.entrySet().stream().filter(it -> it.getValue().equals(e.getPlayer().getUUID())).map(Map.Entry::getKey).findAny().orElse(null);
			watching.remove(uuid);
			e.getPlayer().getMainHandItem().setDamageValue(e.getPlayer().getMainHandItem().getDamageValue()+1);
		}
	}

	@SubscribeEvent
	public void onDie(LivingDeathEvent e) {
		if (!e.getEntity().getType().equals(EntityType.PLAYER)) return;
		if (!watching.containsKey(e.getEntity().getUUID())) return;
		UUID uuid = watching.get(e.getEntity().getUUID());
		ServerPlayerEntity player = e.getEntity().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}

	@SubscribeEvent
	public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
		if (!watching.containsKey(e.getPlayer().getUUID())) return;
		UUID uuid = watching.get(e.getPlayer().getUUID());
		ServerPlayerEntity player = e.getPlayer().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();
	}
}
