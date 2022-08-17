package rok.theft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import rok.theft.Config;
import rok.theft.Theft;

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
	public boolean mayPickup(PlayerEntity pPlayer) {
		if (Config.COMMON.PICKUP_BOTH.get()) return true;
		return !(container.getContainerSize() <= 41);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
