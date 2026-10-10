package rok.theft.platform.fabric;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import rok.theft.Config;
import rok.theft.Theft;

final class FabricConfig {
    private static final Values VALUES;
    private static final ForgeConfigSpec SPEC;

    static {
        var pair = new ForgeConfigSpec.Builder().configure(Values::new);
        VALUES = pair.getLeft();
        SPEC = pair.getRight();
        Config.COMMON.DAMAGE_ITEM = VALUES.DAMAGE_ITEM::get;
        Config.COMMON.ITEM_DURABILITY = VALUES.ITEM_DURABILITY::get;
        Config.COMMON.STEAL_TIMER = VALUES.STEAL_TIMER::get;
        Config.COMMON.STEAL_TIME = VALUES.STEAL_TIME::get;
        Config.COMMON.STEAL_TITLE = VALUES.STEAL_TITLE::get;
        Config.COMMON.PICKUP_BOTH = VALUES.PICKUP_BOTH::get;
        Config.COMMON.SHOW_MISSING = VALUES.SHOW_MISSING::get;
        Config.COMMON.SHOW_MISSING_HOVER = VALUES.SHOW_MISSING_HOVER::get;
        Config.COMMON.POCKET_CHAIN_ENCHANT = VALUES.POCKET_CHAIN_ENCHANT::get;
        Config.COMMON.LIMIT_STEAL_ITEM = VALUES.LIMIT_STEAL_ITEM::get;
        Config.COMMON.LIMIT_ITEM_NUMBER = VALUES.LIMIT_ITEM_NUMBER::get;
        Config.COMMON.COOLDOWN_TO_STEAL = VALUES.COOLDOWN_TO_STEAL::get;
        Config.COMMON.COOLDOWN_RESET_SLEEP = VALUES.COOLDOWN_RESET_SLEEP::get;
        Config.COMMON.GENERATE_LOOT = VALUES.GENERATE_LOOT::get;
        Config.COMMON.GENERATE_LOOT_CHANCE = VALUES.GENERATE_LOOT_CHANCE::get;
    }

    private FabricConfig() {}

    static void register() {
        ForgeConfigRegistry.INSTANCE.register(Theft.MOD_ID, ModConfig.Type.COMMON, SPEC);
    }

    private static final class Values {
        final ForgeConfigSpec.BooleanValue DAMAGE_ITEM;
        final ForgeConfigSpec.IntValue ITEM_DURABILITY;
        final ForgeConfigSpec.BooleanValue STEAL_TIMER;
        final ForgeConfigSpec.IntValue STEAL_TIME;
        final ForgeConfigSpec.BooleanValue STEAL_TITLE;
        final ForgeConfigSpec.BooleanValue PICKUP_BOTH;
        final ForgeConfigSpec.BooleanValue SHOW_MISSING;
        final ForgeConfigSpec.BooleanValue SHOW_MISSING_HOVER;
        final ForgeConfigSpec.BooleanValue POCKET_CHAIN_ENCHANT;
        final ForgeConfigSpec.BooleanValue LIMIT_STEAL_ITEM;
        final ForgeConfigSpec.IntValue LIMIT_ITEM_NUMBER;
        final ForgeConfigSpec.IntValue COOLDOWN_TO_STEAL;
        final ForgeConfigSpec.BooleanValue COOLDOWN_RESET_SLEEP;
        final ForgeConfigSpec.BooleanValue GENERATE_LOOT;
        final ForgeConfigSpec.IntValue GENERATE_LOOT_CHANCE;

        Values(ForgeConfigSpec.Builder builder) {
            builder.comment("Common config").push("misc");
            SHOW_MISSING = builder.comment("The player must know which slot was stolen?").define("show_missing", true);
            SHOW_MISSING_HOVER = builder.comment("Should Stolen Item has hover info?").define("show_missing_hover", true);
            POCKET_CHAIN_ENCHANT = builder.comment("Can player steal items with \"Pocket Chain\" enchantment?").define("pocket_chain_enchant", false);
            builder.pop();

            builder.push("glove");
            DAMAGE_ITEM = builder.comment("Should damage item on every use").define("damage_item", true);
            ITEM_DURABILITY = builder.comment("Item durability (currently fixed at 8 on the glove)").defineInRange("item_durability", 16, 0, 255);
            COOLDOWN_TO_STEAL = builder.comment("How long will the player have to wait to steal again? ( In seconds / 0 to deactivate )").defineInRange("cooldown_to_steal", 10, 0, 86400);
            COOLDOWN_RESET_SLEEP = builder.comment("Should reset steal cooldown when the player sleeps?").define("cooldown_reset_sleep", false);
            builder.pop();

            builder.push("inventory");
            LIMIT_STEAL_ITEM = builder.comment("Limit how much a player can stole").define("limit_steal_item", true);
            LIMIT_ITEM_NUMBER = builder.comment("Quantity of item that can be stolen.").defineInRange("limit_item_number", 5, 0, 999);
            PICKUP_BOTH = builder.comment("Should the player be able to pickup and place item on target's inventory? (Default: just pickup)").define("pickup_both", false);
            STEAL_TIMER = builder.comment("Should player have time to steal another player?").define("steal_timer_enabled", true);
            STEAL_TIME = builder.comment("Time to steal player").defineInRange("steal_time", 16, 0, 999);
            STEAL_TITLE = builder.comment("Stealing inventory should have a title? (Example: \"Stealing Steve\")").define("steal_title_enabled", true);
            builder.pop();

            builder.push("loot");
            GENERATE_LOOT = builder.comment("Should generate loot at shipwreck, pillager outpost and mineshaft").define("generate_loot", true);
            GENERATE_LOOT_CHANCE = builder.comment("Chance to generate loot").defineInRange("generate_loot_chance", 50, 0, 100);
            builder.pop();
        }
    }
}
