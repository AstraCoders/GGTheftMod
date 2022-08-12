package rok.theft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TargetPlayerInventory implements IInventory {
	public final ServerPlayerEntity player;

	public TargetPlayerInventory(ServerPlayerEntity p) {
		player = p;
	}

	@Override
	public int getContainerSize() {
		return 45;
	}

	@Override
	public boolean isEmpty() {
		return player.inventory.isEmpty();
	}

	public int getSlot(int index) {
		if (index == 8) {
			return 40;
		} else if (index >= 0 && index <= 3) {
			return 39 - index;
		} else if (index >= 9 && index <= 35) {
			return index;
		} else if (index >= 36 && index <= 44) {
			return index - 36;
		}

		return -1;
	}
	public boolean isInvalidSlot(int index) {
		return index >= 4 && index < 8;
	}

	@Override
	public ItemStack getItem(int pIndex) {
		if (isInvalidSlot(pIndex)) {
			return ItemStack.EMPTY;
		}
		int slot = getSlot(pIndex);
		return slot == -1 ? ItemStack.EMPTY : player.inventory.getItem(slot);
	}

	@Override
	public ItemStack removeItem(int pIndex, int count) {
		if (isInvalidSlot(pIndex)) {
			return ItemStack.EMPTY;
		}

		int slot = getSlot(pIndex);
		return slot == -1 ? ItemStack.EMPTY : player.inventory.removeItem(slot, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int pIndex) {
		if (isInvalidSlot(pIndex)) {
			return ItemStack.EMPTY;
		}

		int slot = getSlot(pIndex);
		return slot == -1 ? ItemStack.EMPTY : player.inventory.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int index, ItemStack pStack) {
		if (isInvalidSlot(index)) {
			return;
		}

		int slot = getSlot(index);

		if (slot != -1) {
			player.inventory.setItem(slot, pStack);
			setChanged();
		}
	}

	@Override
	public void setChanged() {
		player.inventory.setChanged();
		player.containerMenu.broadcastChanges();
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		return true;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		if (isInvalidSlot(index)) {
			return false;
		}

		int slot = getSlot(index);
		return slot != -1 && player.inventory.canPlaceItem(slot, stack);
	}

	@Override
	public void clearContent() {
		player.inventory.clearContent();
	}
}