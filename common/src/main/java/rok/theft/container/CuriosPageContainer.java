package rok.theft.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 11/10/2026
 */
public class CuriosPageContainer implements Container {
    private static final int MAX_COLUMNS = 8;
    private static final int MAX_ROWS = 8;
    private final Container target;
    private final int totalSlots;
    private final int columns;
    private final int pageSize;
    private int page;

    CuriosPageContainer(Container target, int totalSlots) {
        this.target = target;
        this.totalSlots = totalSlots;
        this.columns = totalSlots == 0 ? 0 : Math.min(MAX_COLUMNS, (Math.min(totalSlots, MAX_COLUMNS * MAX_ROWS) + MAX_ROWS - 1) / MAX_ROWS);
        this.pageSize = this.columns * MAX_ROWS;
    }

    int getColumns() {
        return columns;
    }

    int getPageCount() {
        return pageSize == 0 ? 0 : (totalSlots + pageSize - 1) / pageSize;
    }

    int getPage() {
        return page;
    }

    int getVisibleSlots() {
        return Math.min(pageSize, totalSlots - page * pageSize);
    }

    int getRowsInColumn(int column) {
        if (columns == 0) return 0;
        if (getPageCount() > 1) return MAX_ROWS;
        int visibleSlots = getVisibleSlots();
        return visibleSlots / columns + (column < visibleSlots % columns ? 1 : 0);
    }

    boolean setPage(int page) {
        int nextPage = Math.max(0, Math.min(page, getPageCount() - 1));
        if (nextPage == this.page) return false;
        this.page = nextPage;
        return true;
    }

    @Override
    public int getContainerSize() {
        return pageSize;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getVisibleSlots(); i++) {
            if (!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        int targetIndex = getTargetIndex(index);
        return targetIndex < 0 ? ItemStack.EMPTY : target.getItem(targetIndex);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        int targetIndex = getTargetIndex(index);
        return targetIndex < 0 ? ItemStack.EMPTY : target.removeItem(targetIndex, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        int targetIndex = getTargetIndex(index);
        return targetIndex < 0 ? ItemStack.EMPTY : target.removeItemNoUpdate(targetIndex);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        int targetIndex = getTargetIndex(index);
        if (targetIndex >= 0) target.setItem(targetIndex, stack);
    }

    @Override
    public void setChanged() {
        target.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return target.stillValid(player);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return false;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getVisibleSlots(); i++) removeItem(i, Integer.MAX_VALUE);
    }

    private int getTargetIndex(int index) {
        int targetIndex = 45 + page * pageSize + index;
        return index >= 0 && index < getVisibleSlots() && targetIndex < target.getContainerSize() ? targetIndex : -1;
    }
}
