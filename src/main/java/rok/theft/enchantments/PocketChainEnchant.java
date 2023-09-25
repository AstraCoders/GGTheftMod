package rok.theft.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

/*
 * @author Rok, Pedro Lucas N M Machado created on 31/08/2022
 */
public class PocketChainEnchant extends Enchantment {
    public PocketChainEnchant(EquipmentSlot... pApplicableSlots) {
        super(Rarity.COMMON, EnchantmentCategory.valueOf("ALL"), pApplicableSlots);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "enchantment.theft.pocket_chain.description";
    }
}
