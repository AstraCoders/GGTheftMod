package rok.theft.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import rok.theft.Config;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 12/08/2022
 */
public class CustomSlot extends Slot {
    boolean active = true;
    private final boolean curiosSlot;
    private String curiosIdentifier;
    private ResourceLocation curiosIcon;

    public CustomSlot(Container pContainer, int pIndex, int pX, int pY) {
        this(pContainer, pIndex, pX, pY, false);
    }

    public CustomSlot(Container pContainer, int pIndex, int pX, int pY, boolean curiosSlot) {
        super(pContainer, pIndex, pX, pY);
        this.curiosSlot = curiosSlot;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        if (curiosSlot) return false;
        if (isPlayerInventory()) return true;
        if (getContainerSlot() > 4) return true;
        if (!(pStack.getItem() instanceof ArmorItem)) return false;
        EquipmentSlot eqp = ((ArmorItem) pStack.getItem()).getEquipmentSlot();
        return switch (getContainerSlot()) {
            case 0 -> eqp.equals(EquipmentSlot.HEAD);
            case 1 -> eqp.equals(EquipmentSlot.CHEST);
            case 2 -> eqp.equals(EquipmentSlot.LEGS);
            case 3 -> eqp.equals(EquipmentSlot.FEET);
            default -> false;
        };
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
        return !curiosSlot && container.getContainerSize() <= 41;
    }

    public boolean isCuriosSlot() {
        return curiosSlot;
    }

    public void setCuriosType(String identifier, ResourceLocation icon) {
        this.curiosIdentifier = identifier;
        this.curiosIcon = icon;
    }

    public ResourceLocation getCuriosIcon() {
        return curiosIcon;
    }

    public Component getCuriosTooltip() {
        if (curiosIdentifier == null || curiosIdentifier.isEmpty()) return null;
        String defaultName = Character.toUpperCase(curiosIdentifier.charAt(0)) + curiosIdentifier.substring(1).toLowerCase();
        return Component.translatableWithFallback("curios.identifier." + curiosIdentifier, defaultName);
    }

}
