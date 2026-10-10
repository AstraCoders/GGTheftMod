package rok.theft.platform.fabric;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.init.CreativeTab;
import rok.theft.init.Registry;
import rok.theft.items.MissingItem;
import rok.theft.items.ThiefGloveItem;

public final class FabricRegistry {
    private FabricRegistry() {}

    public static void register() {
        MenuType<CustomChestContainer> menu = new MenuType<>(CustomChestContainer::new, FeatureFlagSet.of());
        net.minecraft.core.Registry.register(BuiltInRegistries.MENU, id("theft_container"), menu);
        Registry.CUSTOM_CHEST = () -> menu;

        Item glove = registerItem("glove", new ThiefGloveItem());
        Registry.THIEF_GLOVE = CreativeTab.addToTab(() -> glove);
        Item missing = registerItem("missing", new MissingItem());
        Registry.MISSING = () -> missing;

        CreativeModeTab tab = net.minecraft.core.Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                id("theft_tab"), CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .title(Component.translatable("tab.theft.name"))
                        .icon(() -> Registry.THIEF_GLOVE.get().getDefaultInstance())
                        .displayItems((parameters, output) -> CreativeTab.ITEMS.forEach(item -> output.accept(item.get())))
                        .build());
        Registry.GLOVE_TAB = () -> tab;
    }

    private static Item registerItem(String path, Item item) {
        return net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, id(path), item);
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Theft.MOD_ID, path);
    }
}
