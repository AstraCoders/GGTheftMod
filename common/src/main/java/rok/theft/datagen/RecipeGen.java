package rok.theft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import rok.theft.init.Registry;

import java.util.concurrent.CompletableFuture;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/04/2024
 */
public class RecipeGen extends RecipeProvider {
	private static final TagKey<Item> LEATHER = TagKey.create(
			Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "leathers"));

	public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	public void buildRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Registry.THIEF_GLOVE.get())
				.define('L', LEATHER)
				.pattern("LLL")
				.pattern(" LL")
				.pattern("LLL")
				.unlockedBy("has_item", has(LEATHER))
				.save(output);

	}
}
