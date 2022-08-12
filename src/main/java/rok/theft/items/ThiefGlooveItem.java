package rok.theft.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import rok.theft.server.TheftAction;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ThiefGlooveItem extends Item {
	public ThiefGlooveItem() {
		super(new Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1));
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pTarget, Hand pHand) {
		ActionResultType actionResultType = super.interactLivingEntity(pStack, pPlayer, pTarget, pHand);
		if (pPlayer.level.isClientSide) return actionResultType;
		if (pTarget.getType() != EntityType.PLAYER) return actionResultType;

		TheftAction.doTheft((ServerPlayerEntity) pPlayer,(ServerPlayerEntity) pTarget);
		return actionResultType;
	}

	@Override
	public ActionResult<ItemStack> use(World pWorld, PlayerEntity pPlayer, Hand pHand) {
		if (pWorld.isClientSide) return ActionResult.pass(pPlayer.getItemInHand(pHand));
		return super.use(pWorld, pPlayer, pHand);
	}


}
