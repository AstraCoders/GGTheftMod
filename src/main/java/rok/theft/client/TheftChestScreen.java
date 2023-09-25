package rok.theft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import rok.theft.Config;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftChestScreen extends AbstractContainerScreen<CustomChestContainer> implements MenuAccess<CustomChestContainer> {

    private final int containerRows;
    private final CustomChestContainer container;
    private final boolean infoItem;
    private final boolean infoTime;

    public TheftChestScreen(CustomChestContainer pChestMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pChestMenu, pPlayerInventory, pTitle);
        this.container = pChestMenu;
        this.containerRows = pChestMenu.getRowCount();
        this.infoItem = Config.COMMON.LIMIT_STEAL_ITEM.get();
        this.infoTime = Config.COMMON.STEAL_TIMER.get();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/theft_inventory.png");
    private static final ResourceLocation CONTAINER_INFO_ITEM = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/inv_info_item.png");
    private static final ResourceLocation CONTAINER_INFO_TIME = new ResourceLocation(Theft.MOD_ID, "textures/gui/container/inv_info_time.png");

    public void render(GuiGraphics pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        if (infoTime) this.renderInfo(pMatrixStack, CONTAINER_INFO_TIME);
        if (infoItem) this.renderInfo(pMatrixStack, CONTAINER_INFO_ITEM);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);


        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;


        pMatrixStack.drawString(this.font, Component.literal("By Rok and Carmello"), i + 10, j + 58, 255255255); // 11141120 - i-8, j + 58


        // Item Limit
        if (Config.COMMON.LIMIT_STEAL_ITEM.get())
            pMatrixStack.drawString(this.font, Component.literal(String.format("%d", container.canStoleQnt)), i + 17, j - 5, getColorByNumber(container.canStoleQnt));

        // Rendering cd
        if (!Config.COMMON.STEAL_TIMER.get()) return;
        if (ClientStuff.theftTime == -1) return;

        int cdValue = ClientStuff.theftTime + 1 > Config.COMMON.STEAL_TIME.get() ? Config.COMMON.STEAL_TIME.get() : ClientStuff.theftTime + 1;
        // handle alignment
        int leftMove = 0;
        if (cdValue > 99) {
            leftMove -= 12;
        } else if (cdValue > 9) {
            leftMove -= 6;
        }

        pMatrixStack.drawString(this.font, Component.literal(String.format("%d", cdValue)), i + 154 + leftMove, j - 5, getColorByNumber(cdValue));
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

    protected void renderBg(GuiGraphics pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pMatrixStack.blit(CONTAINER_BACKGROUND, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        pMatrixStack.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    protected void renderInfo(GuiGraphics pMatrixStack, ResourceLocation FRAME) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - 16) / 2;
        pMatrixStack.blit(FRAME, i, j - this.containerRows * 18 - 14, 0, 0, this.imageWidth, 16);
    }


}
