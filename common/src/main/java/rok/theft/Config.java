package rok.theft;

import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class Config {

    public static final Common COMMON = new Common();

    public static final class Common {
        public Supplier<Boolean> DAMAGE_ITEM = () -> true;
        public Supplier<Integer> ITEM_DURABILITY = () -> 16;
        public Supplier<Boolean> STEAL_TIMER = () -> true;
        public Supplier<Integer> STEAL_TIME = () -> 16;
        public Supplier<Boolean> STEAL_TITLE = () -> true;
        public Supplier<Boolean> PICKUP_BOTH = () -> false;
        public Supplier<Boolean> SHOW_MISSING = () -> true;
        public Supplier<Boolean> SHOW_MISSING_HOVER = () -> true;
        public Supplier<Boolean> POCKET_CHAIN_ENCHANT = () -> false;
        public Supplier<Boolean> LIMIT_STEAL_ITEM = () -> true;
        public Supplier<Integer> LIMIT_ITEM_NUMBER = () -> 5;
        public Supplier<Integer> COOLDOWN_TO_STEAL = () -> 10;
        public Supplier<Boolean> COOLDOWN_RESET_SLEEP = () -> false;
        public Supplier<Boolean> GENERATE_LOOT = () -> true;
        public Supplier<Integer> GENERATE_LOOT_CHANCE = () -> 50;
    }

    private Config() {}
}
