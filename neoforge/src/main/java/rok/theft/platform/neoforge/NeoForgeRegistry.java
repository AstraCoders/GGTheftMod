package rok.theft.platform.neoforge;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.IEventBus;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.init.CreativeTab;
import rok.theft.init.Registry;
import rok.theft.items.MissingItem;
import rok.theft.items.ThiefGloveItem;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class NeoForgeRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Theft.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Theft.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Theft.MOD_ID);
    static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Theft.MOD_ID);

    private NeoForgeRegistry() {}

    public static void register(IEventBus bus) {
        Registry.CUSTOM_CHEST = MENUS.register("theft_container", () -> new MenuType<>(CustomChestContainer::new, FeatureFlagSet.of()));
        Registry.THIEF_GLOVE = CreativeTab.addToTab(ITEMS.register("glove", ThiefGloveItem::new));
        Registry.MISSING = ITEMS.register("missing", MissingItem::new);
        Registry.GLOVE_TAB = TABS.register("theft_tab", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("tab.theft.name"))
                .icon(() -> Registry.THIEF_GLOVE.get().getDefaultInstance())
                .displayItems((params, output) -> CreativeTab.ITEMS.forEach(item -> output.accept(item.get())))
                .build());
        LOOT_MODIFIERS.register("theft_loot", () -> NeoForgeTheftLootModifier.CODEC);

        ITEMS.register(bus);
        MENUS.register(bus);
        TABS.register(bus);
        LOOT_MODIFIERS.register(bus);
    }
}
