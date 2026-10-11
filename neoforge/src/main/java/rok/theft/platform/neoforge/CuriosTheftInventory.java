package rok.theft.platform.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import rok.theft.init.Registry;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 11/10/2026
 */
final class CuriosTheftInventory implements Container {
    private final List<SlotGroup> groups = new ArrayList<>();
    private final List<CurioSlotType> slotTypes = new ArrayList<>();
    private final int size;

    private CuriosTheftInventory(ICuriosItemHandler curios) {
        int size = 0;
        for (var entry : curios.getCurios().entrySet()) {
            String identifier = entry.getKey();
            var stacks = entry.getValue();
            if (!stacks.isVisible()) continue;
            IDynamicStackHandler handler = stacks.getStacks();
            int slots = handler.getSlots();
            if (slots > 0) {
                groups.add(new SlotGroup(handler, size, slots));
                CurioSlotType type = new CurioSlotType(identifier, CuriosApi.getSlot(identifier, false)
                        .map(ISlotType::getIcon)
                        .orElse(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "slot/empty_curio_slot")));
                for (int i = 0; i < slots; i++) slotTypes.add(type);
                size += slots;
            }
        }
        this.size = size;
    }

    static Container create(ServerPlayer player) {
        return CuriosApi.getCuriosInventory(player)
                .map(CuriosTheftInventory::new)
                .filter(inventory -> inventory.getContainerSize() > 0)
                .orElse(null);
    }

    List<CurioSlotType> getSlotTypes() {
        return slotTypes;
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size; i++) {
            if (!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        SlotRef slot = getSlot(index);
        return slot == null ? ItemStack.EMPTY : slot.handler.getStackInSlot(slot.index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        SlotRef slot = getSlot(index);
        return slot == null ? ItemStack.EMPTY : extract(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        SlotRef slot = getSlot(index);
        return slot == null ? ItemStack.EMPTY : extract(slot, Integer.MAX_VALUE);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        SlotRef slot = getSlot(index);
        if (slot != null) slot.handler.setStackInSlot(slot.index, stack);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        SlotRef slot = getSlot(index);
        return slot != null && slot.handler.isItemValid(slot.index, stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < size; i++) {
            removeItem(i, Integer.MAX_VALUE);
        }
    }

    private SlotRef getSlot(int index) {
        if (index < 0 || index >= size) return null;
        for (SlotGroup group : groups) {
            if (index < group.start + group.size) {
                int slot = index - group.start;
                return slot < group.handler.getSlots() ? new SlotRef(group.handler, slot) : null;
            }
        }
        return null;
    }

    private ItemStack extract(SlotRef slot, int count) {
        ItemStack stack = slot.handler.getStackInSlot(slot.index);
        if (stack.is(Registry.MISSING.get())) {
            slot.handler.setStackInSlot(slot.index, ItemStack.EMPTY);
            return stack;
        }
        return slot.handler.extractItem(slot.index, count, false);
    }

    private record SlotGroup(IDynamicStackHandler handler, int start, int size) {}
    private record SlotRef(IDynamicStackHandler handler, int index) {}
    record CurioSlotType(String identifier, ResourceLocation icon) {}
}
