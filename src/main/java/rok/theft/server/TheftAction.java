package rok.theft.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import rok.theft.Events;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;

import javax.annotation.Nullable;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftAction {

	public static void doTheft(ServerPlayerEntity player, ServerPlayerEntity target) {
		player.openMenu(new INamedContainerProvider() {
			@Override
			public ITextComponent getDisplayName() {
				return target.getDisplayName();
			}

			@Nullable
			@Override
			public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
				return new CustomChestContainer(id, inventory, new TargetPlayerInventory(target));
			}
		});
		Events.addToWatchingMap(player, target);
	}
}
