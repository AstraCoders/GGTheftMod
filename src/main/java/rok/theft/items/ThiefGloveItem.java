package rok.theft.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
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
		super(new Properties().stacksTo(1).durability(8).setNoRepair());
	}

	@Override
	public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, Player pPlayer, @NotNull LivingEntity pTarget, InteractionHand pHand) {
		if (pPlayer.level().isClientSide) return InteractionResult.FAIL;
		ServerPlayer player = (ServerPlayer) pPlayer;
		if (pTarget.getType() != EntityType.PLAYER) return InteractionResult.FAIL;
		ServerPlayer target = (ServerPlayer) pTarget;
		if (!player.isCrouching()) return InteractionResult.FAIL;

		EntityHitResult rtr = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
		if (rtr != null) return InteractionResult.FAIL;
		if (target.isCreative()) return InteractionResult.FAIL;

		// COOLDOWN
		if (player.getCooldowns().isOnCooldown(pStack.getItem())) {
			player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(), 0.3F, 1F);
			player.displayClientMessage(Component.translatable("cooldown.theft.left"), true);
			return InteractionResult.FAIL;
		}

		player.setForcedPose(Pose.CROUCHING);
		TheftAction.doTheft(target, player, pStack);

		// Adding SFX
		player.playSound(SoundEvents.LEASH_KNOT_BREAK, 0.2F, 1F);
		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level pWorld, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
		if (pWorld.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pHand));
		return super.use(pWorld, pPlayer, pHand);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, TooltipFlag pFlag) {
		pTooltip.add(Component.translatable("item.theft.glove.lore"));
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}
}
