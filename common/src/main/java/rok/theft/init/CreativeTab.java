package rok.theft.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class CreativeTab {
    public static final List<Supplier<? extends ItemLike>> ITEMS = new ArrayList<>();

    public static <T extends Item> Supplier<T> addToTab(Supplier<T> item) {
        ITEMS.add(item);
        return item;
    }

    private CreativeTab() {}
}
