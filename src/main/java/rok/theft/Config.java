package rok.theft;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
public class Config {

	public static class Common {
		public final ForgeConfigSpec.BooleanValue DAMAGE_ITEM;
		public final ForgeConfigSpec.IntValue ITEM_DURABILITY;
		public final ForgeConfigSpec.BooleanValue STEAL_TIMER;
		public final ForgeConfigSpec.IntValue STEAL_TIME;
		public final ForgeConfigSpec.BooleanValue STEAL_TITLE;
		public final ForgeConfigSpec.BooleanValue PICKUP_BOTH;
		public final ForgeConfigSpec.BooleanValue SHOW_MISSING;
		public final ForgeConfigSpec.BooleanValue SHOW_MISSING_HOVER;
		public final ForgeConfigSpec.BooleanValue POCKET_CHAIN_ENCHANT;
		public final ForgeConfigSpec.BooleanValue LIMIT_STEAL_ITEM;
		public final ForgeConfigSpec.IntValue LIMIT_ITEM_NUMBER;
		public final ForgeConfigSpec.IntValue COOLDOWN_TO_STEAL;
		public final ForgeConfigSpec.BooleanValue COOLDOWN_RESET_SLEEP;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common config").push("misc");
			SHOW_MISSING = builder.comment("The player must know which slot was stolen?").define("show_missing", true);
			SHOW_MISSING_HOVER = builder.comment("Should Stolen Item has hover info?").define("show_missing_hover", true);
			POCKET_CHAIN_ENCHANT = builder.comment("Can player steal items with \"Pocket Chain\" enchantment?").define("pocket_chain_enchant", false);
			builder.pop();
			
			builder.push("glove");
			DAMAGE_ITEM = builder.comment("Should damage item on every use").define("damage_item", true);
			ITEM_DURABILITY = builder.comment("Item durability").defineInRange("item_durability", 16, 0, 255);
			COOLDOWN_TO_STEAL = builder.comment("How long will the player have to wait to steal again? ( In seconds / 0 to deactivate )").defineInRange("cooldown_to_steal",10, 0,86400);
			COOLDOWN_RESET_SLEEP = builder.comment("Should reset steal cooldown when the player sleeps?").define("cooldown_reset_sleep", true);
			builder.pop();
			
			builder.push("inventory");
			LIMIT_STEAL_ITEM = builder.comment("Limit how much a player can stole").define("limit_steal_item", true);
			LIMIT_ITEM_NUMBER = builder.comment("Quantity of item that can be stolen.").defineInRange("limit_item_number",3, 0,999);
			PICKUP_BOTH = builder.comment("Should the player be able to pickup and place item on target's inventory? (Default: just pickup)").define("pickup_both", false);
			STEAL_TIMER = builder.comment("Should player have time to steal another player?").define("steal_timer_enabled", true);
			STEAL_TIME = builder.comment("Time to steal player").defineInRange("steal_time", 10, 0, 999);
			STEAL_TITLE = builder.comment("Stealing inventory should have a title? (Example: \"Stealing Steve\")").define("steal_title_enabled", true);
			builder.pop();
			
		}
	}
	static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonPair.getRight();
		COMMON = commonPair.getLeft();
	}

}
