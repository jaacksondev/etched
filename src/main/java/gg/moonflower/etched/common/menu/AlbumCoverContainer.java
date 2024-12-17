package gg.moonflower.etched.common.menu;

import gg.moonflower.etched.common.component.AlbumCoverComponent;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class AlbumCoverContainer implements Container {

    private final AlbumCoverComponent.Builder builder;
    private final Inventory inventory;
    private final int index;

    public AlbumCoverContainer(Inventory inventory, int index) {
        this.builder = AlbumCoverComponent.builder(inventory.getItem(index));
        this.inventory = inventory;
        this.index = index;
    }

    @Override
    public int getContainerSize() {
        return this.builder.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.builder.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.builder.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack removed = this.builder.removeItem(slot, amount);
        this.setChanged();
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = this.builder.removeItemNoUpdate(slot);
        this.setChanged();
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.builder.setItem(slot, stack);
        this.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return this.builder.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return this.builder.getMaxStackSize(stack);
    }

    @Override
    public void setChanged() {
        this.builder.setChanged();
        this.inventory.getItem(this.index).set(EtchedComponents.ALBUM_COVER, this.builder.build());
    }

    @Override
    public boolean stillValid(Player player) {
        return this.builder.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
        this.builder.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        this.builder.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.builder.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return this.builder.canTakeItem(target, slot, stack);
    }

    @Override
    public void clearContent() {
        this.builder.clearContent();
    }
}
