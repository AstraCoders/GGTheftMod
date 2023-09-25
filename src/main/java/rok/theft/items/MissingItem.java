package rok.theft.items;

import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        item.setCount(0);
        return false;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return super.getName(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        String lore = Language.getInstance().getOrDefault("item.theft.missing.lore");

        CompoundTag stolenTime = pStack.getOrCreateTag();

        if (!stolenTime.contains(Constants.STOLEN_TIME_NBT)) {
            return;
        }

        long time = System.currentTimeMillis() - stolenTime.getLong(Constants.STOLEN_TIME_NBT);
        String formattedTime = new TimeFrame(time).getFormatedDiscursive();

        lore = lore.replace("${time}", formattedTime);

        String[] split = lore.split("\n");
        for (String s : split) {
            pTooltip.add(Component.literal(s));
        }
    }
}
