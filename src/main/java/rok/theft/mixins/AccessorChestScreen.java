package rok.theft.mixins;

import net.minecraft.client.gui.screen.inventory.ChestScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
@Mixin(ChestScreen.class)
public interface AccessorChestScreen {
	@Accessor
	int getContainerRows();
}
