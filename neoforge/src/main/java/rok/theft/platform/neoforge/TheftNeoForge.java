package rok.theft.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Pose;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import rok.theft.Config;
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
            public Container getCuriosInventory(ServerPlayer player) {
                return Config.COMMON.STEAL_CURIOS_ITEMS.get() && ModList.get().isLoaded("curios")
                        ? CuriosTheftInventory.create(player)
                        : null;
            }

            @Override
            public void openTheftMenu(ServerPlayer player, MenuProvider provider, Container curiosInventory) {
                if (ModList.get().isLoaded("curios") && curiosInventory instanceof CuriosTheftInventory curios) {
                    player.openMenu(provider, buffer -> {
                        buffer.writeVarInt(curios.getContainerSize());
                        for (CuriosTheftInventory.CurioSlotType type : curios.getSlotTypes()) {
                            buffer.writeUtf(type.identifier());
                            buffer.writeResourceLocation(type.icon());
                        }
                    });
                } else {
                    player.openMenu(provider, buffer -> buffer.writeVarInt(0));
                }
            }

            @Override
            public void setForcedCrouching(ServerPlayer player, boolean forced) {
                player.setForcedPose(forced ? Pose.CROUCHING : null);
            }
        };
        NeoForge.EVENT_BUS.register(new NeoForgeEvents());
    }
}
