package rok.theft.platform.fabric;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import rok.theft.Config;
import rok.theft.init.Registry;

import java.util.Set;

final class FabricLoot {
    private static final Set<String> TABLES = Set.of(
            "minecraft:chests/shipwreck_map",
            "minecraft:chests/shipwreck_supply",
            "minecraft:chests/shipwreck_treasure",
            "minecraft:chests/pillager_outpost",
            "minecraft:chests/abandoned_mineshaft");

    private FabricLoot() {}

    static void register() {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (!TABLES.contains(key.location().toString()) || !Config.COMMON.GENERATE_LOOT.get()) return;
            float chance = 1.0F - Config.COMMON.GENERATE_LOOT_CHANCE.get() / 100.0F;
            table.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Registry.THIEF_GLOVE.get())
                            .when(LootItemRandomChanceCondition.randomChance(chance))));
        });
    }
}
