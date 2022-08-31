package rok.theft.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.enchantments.PocketChainEnchant;
import rok.theft.items.MissingItem;
import rok.theft.items.ThiefGloveItem;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
public class Registry {

	public static void init() {
		EnchantmentType.create("ALL", item -> {
			return true;
		});
	}
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theft.MOD_ID);
	public static final DeferredRegister<ContainerType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Theft.MOD_ID);
	public static final DeferredRegister<Enchantment> ENCHANTMENT_REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Theft.MOD_ID);
	public static final RegistryObject<ContainerType<CustomChestContainer>> CUSTOM_CHEST = CONTAINER_REGISTRY.register("theft_container", () -> new ContainerType<>(CustomChestContainer::new));
	public static final RegistryObject<Item> THIEF_GLOVE = ITEMS.register("glove", ThiefGloveItem::new);
	public static final RegistryObject<Item> MISSING = ITEMS.register("missing", MissingItem::new);
	public static final RegistryObject<Enchantment> POCKET_CHAIN = ENCHANTMENT_REGISTRY.register("pocket_chain", PocketChainEnchant::new);
}
