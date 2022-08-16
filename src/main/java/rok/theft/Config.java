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

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common config").push("common");
			DAMAGE_ITEM = builder.comment("Should damage item on every use").define("damage_item", true);
			ITEM_DURABILITY = builder.comment("Item durability").defineInRange("item_durability", 16, 0, 255);
			STEAL_TIMER = builder.comment("Player should have time to steal another player?").define("steal_timer_enabled", true);
			STEAL_TIME = builder.comment("Time to steal player").defineInRange("steal_time", 10, 0, 120);
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
