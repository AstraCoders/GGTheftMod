package rok.theft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;
import rok.theft.items.ThiefGlooveItem;
import rok.theft.server.TheftAction;

import javax.annotation.Nullable;

/*
 * @author Rok created on 11/08/2022
 */
public class Events {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteractSpecific e) {
		if (e.getPlayer().level.isClientSide) return;
		if (e.getTarget().getType() != EntityType.PLAYER) return;

		TheftAction.doTheft((ServerPlayerEntity) e.getPlayer(),(ServerPlayerEntity) e.getTarget());
	}

	public static void addToWatchingMap(ServerPlayerEntity player, ServerPlayerEntity target) {
		watching.put(player.getUUID(), target.getUUID());
	}
	// Preventing bugs...
	@SubscribeEvent
	public void closeInventory(PlayerContainerEvent.Close e) {
		if (e.getPlayer().level.isClientSide) return;
		if (watching.containsValue(e.getPlayer().getUUID())) {
			UUID uuid = watching.entrySet().stream().filter(it -> it.getValue().equals(e.getPlayer().getUUID())).map(Map.Entry::getKey).findAny().orElse(null);
			watching.remove(uuid);
		}
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
		if (!watching.containsKey(e.getPlayer().getUUID())) return;
		UUID uuid = watching.get(e.getPlayer().getUUID());
		ServerPlayerEntity player = e.getPlayer().getServer().getPlayerList().getPlayer(uuid);
		if (player == null) return;
		player.closeContainer();

	}
}
