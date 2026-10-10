package rok.theft;

import net.minecraft.server.level.ServerPlayer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class Theft {

    public static final String MOD_ID = "theft";
    public static final String PROTOCOL_VERSION = "1";
    public static TheftPlatform PLATFORM = (player, time) -> {
        throw new IllegalStateException("Theft platform was not initialized");
    };

    private Theft() {}

    public static void sendTheftTime(ServerPlayer player, int time) {
        PLATFORM.sendTheftTime(player, time);
    }

    public static void setForcedCrouching(ServerPlayer player, boolean forced) {
        PLATFORM.setForcedCrouching(player, forced);
    }
}
