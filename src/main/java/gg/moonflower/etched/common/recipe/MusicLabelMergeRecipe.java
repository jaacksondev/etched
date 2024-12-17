package gg.moonflower.etched.common.recipe;

import gg.moonflower.etched.common.component.MusicLabelComponent;
import gg.moonflower.etched.core.registry.EtchedComponents;
import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MusicLabelMergeRecipe extends CustomRecipe {

    public MusicLabelMergeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int count = 0;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (!stack.has(EtchedComponents.MUSIC_LABEL)) {
                    return false;
                }
                count++;
            }
        }

        return count == 2;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        List<ItemStack> labels = new ArrayList<>(2);
        for (int j = 0; j < input.size(); ++j) {
            ItemStack stack = input.getItem(j);
            if (!stack.isEmpty()) {
                if (stack.has(EtchedComponents.MUSIC_LABEL)) {
                    labels.add(stack.copyWithCount(1));
                }
                if (labels.size() > 2) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (labels.size() != 2) {
            return ItemStack.EMPTY;
        }

        ItemStack first = labels.get(0);
        ItemStack second = labels.get(1);
        MusicLabelComponent firstLabel = first.getOrDefault(EtchedComponents.MUSIC_LABEL, MusicLabelComponent.DEFAULT);
        MusicLabelComponent secondLabel = second.getOrDefault(EtchedComponents.MUSIC_LABEL, MusicLabelComponent.DEFAULT);

        if (!firstLabel.simple() || !secondLabel.simple()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = first.copy();
        stack.set(EtchedComponents.MUSIC_LABEL, merge(firstLabel, secondLabel));
        return stack;
    }

    private static MusicLabelComponent merge(MusicLabelComponent first, MusicLabelComponent second) {
        boolean hasFirst = !first.equals(MusicLabelComponent.DEFAULT);
        boolean hasSecond = !second.equals(MusicLabelComponent.DEFAULT);
        if (hasFirst ^ hasSecond) {
            return hasFirst ? first : second;
        }
        if (!hasFirst) {
            return MusicLabelComponent.DEFAULT;
        }

        String author = first.artist().isBlank() ? second.artist() : first.artist();
        String title = first.title().isBlank() ? second.title() : first.title();
        if (title.isBlank()) {
            title = MusicLabelComponent.DEFAULT.title();
        }

        return new MusicLabelComponent(author, title, first.primaryColor(), second.primaryColor());
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(EtchedItems.MUSIC_LABEL.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EtchedRecipes.COMPLEX_MUSIC_LABEL.get();
    }
}