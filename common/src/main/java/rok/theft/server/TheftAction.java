package rok.theft.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import rok.theft.Config;
import rok.theft.Events;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;
import rok.theft.utils.CooldownUtils;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftAction {

	public static void doTheft(ServerPlayer target, ServerPlayer player, ItemStack itemStack) {
		Events.addToWatchingMap(target, player);
		int packetTime = -1;
		if (Config.COMMON.STEAL_TIMER.get()) {
			CooldownUtils.setCooldown(player, (long) Config.COMMON.STEAL_TIME.get(), "stealing");
			packetTime = Config.COMMON.STEAL_TIME.get();
		}
		Theft.sendTheftTime(player, packetTime);
		var curiosInventory = Theft.getCuriosInventory(target);
		TargetPlayerInventory targetInventory = new TargetPlayerInventory(target, curiosInventory);
		Theft.openTheftMenu(player, new MenuProvider() {
			@Override
			public Component getDisplayName() {
				if (Config.COMMON.STEAL_TITLE.get()) {
					return Component.translatable("gui.theft.stealing").append(" ").append(target.getDisplayName());
				}
				return Component.translatable("gui.theft.stealing");
			}

			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
				return new CustomChestContainer(id, inventory, targetInventory, itemStack);
			}
		}, curiosInventory);

	}
}
