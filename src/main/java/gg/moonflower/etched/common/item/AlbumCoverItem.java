package gg.moonflower.etched.common.item;

import gg.moonflower.etched.common.component.AlbumCoverComponent;
import gg.moonflower.etched.common.menu.AlbumCoverMenu;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

import java.util.List;

public class AlbumCoverItem extends Item implements ContainerItem {

    public AlbumCoverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!Etched.SERVER_CONFIG.useAlbumCoverMenu.get()) {
            if (player.isSecondaryUseActive()) {
                if (dropContents(stack, player)) {
                    this.playDropContentsSound(player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
                return InteractionResultHolder.pass(stack);
            }

            return InteractionResultHolder.fail(stack);
        }
        return this.use(this, level, player, hand);
    }

    @Override
    public AbstractContainerMenu constructMenu(int containerId, Inventory inventory, Player player, int index) {
        return new AlbumCoverMenu(containerId, inventory, index);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickAction, Player player) {
        if (Etched.SERVER_CONFIG.useAlbumCoverMenu.get()) {
            return false;
        }
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }

        AlbumCoverComponent.Builder albumCover = AlbumCoverComponent.builder(stack);
        ItemStack clickItem = slot.getItem();
        if (clickItem.isEmpty()) {
            ItemStack removed = albumCover.extract(slot.container);
            if (!removed.isEmpty()) {
                this.playRemoveOneSound(player);
                ItemStack remaining = slot.safeInsert(removed);
                if (!remaining.isEmpty()) {
                    albumCover.insert(remaining);
                    return true;
                }
            }
        } else {
            int count = slot.getItem().getCount();
            ItemStack remaining = albumCover.insert(slot.getItem());
            if (count != remaining.getCount()) {
                this.playInsertSound(player);
                stack.set(EtchedComponents.ALBUM_COVER, albumCover.build());
                slot.safeTake(count, count - remaining.getCount(), player);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack clickItem, Slot slot, ClickAction clickAction, Player player, SlotAccess access) {
        if (Etched.SERVER_CONFIG.useAlbumCoverMenu.get()) {
            return false;
        }
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        if (!slot.allowModification(player)) {
            return false;
        }

        AlbumCoverComponent.Builder albumCover = AlbumCoverComponent.builder(stack);
        if (clickItem.isEmpty()) {
            ItemStack removed = albumCover.extract(slot.container);
            if (!removed.isEmpty()) {
                this.playRemoveOneSound(player);
                access.set(removed);
                return true;
            }
        } else {
            ItemStack remaining = albumCover.insert(clickItem);
            if (clickItem.getCount() != remaining.getCount()) {
                this.playInsertSound(player);
                access.set(remaining);
                stack.set(EtchedComponents.ALBUM_COVER, albumCover.build());
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        AlbumCoverComponent albumCover = itemEntity.getItem().get(EtchedComponents.ALBUM_COVER);
        if (albumCover != null) {
            List<ItemStack> items = albumCover.getItems();
            AlbumCoverComponent.Builder builder = albumCover.toBuilder();
            builder.clearContent();
            itemEntity.getItem().set(EtchedComponents.ALBUM_COVER, builder.build());
            ItemUtils.onContainerDestroyed(itemEntity, items);
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static boolean dropContents(ItemStack stack, Player player) {
        AlbumCoverComponent albumCover = stack.get(EtchedComponents.ALBUM_COVER);
        if (albumCover != null && !albumCover.isEmpty()) {
            List<ItemStack> items = albumCover.getItems();
            AlbumCoverComponent.Builder builder = albumCover.toBuilder();
            builder.clearContent();
            stack.set(EtchedComponents.ALBUM_COVER, builder.build());
            if (player instanceof ServerPlayer) {
                items.forEach(item -> player.drop(item, true));
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        AlbumCoverComponent albumCover = stack.get(EtchedComponents.ALBUM_COVER);
        return albumCover != null && albumCover.getCoverStack() != null;
    }
}
