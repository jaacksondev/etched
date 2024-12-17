package gg.moonflower.etched.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.etched.api.record.PlayableRecord;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AlbumCoverComponent implements TooltipProvider {

    public static final Codec<AlbumCoverComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.optionalEmptyMap(ItemStack.SINGLE_ITEM_CODEC)
                    .xmap(stack -> stack.orElse(ItemStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack))
                    .listOf(0, 9)
                    .fieldOf("items")
                    .forGetter(AlbumCoverComponent::getItems),
            ItemStack.OPTIONAL_CODEC
                    .fieldOf("coverStack")
                    .forGetter(AlbumCoverComponent::getCoverStack)
    ).apply(instance, AlbumCoverComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AlbumCoverComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(9)),
            AlbumCoverComponent::getItems,
            ItemStack.OPTIONAL_STREAM_CODEC,
            AlbumCoverComponent::getCoverStack,
            AlbumCoverComponent::new);
    public static final int MAX_RECORDS = 9;

    private final NonNullList<ItemStack> items;
    private final List<ItemStack> itemsView;
    private final ItemStack coverStack;

    private AlbumCoverComponent(List<ItemStack> items, ItemStack coverStack) {
        this.items = NonNullList.withSize(MAX_RECORDS, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(this.items.size(), items.size()); i++) {
            ItemStack stack = items.get(i);
            if (stack != null && !stack.isEmpty()) {
                this.items.set(i, stack);
            }
        }
        this.itemsView = Collections.unmodifiableList(this.items);
        this.coverStack = coverStack;
        if (!this.coverStack.isEmpty()) {
            this.coverStack.setCount(1);
        }
    }

    public ItemStack getRecord(int slot) {
        return slot >= 0 && slot < this.items.size() ? this.items.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack getCoverStack() {
        return this.coverStack;
    }

    public List<ItemStack> getItems() {
        return this.itemsView;
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ItemStack stack) {
        AlbumCoverComponent.Builder builder;
        AlbumCoverComponent component = stack.get(EtchedComponents.ALBUM_COVER);
        if (component != null) {
            builder = component.toBuilder();
        } else {
            builder = AlbumCoverComponent.builder();
        }
        return builder;
    }

    public static boolean isValid(ItemStack stack) {
        return PlayableRecord.isPlayableRecord(stack) && !stack.has(EtchedComponents.ALBUM_COVER);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        AlbumCoverComponent component = (AlbumCoverComponent) o;
        return ItemStack.listMatches(this.items, component.items) && ItemStack.matches(this.coverStack, component.coverStack);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (ItemStack record : this.items) {
            if (!record.isEmpty()) {
                result = result * 31 + ItemStack.hashItemAndComponents(record);
            }
        }
        result = 31 * result + ItemStack.hashItemAndComponents(this.coverStack);
        return result;
    }

    @Override
    public String toString() {
        return "AlbumCoverComponent[items=" + this.items + ", coverStack=" + this.coverStack + ']';
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        for (ItemStack record : this.items) {
            if (!record.isEmpty()) {
                record.addToTooltip(DataComponents.JUKEBOX_PLAYABLE, context, tooltipAdder, tooltipFlag);
                PlayableRecord.addToTooltip(record, context, tooltipAdder);
            }
        }
    }

    public static class Builder implements Container {

        private final NonNullList<ItemStack> items;
        private ItemStack coverStack;
        private boolean coverStackSet;

        public Builder() {
            this.items = NonNullList.withSize(MAX_RECORDS, ItemStack.EMPTY);
            this.coverStack = ItemStack.EMPTY;
        }

        private Builder(AlbumCoverComponent component) {
            this();
            for (int i = 0; i < MAX_RECORDS; i++) {
                this.items.set(i, component.items.get(i));
            }
            this.coverStack = component.coverStack;
        }

        private void updateCover(ItemStack stack) {
            if (!stack.isEmpty() && !this.coverStackSet && this.coverStack.isEmpty()) {
                this.coverStack = stack;
            }
        }

        public ItemStack insert(ItemStack insert) {
            if (insert.isEmpty()) {
                return ItemStack.EMPTY;
            }

            ItemStack copy = insert.copy();
            for (int i = 0; i < this.items.size(); i++) {
                if (!this.canPlaceItem(i, copy)) {
                    continue;
                }

                ItemStack stack = this.items.get(i);
                int split = Math.min(copy.getCount(), this.getMaxStackSize(copy) - stack.getCount());
                if (split <= 0) {
                    continue;
                }

                if (stack.isEmpty()) {
                    this.items.set(i, copy.split(split));
                    this.updateCover(this.items.get(i));
                } else if (ItemStack.isSameItemSameComponents(stack, copy)) {
                    copy.shrink(split);
                    stack.grow(split);
                    this.updateCover(stack);
                }
                if (copy.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }

            return copy;
        }

        public ItemStack extract(Container inventory) {
            for (int i = this.items.size() - 1; i >= 0; i--) {
                ItemStack stack = this.items.get(i);
                if (!this.canTakeItem(inventory, i, stack)) {
                    continue;
                }

                if (!stack.isEmpty()) {
                    return this.removeItem(i, stack.getMaxStackSize());
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return Math.min(this.getMaxStackSize(), stack.getMaxStackSize());
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return isValid(stack);
        }

        @Override
        public int getContainerSize() {
            return this.items.size();
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack stack : this.items) {
                if (!stack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return slot >= 0 && slot < this.items.size() ? this.items.get(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            return ContainerHelper.removeItem(this.items, slot, amount);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack stack = this.getItem(slot);
            if (!stack.isEmpty()) {
                this.items.set(slot, ItemStack.EMPTY);
            }
            return stack;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (slot >= 0 && slot < this.items.size()) {
                this.items.set(slot, stack);
                this.updateCover(stack);
            }
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            this.items.clear();
        }

        public Builder setCoverStack(ItemStack coverStack) {
            this.coverStack = coverStack;
            this.coverStackSet = true;
            return this;
        }

        public AlbumCoverComponent build() {
            return new AlbumCoverComponent(this.items, this.coverStack.copyWithCount(1));
        }
    }
}
