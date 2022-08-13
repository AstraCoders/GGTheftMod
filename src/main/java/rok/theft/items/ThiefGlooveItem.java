package rok.theft.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import rok.theft.server.TheftAction;
import rok.theft.utils.TraceUtils;

import java.util.function.Consumer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ThiefGlooveItem extends Item {
	public ThiefGlooveItem() {
		super(new Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(16));
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pTarget, Hand pHand) {
		if (pPlayer.level.isClientSide) return ActionResultType.FAIL;
		if (pTarget.getType() != EntityType.PLAYER) return ActionResultType.FAIL;
		if (!pPlayer.isCrouching()) return ActionResultType.FAIL;

		final EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(pTarget, entity -> entity == pPlayer, 6);
		if (rtr != null) return ActionResultType.FAIL;
		pPlayer.setPose(Pose.CROUCHING);
		TheftAction.doTheft((ServerPlayerEntity) pTarget,(ServerPlayerEntity) pPlayer);
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> use(World pWorld, PlayerEntity pPlayer, Hand pHand) {
		if (pWorld.isClientSide) return ActionResult.pass(pPlayer.getItemInHand(pHand));
		return super.use(pWorld, pPlayer, pHand);
	}
}
