package rok.theft.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.enchantments.PocketChainEnchant;
import rok.theft.items.MissingItem;
import rok.theft.items.ThiefGloveItem;
import rok.theft.loot.TheftLootModifier;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
public class Registry {

	public static void init() {
		EnchantmentCategory.create("ALL", item -> true);
	}
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theft.MOD_ID);
	public static final DeferredRegister<MenuType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Theft.MOD_ID);
	public static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Theft.MOD_ID);
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Theft.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Theft.MOD_ID);


	public static final RegistryObject<MenuType<CustomChestContainer>> CUSTOM_CHEST = CONTAINER_REGISTRY.register("theft_container", () -> new MenuType<>(CustomChestContainer::new, FeatureFlagSet.of()));
	public static final RegistryObject<Item> THIEF_GLOVE = CreativeTab.addToTab(ITEMS.register("glove", ThiefGloveItem::new));
	public static final RegistryObject<Item> MISSING = ITEMS.register("missing", MissingItem::new);
	public static final RegistryObject<Enchantment> POCKET_CHAIN = ENCHANTMENT_REGISTRY.register("pocket_chain", PocketChainEnchant::new);
	public static final RegistryObject<Codec<? extends IGlobalLootModifier>> GLOVE_LOOT = GLM.register("theft_loot", TheftLootModifier.CODEC);


	public static final RegistryObject<CreativeModeTab> GLOVE_TAB = TABS.register("theft_tab", () -> CreativeModeTab.builder()
			.title(Component.translatable("tab.theft.name"))
			.icon(Registry.THIEF_GLOVE.get()::getDefaultInstance)
			.displayItems((displayParams, output) ->
					CreativeTab.ITEMS.forEach(itemLike -> output.accept(itemLike.get()))
			).build());


}
