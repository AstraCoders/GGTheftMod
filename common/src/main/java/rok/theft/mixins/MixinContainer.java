package rok.theft.mixins;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rok.theft.init.Registry;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
@Mixin(AbstractContainerMenu.class)
public abstract class MixinContainer {
	@Inject(method = "doClick", at = @At("HEAD"))
	public void doClick(int pSlotId, int pDragType, ClickType pClickType, Player pPlayer, CallbackInfo ci) {
		if (pSlotId < 0) return;

		Slot slot;
		try {
			slot = pPlayer.containerMenu.getSlot(pSlotId);
		} catch (IndexOutOfBoundsException e) {
			return;
		}
		if (slot == null) return;

		if (!slot.hasItem()) return;
		if (ItemStack.isSameItem(Registry.MISSING.get().getDefaultInstance(), slot.getItem())) {
			pPlayer.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1F, 2F);
			slot.remove(slot.getItem().getCount());
		}
	}
}
