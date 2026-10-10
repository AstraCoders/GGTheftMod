package rok.theft.enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class PocketChainEnchant {
    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("theft", "pocket_chain")
    );

    private PocketChainEnchant() {}
}
