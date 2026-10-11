package rok.theft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import rok.theft.Config;
import rok.theft.Theft;
import rok.theft.container.CustomChestContainer;
import rok.theft.container.CustomSlot;

/*
 * @author Rok, Pedro Lucas N M Machado created on 12/08/2022
 */
public class TheftChestScreen extends AbstractContainerScreen<CustomChestContainer> implements MenuAccess<CustomChestContainer> {

    private final int containerRows;
    private final CustomChestContainer container;
    private final boolean infoItem;
    private final boolean infoTime;
    private Button previousCuriosPage;
    private Button nextCuriosPage;

    public TheftChestScreen(CustomChestContainer pChestMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pChestMenu, pPlayerInventory, pTitle);
        this.container = pChestMenu;
        this.containerRows = pChestMenu.getRowCount();
        this.infoItem = Config.COMMON.LIMIT_STEAL_ITEM.get();
        this.infoTime = Config.COMMON.STEAL_TIMER.get();
        this.imageHeight = 114 + this.containerRows * 18;
        this.imageWidth += pChestMenu.getCuriosPanelWidth();
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelX = pChestMenu.getCuriosPanelWidth() + 8;
        this.inventoryLabelX = pChestMenu.getCuriosPanelWidth() + 8;
    }

    @Override
    protected void init() {
        super.init();
        if (container.getCuriosPageCount() < 2) return;
        int center = this.leftPos + container.getCuriosPanelWidth() / 2;
        this.previousCuriosPage = Button.builder(Component.literal("<"), button -> changeCuriosPage(0))
                .bounds(center - 13, this.topPos + 1, 12, 12).build();
        this.nextCuriosPage = Button.builder(Component.literal(">"), button -> changeCuriosPage(1))
                .bounds(center + 1, this.topPos + 1, 12, 12).build();
        updateCuriosPageButtons();
        this.addRenderableWidget(this.previousCuriosPage);
        this.addRenderableWidget(this.nextCuriosPage);
    }

    private void changeCuriosPage(int buttonId) {
        if (this.minecraft == null || this.minecraft.player == null || this.minecraft.gameMode == null) return;
        if (this.container.clickMenuButton(this.minecraft.player, buttonId)) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.container.containerId, buttonId);
            updateCuriosPageButtons();
        }
    }

    private void updateCuriosPageButtons() {
        if (this.previousCuriosPage != null) this.previousCuriosPage.active = container.getCuriosPage() > 0;
        if (this.nextCuriosPage != null) this.nextCuriosPage.active = container.getCuriosPage() + 1 < container.getCuriosPageCount();
    }

    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CONTAINER_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Theft.MOD_ID, "textures/gui/container/theft_inventory.png");
    private static final ResourceLocation CONTAINER_INFO_ITEM = ResourceLocation.fromNamespaceAndPath(Theft.MOD_ID, "textures/gui/container/inv_info_item.png");
    private static final ResourceLocation CONTAINER_INFO_TIME = ResourceLocation.fromNamespaceAndPath(Theft.MOD_ID, "textures/gui/container/inv_info_time.png");
    private static final ResourceLocation CURIOS_BACKGROUND = ResourceLocation.fromNamespaceAndPath("curios", "textures/gui/curios/inventory.png");

    public void render(GuiGraphics pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        renderCuriosSlotIcons(pMatrixStack);
        if (infoTime) this.renderInfo(pMatrixStack, CONTAINER_INFO_TIME);
        if (infoItem) this.renderInfo(pMatrixStack, CONTAINER_INFO_ITEM);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
        if (this.hoveredSlot instanceof CustomSlot slot && !slot.hasItem() && slot.getCuriosTooltip() != null) {
            pMatrixStack.renderTooltip(this.font, slot.getCuriosTooltip(), pMouseX, pMouseY);
        }


        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;


//        pMatrixStack.drawString(this.font, Component.literal("By Rok and Carmello"), i + 10, j + 58, 255255255); // 11141120 - i-8, j + 58

        // Item Limit
        if (Config.COMMON.LIMIT_STEAL_ITEM.get())
            pMatrixStack.drawString(this.font, Component.literal(String.format("%d", container.canStoleQnt)), i + container.getCuriosPanelWidth() + 17, j - 5, getColorByNumber(container.canStoleQnt));

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

        pMatrixStack.drawString(this.font, Component.literal(String.format("%d", cdValue)), i + container.getCuriosPanelWidth() + 154 + leftMove, j - 5, getColorByNumber(cdValue));
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
        int menuLeft = i + container.getCuriosPanelWidth();
        pMatrixStack.blit(CONTAINER_BACKGROUND, menuLeft, j, 0, 0, 176, this.containerRows * 18 + 17);
        pMatrixStack.blit(CONTAINER_BACKGROUND, menuLeft, j + this.containerRows * 18 + 17, 0, 126, 176, 96);
        if (container.getCuriosColumns() > 0) renderCuriosPanel(pMatrixStack, menuLeft, j);
    }

    private void renderCuriosSlotIcons(GuiGraphics graphics) {
        for (var menuSlot : container.slots) {
            if (menuSlot instanceof CustomSlot slot && slot.isCuriosSlot() && slot.isActive() && !slot.hasItem() && slot.getCuriosIcon() != null) {
                graphics.blit(this.leftPos + slot.x, this.topPos + slot.y, 0, 16, 16,
                        this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(slot.getCuriosIcon()));
            }
        }
    }

    private void renderCuriosPanel(GuiGraphics graphics, int menuLeft, int top) {
        int columns = container.getCuriosColumns();
        int xOffset = -33 - (columns - 1) * 18;
        int yOffset = top + 11;

        for (int column = 0; column < columns; column++) {
            int rows = container.getCuriosRowsInColumn(column);
            int panelHeight = 7 + rows * 18;
            int textureX = 91 + (column == 0 ? 0 : 7);
            graphics.blit(CURIOS_BACKGROUND, menuLeft + xOffset, yOffset, textureX, 0, 25, panelHeight);
            graphics.blit(CURIOS_BACKGROUND, menuLeft + xOffset, yOffset + panelHeight, textureX, 159, 25, 7);

            if (columns == 1) {
                graphics.blit(CURIOS_BACKGROUND, menuLeft + xOffset + 7, yOffset, textureX + 7, 0, 25, panelHeight);
                graphics.blit(CURIOS_BACKGROUND, menuLeft + xOffset + 7, yOffset + panelHeight, textureX + 7, 159, 25, 7);
            }
            xOffset += column == 0 ? 25 : 18;
        }

        xOffset -= columns * 18;
        for (int column = 0; column < columns; column++) {
            graphics.blit(CURIOS_BACKGROUND, menuLeft + xOffset, yOffset + 7, 7, 7, 18, container.getCuriosRowsInColumn(column) * 18);
            xOffset += 18;
        }
    }

    protected void renderInfo(GuiGraphics pMatrixStack, ResourceLocation FRAME) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2 + container.getCuriosPanelWidth();
        int j = (this.height - 16) / 2;
        pMatrixStack.blit(FRAME, i, j - this.containerRows * 18 - 14, 0, 0, 176, 16);
    }


}
