package rok.theft.container;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 12/08/2022
 */
public class CustomSlot extends Slot {
	public CustomSlot(IInventory pContainer, int pIndex, int pX, int pY) {
		super(pContainer, pIndex, pX, pY);
	}

	boolean active = true;

	@Override
	public boolean mayPlace(ItemStack pStack) {
			return pStack.getItem() instanceof ArmorItem || getSlotIndex() > 4;
	}

	@Override
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
