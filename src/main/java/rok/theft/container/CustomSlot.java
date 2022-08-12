package rok.theft.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 12/08/2022
 */
public class CustomSlot extends Slot {
	public CustomSlot(IInventory pContainer, int pIndex, int pX, int pY) {
		super(pContainer, pIndex, pX, pY);
	}

	boolean active = true;

	@Override
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
