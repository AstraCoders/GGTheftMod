package rok.theft.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftChestScreen extends ContainerScreen<CustomChestContainer> implements IHasContainer<CustomChestContainer> {

	private final int containerRows;

	public TheftChestScreen(CustomChestContainer pChestMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pChestMenu, pPlayerInventory, pTitle);
		this.passEvents = false;
		int i = 222;
		int j = 114;
		this.containerRows = pChestMenu.getRowCount();
		this.imageHeight = 114 + this.containerRows * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	/**
	 * The ResourceLocation containing the chest GUI texture.
	 */
	private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/theft_inventory.png");

	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
	}

	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(CONTAINER_BACKGROUND);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
		this.blit(pMatrixStack, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
	}

}
