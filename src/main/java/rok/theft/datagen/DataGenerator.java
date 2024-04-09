package rok.theft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rok.theft.Theft;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/04/2024
 */
@Mod.EventBusSubscriber(modid = Theft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent e) {
		PackOutput output = e.getGenerator().getPackOutput();
		e.getGenerator().addProvider(e.includeServer(), new RecipeGen(output));
	}

}
