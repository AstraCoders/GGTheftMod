package rok.theft.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rok.theft.Theft;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ClientStuff {

	public static void init(FMLClientSetupEvent e) {
		e.enqueueWork(() -> {
			ScreenManager.register(Theft.CUSTOM_CHEST.get(), TheftChestScreen::new);
		});
	}
}
