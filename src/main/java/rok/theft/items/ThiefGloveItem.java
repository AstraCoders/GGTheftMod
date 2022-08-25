package rok.theft.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import rok.theft.Config;
import rok.theft.server.TheftAction;
import rok.theft.utils.TraceUtils;

import javax.annotation.Nullable;
import java.util.List;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ThiefGloveItem extends Item {
	public ThiefGloveItem() {
		super(new Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(Config.COMMON.ITEM_DURABILITY.get()));
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pTarget, Hand pHand) {
		if (pTarget.getType() != EntityType.PLAYER) return ActionResultType.FAIL;
		if (!pPlayer.isCrouching()) return ActionResultType.FAIL;

		final EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(pTarget, entity -> entity == pPlayer, 6);
		if (rtr != null) return ActionResultType.FAIL;
		if (pPlayer.level.isClientSide) return ActionResultType.FAIL;

		pPlayer.setForcedPose(Pose.CROUCHING);
		TheftAction.doTheft((ServerPlayerEntity) pTarget, (ServerPlayerEntity) pPlayer);

		// Adding SFX
		pPlayer.level.playSound(null, pPlayer, SoundEvents.LEASH_KNOT_BREAK, SoundCategory.PLAYERS, 0.2F, 1F);
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> use(World pWorld, PlayerEntity pPlayer, Hand pHand) {
		if (pWorld.isClientSide) return ActionResult.pass(pPlayer.getItemInHand(pHand));
		return super.use(pWorld, pPlayer, pHand);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
		String[] split = LanguageMap.getInstance().getOrDefault("item.theft.glove.lore").split("\n");
		for (String s : split) {
			pTooltip.add(new StringTextComponent(s));
		}
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}
}
