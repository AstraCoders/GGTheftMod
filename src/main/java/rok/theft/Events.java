package rok.theft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

/*
 * @author Rok created on 11/08/2022
 */
public class Events {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteractSpecific e) {
		if (e.getPlayer().level.isClientSide) return;
		if (e.getTarget().getType() == EntityType.PLAYER) {
			ServerPlayerEntity target = (ServerPlayerEntity) e.getTarget();
			ServerPlayerEntity player = (ServerPlayerEntity) e.getPlayer();
			player.openMenu(new INamedContainerProvider() {
				@Override
				public ITextComponent getDisplayName() {
					return target.getDisplayName();
				}

				@Nullable
				@Override
				public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
					return new ChestContainer(ContainerType.GENERIC_9x5, id, inventory, new PlayerInvChest(target), 5);
				}
			});
		}
	}
}
