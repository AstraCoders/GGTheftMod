package rok.theft.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rok.theft.init.Registry;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
@Mixin(Container.class)
public abstract class MixinContainer {
	@Shadow public abstract Slot getSlot(int pSlotId);

	@Inject(method = "quickMoveStack", at = @At("HEAD"))
	public void quickMoveStack(PlayerEntity pPlayer, int pIndex, CallbackInfoReturnable<ItemStack> cir) {
		System.out.println("Foi até aqui 1222222222222222222222222");
	}

	@Inject(method = "doClick", at = @At("HEAD"))
	public void doClick(int pSlotId, int pDragType, ClickType pClickType, PlayerEntity pPlayer, CallbackInfoReturnable<ItemStack> cir) {
		System.out.println("Foi até aqui 1");
		Slot slot = getSlot(pSlotId);
		System.out.println("Foi até aqui 2");
		if (slot.getItem().sameItemStackIgnoreDurability(Registry.MISSING.get().getDefaultInstance())) {
			System.out.println("Foi até aqui 3");
			slot.remove(1);
		}
	}
}
