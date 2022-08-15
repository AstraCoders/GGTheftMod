package rok.theft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rok.theft.client.ClientStuff;
import rok.theft.init.Registry;

@Mod(Theft.MOD_ID)
public class Theft {
	public static final String MOD_ID = "theft";

	public Theft() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Events());

		Registry.ITEMS.register(bus);
		Registry.CONTAINER_REGISTRY.register(bus);
		bus.addListener(this::onClientRegister);
	}
	public void onClientRegister(FMLClientSetupEvent e) {
		ClientStuff.init(e);
	}
}
