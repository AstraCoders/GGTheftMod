package rok.theft.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import rok.theft.Theft;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class CustomChestContainer extends ChestContainer {
	public CustomChestContainer(int pContainerId, PlayerInventory pPlayerInventory) {
		this(pContainerId, pPlayerInventory, new Inventory(5 * 9));
	}

	public CustomChestContainer(int pContainerId, PlayerInventory pPlayerInventory, IInventory pContainer) {
		super(Theft.CUSTOM_CHEST.get(), pContainerId, pPlayerInventory, pContainer, 5);
	}
}
