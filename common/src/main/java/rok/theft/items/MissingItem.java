package rok.theft.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import rok.theft.Constants;
import rok.theft.utils.TimeFrame;

import java.util.List;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
public class MissingItem extends Item {
	public MissingItem() {
		super(new Properties().stacksTo(1));
	}


	@Override
	public Component getName(ItemStack pStack) {
		return super.getName(pStack);
	}

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltip, TooltipFlag pFlag) {
		var stolenTime = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

		if (!stolenTime.contains(Constants.STOLEN_TIME_NBT)) {
			return;
		}

		long time = System.currentTimeMillis() - stolenTime.getLong(Constants.STOLEN_TIME_NBT);
		String formattedTime = new TimeFrame(time).getFormatedDiscursive();
		pTooltip.add(Component.translatable("item.theft.missing.lore.1", formattedTime));
		pTooltip.add(Component.translatable("item.theft.missing.lore.2"));
	}
}
