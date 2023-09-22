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
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import rok.theft.Config;
import rok.theft.init.CreativeTab;
import rok.theft.init.Registry;
import rok.theft.server.TheftAction;
import rok.theft.utils.TraceUtils;

import javax.annotation.Nullable;
import java.util.List;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ThiefGloveItem extends Item {
	public ThiefGloveItem() {
		super(new Properties().tab(CreativeTab.GLOVE_TAB).stacksTo(1).durability(Config.COMMON.ITEM_DURABILITY.get()).setNoRepair());
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pTarget, Hand pHand) {
		if (pPlayer.level.isClientSide) return ActionResultType.FAIL;
		ServerPlayerEntity player = (ServerPlayerEntity) pPlayer;
		if (pTarget.getType() != EntityType.PLAYER) return ActionResultType.FAIL;
		ServerPlayerEntity target = (ServerPlayerEntity) pTarget;
		if (!player.isCrouching()) return ActionResultType.FAIL;

		EntityRayTraceResult rtr = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
		if (rtr != null) return ActionResultType.FAIL;
		if (target.isCreative()) return ActionResultType.FAIL;

		// COOLDOWN
		if (player.getCooldowns().isOnCooldown(pStack.getItem())) {
			STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.ACTIONBAR, new TranslationTextComponent("cooldown.theft.left"), 15, 50, 15);
			player.connection.send(titlePacket);
			player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO, 0.3F, 1F);
			return ActionResultType.FAIL;
		}

		player.setForcedPose(Pose.CROUCHING);
		TheftAction.doTheft(target, player, pStack);

		// Adding SFX
		player.playSound(SoundEvents.LEASH_KNOT_BREAK, 0.2F, 1F);
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
