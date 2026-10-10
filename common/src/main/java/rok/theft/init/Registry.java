package rok.theft.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import rok.theft.container.CustomChestContainer;

import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class Registry {
    public static Supplier<MenuType<CustomChestContainer>> CUSTOM_CHEST;
    public static Supplier<Item> THIEF_GLOVE;
    public static Supplier<Item> MISSING;
    public static Supplier<CreativeModeTab> GLOVE_TAB;

    private Registry() {}
}
