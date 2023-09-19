package rok.theft.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/*
 * @author Rok, Pedro Lucas N M Machado created on 31/08/2022
 */
public class PocketChainEnchant extends Enchantment {
	public PocketChainEnchant(EquipmentSlotType... pApplicableSlots) {
		super(Rarity.COMMON, EnchantmentType.valueOf("ALL"), pApplicableSlots);
	}

	@Override
	public String getDescriptionId() {
		return "enchantment.theft.pocket_chain.description";
	}
}
