package rok.theft.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab {

    public static final ItemGroup GLOVE_TAB = new ItemGroup("theft_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registry.THIEF_GLOVE.get());
        }
    };
}
