package rok.theft.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import rok.theft.Config;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 12/08/2022
 */
public class CustomSlot extends Slot {
    boolean active = true;

    public CustomSlot(Container pContainer, int pIndex, int pX, int pY) {
        super(pContainer, pIndex, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        if (isPlayerInventory()) return true;
        if (getSlotIndex() > 4) return true;
        if (!(pStack.getItem() instanceof ArmorItem)) return false;
        EquipmentSlot eqp = ((ArmorItem) pStack.getItem()).getEquipmentSlot();
        switch (getSlotIndex()) {
            case 0:
                return eqp.equals(EquipmentSlot.HEAD);
            case 1:
                return eqp.equals(EquipmentSlot.CHEST);
            case 2:
                return eqp.equals(EquipmentSlot.LEGS);
            case 3:
                return eqp.equals(EquipmentSlot.FEET);
            default:
                return false;
        }
    }

    @Override
    public boolean mayPickup(Player pPlayer) {
        if (Config.COMMON.PICKUP_BOTH.get()) return true;
        return !isPlayerInventory();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPlayerInventory() {
        return container.getContainerSize() <= 41;
    }

}
