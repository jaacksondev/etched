package gg.moonflower.etched.common.recipe;

import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MusicDiscCloningRecipe extends CustomRecipe {

    public MusicDiscCloningRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        ItemStack base = ItemStack.EMPTY;
        ItemStack copy = ItemStack.EMPTY;

        for (int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack stack = inv.getItem(j);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ItemTags.MUSIC_DISCS)) {
                if (!base.isEmpty()) {
                    return false;
                }

                base = stack;
            } else {
                if (!copy.isEmpty() || !stack.is(EtchedItems.BLANK_MUSIC_DISC.get())) {
                    return false;
                }

                copy = stack;
            }
        }

        return !base.isEmpty() && !copy.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack base = ItemStack.EMPTY;
        ItemStack copy = ItemStack.EMPTY;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack stack = container.getItem(j);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ItemTags.MUSIC_DISCS)) {
                if (!base.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                base = stack;
            } else {
                if (!copy.isEmpty() || !stack.is(EtchedItems.BLANK_MUSIC_DISC.get())) {
                    return ItemStack.EMPTY;
                }

                copy = stack;
            }
        }

        if (base.isEmpty() || copy.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return base.copyWithCount(1);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            } else if (stack.is(ItemTags.MUSIC_DISCS)) {
                list.set(i, stack.copyWithCount(1));
            }
        }

        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EtchedRecipes.CLONE_MUSIC_DISC.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
}
