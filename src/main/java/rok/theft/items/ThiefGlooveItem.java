package rok.theft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolItem;
import net.minecraftforge.fml.RegistryObject;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class ThiefGlooveItem extends ToolItem {
	public ThiefGlooveItem() {
		super(new Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1).durability(1));
	}
}
