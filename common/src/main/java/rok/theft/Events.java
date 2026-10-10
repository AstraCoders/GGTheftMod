package rok.theft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import rok.theft.init.Registry;
import rok.theft.utils.CooldownUtils;
import rok.theft.utils.TraceUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class Events {

    private static final ConcurrentHashMap<UUID, UUID> watching = new ConcurrentHashMap<>();
    private static int tickCount;

    private Events() {}

    public static void serverTick(MinecraftServer server) {
        tickCount++;
        if (tickCount % 4 == 0) {
            handleTickEvent(server, tickCount);
        }
        if (tickCount == 40) tickCount = 0;
    }

    private static void handleTickEvent(MinecraftServer server, int tickCount) {
        if (watching.isEmpty()) return;
        watching.forEach((targetId, playerId) -> {
            ServerPlayer target = server.getPlayerList().getPlayer(targetId);
            ServerPlayer player = server.getPlayerList().getPlayer(playerId);
            if (target == null || player == null) {
                watching.remove(targetId);
                return;
            }
            EntityHitResult result = TraceUtils.rayTraceEntity(target, entity -> entity == player, 6);
            if (result != null) {
                player.closeContainer();
                watching.remove(targetId);
            }
            if (!Config.COMMON.STEAL_TIMER.get()) return;

            long cooldown = CooldownUtils.getCooldownTimeSec(player, "stealing");
            Theft.sendTheftTime(player, (int) cooldown);
            if (!CooldownUtils.isInCooldown(player, "stealing")) {
                player.closeContainer();
                watching.remove(targetId);
            }
            if (target.distanceTo(player) > 6) {
                player.closeContainer();
                watching.remove(targetId);
            }

            if (tickCount % 40 == 0 && cooldown >= 3) {
                target.playSound(SoundEvents.NOTE_BLOCK_BANJO.value(), 0.1F, 1F);
            } else if (tickCount % 20 == 0 && cooldown < 3) {
                target.playSound(SoundEvents.NOTE_BLOCK_BANJO.value(), 0.1F, 1F);
            } else if (tickCount % 4 == 0 && cooldown < 1) {
                target.playSound(SoundEvents.NOTE_BLOCK_BANJO.value(), 0.1F, 1F);
            }
        });
    }

    public static void addToWatchingMap(ServerPlayer target, ServerPlayer player) {
        if (watching.containsKey(target.getUUID())) {
            UUID uuid = watching.get(target.getUUID());
            ServerPlayer previousPlayer = target.getServer().getPlayerList().getPlayer(uuid);
            if (previousPlayer == null) return;
            previousPlayer.closeContainer();
            watching.remove(target.getUUID());
        }
        watching.put(target.getUUID(), player.getUUID());
    }

    public static void closeInventory(Player player) {
        if (player.level().isClientSide || !watching.containsValue(player.getUUID())) return;
        UUID targetId = watching.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player.getUUID()))
                .map(Map.Entry::getKey).findAny().orElse(null);
        if (targetId == null) return;
        watching.remove(targetId);

        ServerPlayer serverPlayer = (ServerPlayer) player;
        CooldownUtils.removeCooldown(serverPlayer, "stealing");
        if (Config.COMMON.DAMAGE_ITEM.get()) {
            serverPlayer.getMainHandItem().hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer,
                    item -> serverPlayer.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
        }
        serverPlayer.playSound(SoundEvents.LEASH_KNOT_BREAK, 0.5F, 1F);
        Theft.setForcedCrouching(serverPlayer, false);
    }

    public static void onDeath(LivingEntity entity) {
        if (entity.level().isClientSide || entity.getType() != EntityType.PLAYER || !watching.containsKey(entity.getUUID())) return;
        UUID playerId = watching.get(entity.getUUID());
        ServerPlayer player = entity.getServer().getPlayerList().getPlayer(playerId);
        if (player != null) player.closeContainer();
    }

    public static void onLoggedOut(Player player) {
        if (player.level().isClientSide || !watching.containsKey(player.getUUID())) return;
        UUID playerId = watching.get(player.getUUID());
        ServerPlayer thief = player.getServer().getPlayerList().getPlayer(playerId);
        if (thief != null) thief.closeContainer();
    }

    public static boolean cancelDrop(ItemStack stack) {
        return ItemStack.isSameItem(Registry.MISSING.get().getDefaultInstance(), stack);
    }

    public static boolean cancelTooltip(ItemStack stack) {
        return !Config.COMMON.SHOW_MISSING_HOVER.get()
                && ItemStack.isSameItem(Registry.MISSING.get().getDefaultInstance(), stack);
    }

    public static void onDayPass(LevelAccessor level) {
        if (!Config.COMMON.COOLDOWN_RESET_SLEEP.get()) return;
        for (Player player : level.players()) {
            player.getCooldowns().removeCooldown(Registry.THIEF_GLOVE.get());
        }
    }
}
