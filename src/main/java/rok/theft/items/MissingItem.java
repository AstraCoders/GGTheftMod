package rok.theft.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import rok.theft.Constants;
import rok.theft.utils.TimeFrame;

import javax.annotation.Nullable;
import java.util.List;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
public class MissingItem extends Item {
	public MissingItem() {
		super(new Properties().stacksTo(1));
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
		item.setCount(0);
		return false;
	}

	// TODO: se for para fazer o tempo roubado no nome, ou na lore
	@Override
	public ITextComponent getName(ItemStack pStack) {
		return super.getName(pStack);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
		String lore = LanguageMap.getInstance().getOrDefault("item.theft.missing.lore");

		CompoundNBT stolenTime = pStack.getOrCreateTag();

		if (!stolenTime.contains(Constants.STOLEN_TIME_NBT)) {
			return;
		}

		long time = System.currentTimeMillis() - stolenTime.getLong(Constants.STOLEN_TIME_NBT);
		String formattedTime = new TimeFrame(time).getFormatedDiscursive();

		lore = lore.replace("${time}", formattedTime);

		String[] split = lore.split("\n");
		for (String s : split) {
			pTooltip.add(new StringTextComponent(s));
		}
	}
}
