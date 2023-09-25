package rok.theft.server;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import rok.theft.Config;
import rok.theft.Events;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;
import rok.theft.packets.StartTheftPacket;
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
		Theft.PACKET_HANDLER.sendTo(new StartTheftPacket(packetTime), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		player.openMenu(new MenuProvider() {
			@Override
			public Component getDisplayName() {
				if (Config.COMMON.STEAL_TITLE.get()) {
					return Component.translatable("gui.theft.stealing").append(" ").append(target.getDisplayName());
				}
				return Component.translatable("gui.theft.stealing");
			}

			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
				return new CustomChestContainer(id, inventory, new TargetPlayerInventory(target), itemStack);
			}
		});

	}
}
