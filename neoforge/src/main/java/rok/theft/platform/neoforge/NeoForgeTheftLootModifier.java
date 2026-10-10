package rok.theft.platform.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import rok.theft.Config;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class NeoForgeTheftLootModifier extends LootModifier {
    private final Item item;

    public static final MapCodec<NeoForgeTheftLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance).and(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(modifier -> modifier.item)
            ).apply(instance, NeoForgeTheftLootModifier::new));

    public NeoForgeTheftLootModifier(LootItemCondition[] conditions, Item item) {
        super(conditions);
        this.item = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        if (!Config.COMMON.GENERATE_LOOT.get()
                || context.getRandom().nextDouble() <= (double) Config.COMMON.GENERATE_LOOT_CHANCE.get() / 100) return loot;
        loot.add(new ItemStack(item));
        return loot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
