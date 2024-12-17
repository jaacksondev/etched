package gg.moonflower.etched.common.menu;

import gg.moonflower.etched.common.component.AlbumCoverComponent;
import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class AlbumCoverMenu extends AbstractContainerMenu {

    private final Container albumCoverInventory;

    public AlbumCoverMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, -1);
    }

    public AlbumCoverMenu(int containerId, Inventory inventory, int albumCoverIndex) {
        super(EtchedMenus.ALBUM_COVER_MENU.get(), containerId);
        this.albumCoverInventory = albumCoverIndex == -1 ? new SimpleContainer(AlbumCoverComponent.MAX_RECORDS) : new AlbumCoverContainer(inventory, albumCoverIndex);

        for (int n = 0; n < 3; ++n) {
            for (int m = 0; m < 3; ++m) {
                this.addSlot(new Slot(this.albumCoverInventory, m + n * 3, 62 + m * 18, 17 + n * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return AlbumCoverComponent.isValid(stack);
                    }
                });
            }
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, y * 18 + 84) {
                    @Override
                    public boolean mayPickup(Player player) {
                        return this.getItem().getItem() != EtchedItems.ALBUM_COVER.get();
                    }
                });
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142) {
                @Override
                public boolean mayPickup(Player player) {
                    return this.getItem().getItem() != EtchedItems.ALBUM_COVER.get();
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.albumCoverInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < this.albumCoverInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.albumCoverInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.albumCoverInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }
}
