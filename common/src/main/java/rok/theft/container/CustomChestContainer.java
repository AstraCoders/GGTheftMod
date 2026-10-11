package rok.theft.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import rok.theft.enchantments.PocketChainEnchant;
import org.jetbrains.annotations.NotNull;
import rok.theft.Config;
import rok.theft.Constants;
import rok.theft.Events;
import rok.theft.init.Registry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class CustomChestContainer extends AbstractContainerMenu {
	private final Container container;
	private final Set<Integer> initialSlots = new HashSet<>();
	private final Set<Integer> initialCuriosSlots = new HashSet<>();
	private final int containerRows;
	private final int curiosSlots;
	private final int curiosColumns;
	private final int curiosPanelWidth;
	private final int curiosSlotStartMenuIndex;
	private final CuriosPageContainer curiosPageContainer;
	public int canStoleQnt;

	private final ItemStack glove;

	public CustomChestContainer(int pContainerId, Inventory pPlayerInventory) {
		this(Registry.CUSTOM_CHEST.get(), pContainerId, pPlayerInventory, new SimpleContainer(45), null);
	}

	public CustomChestContainer(int pContainerId, Inventory pPlayerInventory, int curiosSlots) {
		this(pContainerId, pPlayerInventory, curiosSlots, List.of());
	}

	public CustomChestContainer(int pContainerId, Inventory pPlayerInventory, int curiosSlots, List<Pair<String, ResourceLocation>> curiosTypes) {
		this(Registry.CUSTOM_CHEST.get(), pContainerId, pPlayerInventory, new SimpleContainer(45 + curiosSlots), null, curiosTypes);
	}

	public CustomChestContainer(int pContainerId, Inventory pPlayerInventory, Container pContainer, ItemStack glove) {
		this(Registry.CUSTOM_CHEST.get(), pContainerId, pPlayerInventory, pContainer, glove, List.of());
	}

	public CustomChestContainer(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, ItemStack glove) {
		this(pType, pContainerId, pPlayerInventory, pContainer, glove, List.of());
	}

	private CustomChestContainer(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, ItemStack glove, List<Pair<String, ResourceLocation>> curiosTypes) {
		super(pType, pContainerId);
		checkContainerSize(pContainer, 45);
		this.container = pContainer;
		this.containerRows = 5;
		this.curiosSlots = Math.max(0, pContainer.getContainerSize() - 45);
		this.curiosPageContainer = this.curiosSlots == 0 ? null : new CuriosPageContainer(pContainer, this.curiosSlots);
		this.curiosColumns = this.curiosPageContainer == null ? 0 : this.curiosPageContainer.getColumns();
		this.curiosPanelWidth = this.curiosColumns == 0 ? 0 : 15 + this.curiosColumns * 18;
		this.glove = glove;

		// TODO: verificar CD de dia do mine
		canStoleQnt = Config.COMMON.LIMIT_ITEM_NUMBER.get();

		pContainer.startOpen(pPlayerInventory.player);
		int i = (this.containerRows - 4) * 18;

		if (Config.COMMON.SHOW_MISSING.get()) {
			for (int j = 0; j < 45; ++j) {
				if (!container.getItem(j).isEmpty()) {
					initialSlots.add(j);
				}
			}
			for (int j = 0; j < this.curiosSlots; ++j) {
				if (!container.getItem(45 + j).isEmpty()) initialCuriosSlots.add(j);
			}
		}

		for (int j = 0; j < this.containerRows; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new CustomSlot(pContainer, k + j * 9, this.curiosPanelWidth + 8 + k * 18, 18 + j * 18));
			}
		}

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new CustomSlot(pPlayerInventory, j1 + l * 9 + 9, this.curiosPanelWidth + 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new CustomSlot(pPlayerInventory, i1, this.curiosPanelWidth + 8 + i1 * 18, 161 + i));
		}
		for (int x = 4; x < 8; x++) {
			((CustomSlot) getSlot(x)).setActive(false);
		}
		this.curiosSlotStartMenuIndex = this.slots.size();
		for (int x = 0; x < (this.curiosPageContainer == null ? 0 : this.curiosPageContainer.getContainerSize()); x++) {
			int column = x % this.curiosColumns;
			int row = x / this.curiosColumns;
			CustomSlot slot = new CustomSlot(this.curiosPageContainer, x, 8 + column * 18, 18 + row * 18, true);
			if (x < curiosTypes.size()) slot.setCuriosType(curiosTypes.get(x).getFirst(), curiosTypes.get(x).getSecond());
			slot.setActive(x < this.curiosPageContainer.getVisibleSlots());
			this.addSlot(slot);
		}
	}


	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean stillValid(Player pPlayer) {
		return this.container.stillValid(pPlayer);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public @NotNull ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot instanceof CustomSlot customSlot && customSlot.isCuriosSlot()) return ItemStack.EMPTY;
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (pIndex < this.containerRows * 9) {
				if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.containerRows * 9 + 36, true)) {
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
		return itemstack;
	}


	@Override
	public void clicked(int pSlotId, int pDragType, @NotNull ClickType pClickType, Player pPlayer) {
		if (pSlotId < 0) return;
		CustomSlot slot = (CustomSlot) getSlot(pSlotId);
		if (isMissingItem(slot)) {
			pPlayer.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1F, 2F);
			slot.remove(slot.getItem().getCount());
			return;
		} else if (isGlooveItem(slot)) {
			return;
		} else if (hasPocketChainEnchant(slot)) {
			return;
		}

		if (pClickType.equals(ClickType.PICKUP_ALL)) return;

		if (Config.COMMON.LIMIT_STEAL_ITEM.get()) {
			if (slot.hasItem() && !slot.isPlayerInventory()) {
				if (canStoleQnt <= 0) {
					if (pPlayer instanceof ServerPlayer serverPlayer) serverPlayer.closeContainer();
					return;
				}
				canStoleQnt--;
			}
		}

		super.clicked(pSlotId, pDragType, pClickType, pPlayer);
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player pPlayer) {

		// time in cooldown
		if (Config.COMMON.COOLDOWN_TO_STEAL.get() != 0 && glove != null) {
			pPlayer.getCooldowns().addCooldown(glove.getItem(), Config.COMMON.COOLDOWN_TO_STEAL.get() * 20);
		}

		// creating missing item
		if (Config.COMMON.SHOW_MISSING.get()) {
			ItemStack missing = Registry.MISSING.get().getDefaultInstance();
			CompoundTag nbt = new CompoundTag();
			nbt.putLong(Constants.STOLEN_TIME_NBT, System.currentTimeMillis());
			missing.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

			for (Integer initialSlot : initialSlots) {
				if (!getSlot(initialSlot).hasItem()) {
					getSlot(initialSlot).set(missing);
				}
			}
			for (Integer initialSlot : initialCuriosSlots) {
				if (container.getItem(45 + initialSlot).isEmpty()) container.setItem(45 + initialSlot, missing);
			}
		}
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
		Events.closeInventory(pPlayer);
	}

	public int getRowCount() {
		return this.containerRows;
	}

	public int getCuriosPanelWidth() {
		return this.curiosPanelWidth;
	}

	public int getCuriosColumns() {
		return this.curiosColumns;
	}

	public int getCuriosSlots() {
		return this.curiosSlots;
	}

	public int getCuriosRowsInColumn(int column) {
		return this.curiosPageContainer == null ? 0 : this.curiosPageContainer.getRowsInColumn(column);
	}

	public int getCuriosPageCount() {
		return this.curiosPageContainer == null ? 0 : this.curiosPageContainer.getPageCount();
	}

	public int getCuriosPage() {
		return this.curiosPageContainer == null ? 0 : this.curiosPageContainer.getPage();
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		if (this.curiosPageContainer == null || this.getCuriosPageCount() < 2) return false;
		int nextPage = switch (buttonId) {
			case 0 -> this.getCuriosPage() - 1;
			case 1 -> this.getCuriosPage() + 1;
			default -> this.getCuriosPage();
		};
		if (!this.curiosPageContainer.setPage(nextPage)) return false;
		for (int i = 0; i < this.curiosPageContainer.getContainerSize(); i++) {
			((CustomSlot) this.getSlot(this.curiosSlotStartMenuIndex + i)).setActive(i < this.curiosPageContainer.getVisibleSlots());
		}
		this.broadcastFullState();
		return true;
	}

	private boolean isGlooveItem(Slot slot) {
		return ItemStack.isSameItem(Registry.THIEF_GLOVE.get().getDefaultInstance(), slot.getItem());
	}

	private boolean isMissingItem(Slot slot) {
		return ItemStack.isSameItem(Registry.MISSING.get().getDefaultInstance(), slot.getItem());
	}

	private boolean hasPocketChainEnchant(Slot slot) {
		if (Config.COMMON.POCKET_CHAIN_ENCHANT.get()) return false;
		ItemEnchantments enchantments = slot.getItem().getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		return enchantments.keySet().stream().anyMatch(holder -> holder.is(PocketChainEnchant.KEY));
	}
}
