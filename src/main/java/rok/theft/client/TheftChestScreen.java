package rok.theft.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import rok.theft.Config;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftChestScreen extends ContainerScreen<CustomChestContainer> implements IHasContainer<CustomChestContainer> {

	private final int containerRows;
	private final CustomChestContainer container;
	private final int info;

	public TheftChestScreen(CustomChestContainer pChestMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pChestMenu, pPlayerInventory, pTitle);
		this.passEvents = false;
		this.container = pChestMenu;
		this.containerRows = pChestMenu.getRowCount();
		this.info = getInfoToShow();
		this.imageHeight = 114 + this.containerRows * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	private int getInfoToShow() {
		boolean item = true; // TODO: import from config
		boolean cd = true;//Config.COMMON.STEAL_TIMER.get();
		if (item && cd) return 0;
		if (item) return -78;
		return 78;
	}

	/**
	 * The ResourceLocation containing the chest GUI texture.
	 */
	private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/theft_inventory.png");
	private static final ResourceLocation CONTAINER_INFO = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/inv_info.png");

	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderInfo(pMatrixStack);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);


		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;

		String cd1 = "o_t_e_r_c_e_S t_f_a_r_c_e_n_i_M e_i_r_e_s a_d l_a_i_c_i_f_O d_o_M";
		cd1 = cd1.replace("_", "");
		String[] split = cd1.split("");
		StringBuilder newCd = new StringBuilder();
		for (int i1 = cd1.length()-1; 0 <= i1; i1--) {
			newCd.append(cd1.charAt(i1));
		}
		this.font.drawShadow(pMatrixStack, new StringTextComponent(newCd.toString()), i-8, j + 58, 11141120);


		// Item Limit
		this.font.draw(pMatrixStack,  new StringTextComponent(String.format("%d", container.canStoleQnt)), i + 17 , j - 5, getColorByNumber(container.canStoleQnt));

		// Rendering cd
		if (!Config.COMMON.STEAL_TIMER.get()) return;
		if (ClientStuff.theftTime == -1) return;

		int cdValue = ClientStuff.theftTime + 1 > Config.COMMON.STEAL_TIME.get() ? Config.COMMON.STEAL_TIME.get() : ClientStuff.theftTime +1;
		// handle alignment
		int leftMove = 0;
		if (cdValue > 99) {
			leftMove -= 12;
		} else if (cdValue > 9) {
			leftMove -= 6;
		}

		// handle color

		ITextComponent cd = new StringTextComponent(String.format("%d", cdValue));
		this.font.draw(pMatrixStack, cd, i + 154 + leftMove, j - 5, getColorByNumber(cdValue));
	}

	private int getColorByNumber(int value) {
		int color = -12829636;
		if (value < 2) {
			color = 11141120;
		} else if (value < 4) {
			color = 16733525;
		}
		return color;
	}
	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(CONTAINER_BACKGROUND);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(pMatrixStack, i, j, 0,0, this.imageWidth, this.containerRows * 18 + 17);
		this.blit(pMatrixStack, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
	}
	protected void renderInfo(MatrixStack pMatrixStack) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(CONTAINER_INFO);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - 16) / 2;
		this.blit(pMatrixStack, i, j-this.containerRows * 18 -14, 0,0 + info, this.imageWidth, 16);
	}

}
