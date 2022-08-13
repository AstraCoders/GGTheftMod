package rok.theft;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rok.theft.client.ClientStuff;
import rok.theft.config.ConfigHelper;
import rok.theft.config.JsonConfigManager;
import rok.theft.config.instances.MainConfig;
import rok.theft.container.CustomChestContainer;
import rok.theft.init.Registry;
import rok.theft.items.ThiefGlooveItem;

@Mod(Theft.MOD_ID)
public class Theft {
	public static final String MOD_ID = "theft";

	private static JsonConfigManager<MainConfig> configManager;
	private static MainConfig mainConfig;

	private static final DeferredRegister<ContainerType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
	public static RegistryObject<ContainerType<CustomChestContainer>> CUSTOM_CHEST = CONTAINER_REGISTRY.register("theft_container", () -> new ContainerType<>(CustomChestContainer::new));

	public Theft() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		// TODO: carmello vê pq n ta funfando por favo, não entendi muito bem como inicia seu sistema de config
		configManager = ConfigHelper.getConfig("theft", MainConfig::new, MainConfig.class);
		mainConfig = configManager.get();

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Events());

		CONTAINER_REGISTRY.register(bus);
		Registry.ITEMS.register(bus);
		bus.addListener(this::onClientRegister);
	}
	public void onClientRegister(FMLClientSetupEvent e) {
		ClientStuff.init(e);
	}

	// Config thing...
	@SubscribeEvent
	public void onServerEvent(FMLServerStartedEvent e) {
		configManager = ConfigHelper.getConfig("theft", MainConfig::new, MainConfig.class);
		mainConfig = configManager.get();
	}
	public static void reloadConfig() {
		configManager = ConfigHelper.getConfig("theft", MainConfig::new, MainConfig.class);
		mainConfig = configManager.get();
	}
	public static MainConfig getMainConfig() {
		return mainConfig;
	}
}
