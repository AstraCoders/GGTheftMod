package rok.theft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
public final class DataGenerator {
    private DataGenerator() {}

    public static void onGatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        event.getGenerator().addProvider(event.includeServer(),
                new RecipeGen(output, event.getLookupProvider()));
    }
}
