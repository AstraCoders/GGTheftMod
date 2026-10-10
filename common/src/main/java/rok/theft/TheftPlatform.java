package rok.theft;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public interface TheftPlatform {
    void sendTheftTime(ServerPlayer player, int time);

    default void setForcedCrouching(ServerPlayer player, boolean forced) {
        // ponytail: Fabric only mirrors the current sneak pose; use a pose mixin if input-independent forcing is needed.
        player.setPose(forced || player.isShiftKeyDown() ? Pose.CROUCHING : Pose.STANDING);
    }
}
