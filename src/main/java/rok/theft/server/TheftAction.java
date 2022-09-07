package rok.theft.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import rok.theft.Events;
import rok.theft.Config;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.TargetPlayerInventory;
import rok.theft.packets.StartTheftPacket;
import rok.theft.utils.CooldownUtils;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftAction {

	public static void doTheft(ServerPlayerEntity target, ServerPlayerEntity player, ItemStack itemStack) {
		Events.addToWatchingMap(target, player);
		int packetTime = -1;
		if (Config.COMMON.STEAL_TIMER.get()) {
			CooldownUtils.setCooldown(player, (long) Config.COMMON.STEAL_TIME.get(), "stealing");
			packetTime = Config.COMMON.STEAL_TIME.get();
		}
		Theft.PACKET_HANDLER.sendTo(new StartTheftPacket(packetTime), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
		player.openMenu(new INamedContainerProvider() {
			@Override
			public ITextComponent getDisplayName() {
				if (Config.COMMON.STEAL_TITLE.get())
					return new StringTextComponent(LanguageMap.getInstance().getOrDefault("gui.theft.stealing") + " " + target.getDisplayName().getString());
				return new StringTextComponent(LanguageMap.getInstance().getOrDefault("gui.theft.stealing"));
			}

			@Override
			public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
				return new CustomChestContainer(id, inventory, new TargetPlayerInventory(target), itemStack);
			}
		});

	}
}
