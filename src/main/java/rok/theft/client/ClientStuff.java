package rok.theft.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rok.theft.init.Registry;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ClientStuff {

	public static void init(FMLClientSetupEvent e) {
		e.enqueueWork(() -> ScreenManager.register(Registry.CUSTOM_CHEST.get(), TheftChestScreen::new));
	}
}
