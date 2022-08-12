package rok.theft.mixins;

import net.minecraft.inventory.container.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rok.theft.mixins.hacks.IStatus;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
@Mixin(Slot.class)
public class MixinSlot implements IStatus {
	boolean active = true;

	@Override
	public boolean getStatus() {
		return active;
	}

	@Override
	public void setStatus(boolean status) {
		active = status;
	}

	@Inject(method = "isActive", at = @At("HEAD"),cancellable = true)
	public void onActive(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(active);
	}
}
