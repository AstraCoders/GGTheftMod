package rok.theft.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rok.theft.Config;
import rok.theft.server.TheftAction;
import rok.theft.utils.TimeFrame;
import rok.theft.utils.TraceUtils;

import javax.annotation.Nullable;
import java.awt.event.ContainerEvent;
import java.sql.Time;
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

		CompoundNBT stolenTime = pStack.getTag();
		if (stolenTime == null) return;

		long time = System.currentTimeMillis() - stolenTime.getLong("stolenTime");
		TimeFrame timeFrame = new TimeFrame(time);
		timeFrame.getFormatedDiscursive();
		lore = lore.replace("${time}", timeFrame.getFormatedDiscursive());

		String[] split = lore.split("\n");
		for (String s : split) {
			pTooltip.add(new StringTextComponent(s));
		}

		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}
}
