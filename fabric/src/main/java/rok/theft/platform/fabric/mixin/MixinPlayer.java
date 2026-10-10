package rok.theft.platform.fabric.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rok.theft.Events;

@Mixin(Player.class)
abstract class MixinPlayer {
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void theft$preventMissingDrop(ItemStack stack, boolean throwRandomly, boolean retainOwnership,
                                          CallbackInfoReturnable<ItemEntity> callback) {
        if (Events.cancelDrop(stack)) callback.setReturnValue(null);
    }
}
