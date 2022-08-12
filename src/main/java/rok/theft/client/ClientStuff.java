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
@Mod.EventBusSubscriber(modid = Theft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientStuff {

	@SubscribeEvent
	public void onSetup(FMLClientSetupEvent e) {
		ScreenManager.register(Theft.CUSTOM_CHEST.get(), TheftChestScreen::new);
	}
}
