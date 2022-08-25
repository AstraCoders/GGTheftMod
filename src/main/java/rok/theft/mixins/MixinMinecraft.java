package rok.theft.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/08/2022
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Inject(method = "run", at = @At("TAIL"), cancellable = true)
	public void onTest(CallbackInfo ci) {
		System.out.println("Hello world - CDEBUG");
	}
}
