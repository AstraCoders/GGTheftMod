package rok.theft.container;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TargetPlayerInventory implements Container {
    public final ServerPlayer player;
    private final Container curios;

    public TargetPlayerInventory(ServerPlayer p, Container curios) {
        player = p;
        this.curios = curios;
    }

    @Override
    public int getContainerSize() {
        return 45 + (curios == null ? 0 : curios.getContainerSize());
    }

    @Override
    public boolean isEmpty() {
        return player.getInventory().isEmpty() && (curios == null || curios.isEmpty());
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
        if (pIndex >= 45) {
            int curiosIndex = pIndex - 45;
            return curios != null && curiosIndex < curios.getContainerSize() ? curios.getItem(curiosIndex) : ItemStack.EMPTY;
        }
        int slot = getSlot(pIndex);
        return slot == -1 ? ItemStack.EMPTY : player.getInventory().getItem(slot);
    }

    @Override
    public ItemStack removeItem(int pIndex, int count) {
        if (isInvalidSlot(pIndex)) {
            return ItemStack.EMPTY;
        }
        if (pIndex >= 45) {
            int curiosIndex = pIndex - 45;
            return curios != null && curiosIndex < curios.getContainerSize() ? curios.removeItem(curiosIndex, count) : ItemStack.EMPTY;
        }

        int slot = getSlot(pIndex);
        return slot == -1 ? ItemStack.EMPTY : player.getInventory().removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        if (isInvalidSlot(pIndex)) {
            return ItemStack.EMPTY;
        }
        if (pIndex >= 45) {
            int curiosIndex = pIndex - 45;
            return curios != null && curiosIndex < curios.getContainerSize() ? curios.removeItemNoUpdate(curiosIndex) : ItemStack.EMPTY;
        }

        int slot = getSlot(pIndex);
        return slot == -1 ? ItemStack.EMPTY : player.getInventory().removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int index, ItemStack pStack) {
        if (isInvalidSlot(index)) {
            return;
        }
        if (index >= 45) {
            int curiosIndex = index - 45;
            if (curios != null && curiosIndex < curios.getContainerSize()) {
                curios.setItem(curiosIndex, pStack);
                setChanged();
            }
            return;
        }

        int slot = getSlot(index);

        if (slot != -1) {
            player.getInventory().setItem(slot, pStack);
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        player.getInventory().setChanged();
        if (curios != null) curios.setChanged();
        player.containerMenu.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (isInvalidSlot(index)) {
            return false;
        }
        if (index >= 45) {
            int curiosIndex = index - 45;
            return curios != null && curiosIndex < curios.getContainerSize() && curios.canPlaceItem(curiosIndex, stack);
        }

        int slot = getSlot(index);
        return slot != -1 && player.getInventory().canPlaceItem(slot, stack);
    }

    @Override
    public void clearContent() {
        player.getInventory().clearContent();
        if (curios != null) curios.clearContent();
    }
}
