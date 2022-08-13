package rok.theft.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rok.theft.Theft;
import rok.theft.items.ThiefGlooveItem;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
public class Registry {
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Theft.MOD_ID);
	public static RegistryObject<Item> THIEF_GLOOVE = ITEMS.register("gloove", ThiefGlooveItem::new);

}
