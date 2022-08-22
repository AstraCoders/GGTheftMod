package rok.theft.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.items.ThiefGloveItem;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
public class Registry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theft.MOD_ID);

	public static final DeferredRegister<ContainerType<?>> CONTAINER_REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Theft.MOD_ID);

	public static final RegistryObject<ContainerType<CustomChestContainer>> CUSTOM_CHEST = CONTAINER_REGISTRY.register("theft_container", () -> new ContainerType<>(CustomChestContainer::new));
	public static final RegistryObject<Item> THIEF_GLOVE = ITEMS.register("glove", ThiefGloveItem::new);

}
