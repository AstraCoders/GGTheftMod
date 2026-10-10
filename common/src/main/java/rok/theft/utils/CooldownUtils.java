package rok.theft.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.UUID;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/11/2021
 */
public class CooldownUtils {
    public record CooldownInstance(UUID uuid, Long time, String key) {

    }

    private static final HashSet<CooldownInstance> cooldownInstanceSet = new HashSet<>();

    public static CooldownInstance getCooldown(ServerPlayer player, String key) {
        return cooldownInstanceSet.stream()
                .filter(cooldownInstance -> player.getUUID().equals(cooldownInstance.uuid()) && cooldownInstance.key().equals(key))
                .findFirst()
                .orElse(null);
    }

    public static void removeCooldown(Player player, String key) {
        cooldownInstanceSet.removeIf(cooldownInstance -> player.getUUID().equals(cooldownInstance.uuid()) && cooldownInstance.key().equals(key));
    }

    public static boolean isInCooldown(ServerPlayer player, String key) {
        CooldownInstance cooldownInstance;
        if ((cooldownInstance = getCooldown(player, key)) != null) {
            if (cooldownInstance.time() > System.currentTimeMillis()) {
                return true;
            } else {
                removeCooldown(player, key);
                return false;
            }
        }
        return false;
    }

    public static void setCooldown(ServerPlayer player, Long time, String key) {
        cooldownInstanceSet.add(new CooldownInstance(player.getUUID(), System.currentTimeMillis() + (time * 1000), key));
    }

    public static long getCooldownTime(ServerPlayer player, String key) {
        CooldownInstance cooldownInstance;
        if ((cooldownInstance = getCooldown(player, key)) != null) {
            long time = cooldownInstance.time() - System.currentTimeMillis();
            if (time < 0) {
                removeCooldown(player, key);
                return 0;
            }
            return time;
        }
        return 0;
    }

    public static long getCooldownTimeSec(ServerPlayer player, String key) {
        return getCooldownTime(player, key) / 1000;
    }
}
