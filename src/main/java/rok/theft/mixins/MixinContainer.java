package rok.theft.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rok.theft.init.Registry;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
@Mixin(Container.class)
public abstract class MixinContainer {
	@Inject(method = "doClick", at = @At("HEAD"))
	public void doClick(int pSlotId, int pDragType, ClickType pClickType, PlayerEntity pPlayer, CallbackInfoReturnable<ItemStack> cir) {
		if (pSlotId < 0) return;
		Slot slot;
		try {
			slot = pPlayer.containerMenu.getSlot(pSlotId);
		} catch (IndexOutOfBoundsException e) {
			return;
		}
		if (slot == null) return;
		if (!slot.hasItem()) return;
		if (slot.getItem().sameItemStackIgnoreDurability(Registry.MISSING.get().getDefaultInstance())) {
			pPlayer.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1F, 2F);
			slot.remove(slot.getItem().getCount());
		}
	}
}
