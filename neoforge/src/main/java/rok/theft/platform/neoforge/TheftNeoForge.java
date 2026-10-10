package rok.theft.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import rok.theft.Theft;
import rok.theft.TheftPlatform;
import rok.theft.datagen.DataGenerator;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
@Mod(Theft.MOD_ID)
public final class TheftNeoForge {
    public TheftNeoForge(IEventBus modBus, ModContainer container) {
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, NeoForgeConfig.SPEC);
        NeoForgeRegistry.register(modBus);
        modBus.addListener(DataGenerator::onGatherData);
        NeoForgeNetwork.register(modBus);
        Theft.PLATFORM = new TheftPlatform() {
            @Override
            public void sendTheftTime(ServerPlayer player, int time) {
                NeoForgeNetwork.send(player, time);
            }

            @Override
            public void setForcedCrouching(ServerPlayer player, boolean forced) {
                player.setForcedPose(forced ? Pose.CROUCHING : null);
            }
        };
        NeoForge.EVENT_BUS.register(new NeoForgeEvents());
    }
}
