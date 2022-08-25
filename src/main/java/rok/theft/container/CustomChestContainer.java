package rok.theft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rok.theft.init.Registry;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class CustomChestContainer extends Container {
	private final IInventory container;
	private final int containerRows;

	public CustomChestContainer(int pContainerId, PlayerInventory pPlayerInventory) {
		this(pContainerId, pPlayerInventory, new Inventory(5 * 9));
	}


	public CustomChestContainer(int pContainerId, PlayerInventory pPlayerInventory, IInventory pContainer) {
		this(Registry.CUSTOM_CHEST.get(), pContainerId, pPlayerInventory, pContainer, 5);
	}

	public CustomChestContainer(ContainerType<?> pType, int pContainerId, PlayerInventory pPlayerInventory, IInventory pContainer, int pRows) {
		super(pType, pContainerId);
		checkContainerSize(pContainer, pRows * 9);
		this.container = pContainer;
		this.containerRows = pRows;
		pContainer.startOpen(pPlayerInventory.player);
		int i = (this.containerRows - 4) * 18;

		for (int j = 0; j < this.containerRows; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new CustomSlot(pContainer, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new CustomSlot(pPlayerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new CustomSlot(pPlayerInventory, i1, 8 + i1 * 18, 161 + i));
		}
		for (int x = 4; x < 8; x++) {
			((CustomSlot) getSlot(x)).setActive(false);
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean stillValid(PlayerEntity pPlayer) {
		return this.container.stillValid(pPlayer);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack quickMoveStack(PlayerEntity pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (pIndex < this.containerRows * 9) {
				if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 9, this.containerRows * 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		if (!((CustomSlot) slot).isPlayerInventory()) {
			slot.set(Registry.MISSING.get().getDefaultInstance());
		}

		return itemstack;
	}

	@Override
	public ItemStack clicked(int pSlotId, int pDragType, ClickType pClickType, PlayerEntity pPlayer) {
		CustomSlot slot = (CustomSlot) getSlot(pSlotId);
		if (!slot.getItem().sameItemStackIgnoreDurability(Registry.MISSING.get().getDefaultInstance())) {
			slot.remove(1);
		}
		return super.clicked(pSlotId, pDragType, pClickType, pPlayer);
	}

	/**
	 * Called when the container is closed.
	 */
	public void removed(PlayerEntity pPlayer) {
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
	}

	@OnlyIn(Dist.CLIENT)
	public int getRowCount() {
		return this.containerRows;
	}
}
