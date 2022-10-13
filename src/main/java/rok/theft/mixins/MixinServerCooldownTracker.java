package rok.theft.mixins;

import net.minecraft.item.Item;
import net.minecraft.util.ServerCooldownTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 12/10/2022
 */
@Mixin(ServerCooldownTracker.class)
public interface MixinServerCooldownTracker {

	@Invoker("onCooldownEnded")
	void onCooldownEnded(Item pItem);
}
