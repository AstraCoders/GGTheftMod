package rok.theft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import rok.theft.Config;

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
		if (isPlayerInventory()) return true;
		if (getSlotIndex() > 4) return true;
		if (!(pStack.getItem() instanceof ArmorItem)) return false;
		EquipmentSlotType eqp = ((ArmorItem) pStack.getItem()).getSlot();
		switch (getSlotIndex()) {
			case 0:
				return eqp.equals(EquipmentSlotType.HEAD);
			case 1:
				return eqp.equals(EquipmentSlotType.CHEST);
			case 2:
				return eqp.equals(EquipmentSlotType.LEGS);
			case 3:
				return eqp.equals(EquipmentSlotType.FEET);
			default:
				return false;
		}
	}

	@Override
	public boolean mayPickup(PlayerEntity pPlayer) {
		if (Config.COMMON.PICKUP_BOTH.get()) return true;
		return !isPlayerInventory();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public boolean isPlayerInventory() {
		return container.getContainerSize() <= 41;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
