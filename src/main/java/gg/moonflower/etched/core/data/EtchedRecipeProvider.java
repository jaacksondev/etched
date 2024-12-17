package gg.moonflower.etched.core.data;

import gg.moonflower.etched.common.recipe.MusicDiscCloningRecipe;
import gg.moonflower.etched.common.recipe.MusicLabelDyeRecipe;
import gg.moonflower.etched.common.recipe.MusicLabelMergeRecipe;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class EtchedRecipeProvider extends RecipeProvider {

    public EtchedRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        TagKey<Item> paperTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "paper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EtchedItems.MUSIC_LABEL)
                .define('P', paperTag)
                .pattern(" P ")
                .pattern("P P")
                .pattern(" P ")
                .unlockedBy("has_paper", has(paperTag))
                .save(recipeOutput);
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(Tags.Items.MUSIC_DISCS),
                RecipeCategory.TOOLS,
                EtchedItems.BLANK_MUSIC_DISC,
                0.2F,
                200)
                .unlockedBy("has_music_disc", has(Tags.Items.MUSIC_DISCS))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, EtchedItems.JUKEBOX_MINECART)
                .requires(Blocks.JUKEBOX)
                .requires(Items.MINECART)
                .unlockedBy("has_minecart", has(Items.MINECART))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EtchedItems.BOOMBOX)
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('J', Blocks.JUKEBOX)
                .pattern(" I ")
                .pattern("IRI")
                .pattern("IJI")
                .unlockedBy("has_jukebox", has(Blocks.JUKEBOX))
                .unlockedBy("has_music_disc", has(Tags.Items.MUSIC_DISCS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EtchedItems.ALBUM_COVER)
                .define('P', paperTag)
                .define('M', EtchedItems.MUSIC_LABEL)
                .pattern("PPP")
                .pattern("PMP")
                .pattern("PPP")
                .unlockedBy("has_etched_music_disc", has(EtchedItems.ETCHED_MUSIC_DISC))
                .unlockedBy("has_music_label", has(EtchedItems.MUSIC_LABEL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EtchedBlocks.ETCHING_TABLE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .pattern(" DI")
                .pattern("PPP")
                .unlockedBy("has_jukebox", has(Blocks.JUKEBOX))
                .unlockedBy("has_blank_music_disc", has(EtchedItems.BLANK_MUSIC_DISC))
                .unlockedBy("has_music_label", has(EtchedItems.MUSIC_LABEL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EtchedBlocks.ALBUM_JUKEBOX)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('H', Items.REPEATER)
                .define('J', Blocks.JUKEBOX)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .pattern("RHR")
                .pattern("RJR")
                .pattern("RCR")
                .unlockedBy("has_jukebox", has(Blocks.JUKEBOX))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EtchedBlocks.RADIO)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Items.COPPER_INGOT)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Blocks.NOTE_BLOCK)
                .define('P', ItemTags.PLANKS)
                .pattern("RCR")
                .pattern("INI")
                .pattern("PPP")
                .unlockedBy("has_note_block", has(Blocks.NOTE_BLOCK))
                .unlockedBy("has_jukebox", has(Blocks.JUKEBOX))
                .unlockedBy("has_music_disc", has(Tags.Items.MUSIC_DISCS))
                .save(recipeOutput);

        SpecialRecipeBuilder.special(MusicLabelMergeRecipe::new).save(recipeOutput, Etched.etchedPath("merge_music_label"));
        SpecialRecipeBuilder.special(MusicDiscCloningRecipe::new).save(recipeOutput, Etched.etchedPath("music_disc_cloning"));
        SpecialRecipeBuilder.special(MusicLabelDyeRecipe::new).save(recipeOutput, Etched.etchedPath("dye_music_label"));
    }
}
