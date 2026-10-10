package rok.theft.platform.fabric;

import net.fabricmc.api.ModInitializer;
import rok.theft.Theft;

public final class TheftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfig.register();
        FabricRegistry.register();
        FabricNetwork.register();
        Theft.PLATFORM = FabricNetwork::send;
        FabricEvents.register();
    }
}
