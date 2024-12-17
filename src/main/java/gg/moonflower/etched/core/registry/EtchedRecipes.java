package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.recipe.MusicDiscCloningRecipe;
import gg.moonflower.etched.common.recipe.MusicLabelDyeRecipe;
import gg.moonflower.etched.common.recipe.MusicLabelMergeRecipe;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EtchedRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Etched.MOD_ID);

    public static final Supplier<SimpleCraftingRecipeSerializer<MusicLabelMergeRecipe>> COMPLEX_MUSIC_LABEL = REGISTRY.register("merge_music_label", () -> new SimpleCraftingRecipeSerializer<>(MusicLabelMergeRecipe::new));
    public static final Supplier<SimpleCraftingRecipeSerializer<MusicDiscCloningRecipe>> CLONE_MUSIC_DISC = REGISTRY.register("music_disc_cloning", () -> new SimpleCraftingRecipeSerializer<>(MusicDiscCloningRecipe::new));
    public static final Supplier<SimpleCraftingRecipeSerializer<MusicLabelDyeRecipe>> DYE_MUSIC_LABEL = REGISTRY.register("dye_music_label", () -> new SimpleCraftingRecipeSerializer<>(MusicLabelDyeRecipe::new));
}