package gg.moonflower.etched.common.recipe;

import gg.moonflower.etched.common.component.MusicLabelComponent;
import gg.moonflower.etched.core.registry.EtchedComponents;
import gg.moonflower.etched.core.registry.EtchedRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MusicLabelDyeRecipe extends CustomRecipe {

    public MusicLabelDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack label = ItemStack.EMPTY;
        List<ItemStack> dyes = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                MusicLabelComponent musicLabel = stack.get(EtchedComponents.MUSIC_LABEL);
                if (musicLabel != null) {
                    if (!label.isEmpty()) {
                        return false;
                    }
                    if (!musicLabel.simple()) {
                        return false;
                    }

                    label = stack;
                } else {
                    if (!(stack.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    dyes.add(stack);
                }
            }
        }

        return !label.isEmpty() && !dyes.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack label = ItemStack.EMPTY;
        List<DyeColor> dyes = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                MusicLabelComponent musicLabel = stack.get(EtchedComponents.MUSIC_LABEL);
                if (musicLabel != null) {
                    if (!label.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (!musicLabel.simple()) {
                        return ItemStack.EMPTY;
                    }

                    label = stack.copy();
                } else {
                    if (!(stack.getItem() instanceof DyeItem dyeitem)) {
                        return ItemStack.EMPTY;
                    }

                    dyes.add(dyeitem.getDyeColor());
                }
            }
        }

        if (label.isEmpty() || dyes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return applyDyes(label, dyes);
    }

    private static ItemStack applyDyes(ItemStack stack, Iterable<DyeColor> dyes) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int luminocity = 0;
        int sum = 0;

        MusicLabelComponent label = stack.get(EtchedComponents.MUSIC_LABEL);
        if (label != null) {
            if (label.primaryColor() != 0xFFFFFF) {
                int color = label.primaryColor();
                int r = FastColor.ARGB32.red(color);
                int g = FastColor.ARGB32.green(color);
                int b = FastColor.ARGB32.blue(color);
                luminocity += Math.max(r, Math.max(g, b));
                red += r;
                green += g;
                blue += b;
                sum++;
            }
        } else {
            label = MusicLabelComponent.EMPTY;
        }

        for (DyeColor dyeColor : dyes) {
            int color = dyeColor.getTextureDiffuseColor();
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            luminocity += Math.max(r, Math.max(g, b));
            red += r;
            green += g;
            blue += b;
            sum++;
        }

        int avgRed = red / sum;
        int avgGreen = green / sum;
        int avgBlue = blue / sum;
        float avgLuminosity = (float) luminocity / (float) sum;
        float brightness = (float) Math.max(avgRed, Math.max(avgGreen, avgBlue));
        stack.set(EtchedComponents.MUSIC_LABEL, label.withColor(FastColor.ARGB32.color(0, (int) (avgRed * avgLuminosity / brightness), (int) (avgGreen * avgLuminosity / brightness), (int) (avgBlue * avgLuminosity / brightness))));
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EtchedRecipes.DYE_MUSIC_LABEL.get();
    }
}
