package rok.theft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rok.theft.client.ClientStuff;
import rok.theft.init.Registry;
import rok.theft.packets.StartTheftPacket;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Mod(Theft.MOD_ID)
public class Theft {

    public static final String MOD_ID = "theft";
    public static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "connection"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public Theft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Events());

        Registry.ITEMS.register(bus);
        Registry.CONTAINER_REGISTRY.register(bus);
        Registry.GLM.register(bus);
        Registry.TABS.register(bus);

        Registry.init();
        Registry.ENCHANTMENT_REGISTRY.register(bus);

        bus.addListener(this::onClientRegister);
        addNetworkMessage(StartTheftPacket.class, StartTheftPacket::buffer, StartTheftPacket::new, StartTheftPacket::handle);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }



    public void onClientRegister(FMLClientSetupEvent e) {
        ClientStuff.init(e);
    }
}
