package rok.theft.utils;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.UUID;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/11/2021
 */
public class CooldownUtils {
	private static class CooldownInstance {

		private final UUID uuid;
		private final Long time;
		private final String key;

		private CooldownInstance(UUID uuid, Long time, String key) {
			this.uuid = uuid;
			this.time = time;
			this.key = key;
		}

		public Long getTime() {
			return time;
		}

		public UUID getUuid() {
			return uuid;
		}

		public String getKey() {
			return key;
		}
	}

	private static final HashSet<CooldownInstance> cooldownInstanceSet = new HashSet<>();

	public static CooldownInstance getCooldown(PlayerEntity player, String key) {
		return cooldownInstanceSet.stream()
				       .filter(cooldownInstance -> player.getUUID().equals(cooldownInstance.getUuid()) && cooldownInstance.getKey().equals(key))
				       .findFirst()
				       .orElse(null);
	}

	public static void removeCooldown(PlayerEntity player, String key) {
		cooldownInstanceSet.removeIf(cooldownInstance -> player.getUUID().equals(cooldownInstance.getUuid()) && cooldownInstance.getKey().equals(key));
	}

	public static boolean isInCooldown(PlayerEntity player, String key) {
		CooldownInstance cooldownInstance;
		if ((cooldownInstance = getCooldown(player, key)) != null) {
			if (cooldownInstance.getTime() > System.currentTimeMillis()) {
				return true;
			} else {
				removeCooldown(player, key);
				return false;
			}
		}
		return false;
	}

	public static void setCooldown(PlayerEntity player, Long time, String key) {
		cooldownInstanceSet.add(new CooldownInstance(player.getUUID(), System.currentTimeMillis() + (time * 1000), key));
	}

	public static long getCooldownTime(PlayerEntity player, String key) {
		CooldownInstance cooldownInstance;
		if ((cooldownInstance = getCooldown(player, key)) != null) {
			long time = cooldownInstance.getTime() - System.currentTimeMillis();
			if (time < 0) {
				removeCooldown(player, key);
				return 0;
			}
			return time;
		}
		return 0;
	}

	public static long getCooldownTimeSec(PlayerEntity player, String key) {
		return getCooldownTime(player, key) / 1000;
	}
}
