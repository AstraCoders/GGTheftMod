package rok.theft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraftforge.common.Tags;
import rok.theft.init.Registry;

import java.util.function.Consumer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/04/2024
 */
public class RecipeGen extends RecipeProvider {

	public RecipeGen(PackOutput arg) {
		super(arg);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Registry.THIEF_GLOVE.get())
				.define('L', Tags.Items.LEATHER)
				.pattern("LLL")
				.pattern(" LL")
				.pattern("LLL")
				.unlockedBy("has_item", has(Tags.Items.LEATHER))
				.save(consumer);

	}
}
