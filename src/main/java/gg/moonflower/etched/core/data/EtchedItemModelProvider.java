package gg.moonflower.etched.core.data;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.etched.common.component.DiscAppearanceComponent;
import gg.moonflower.etched.core.Etched;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedItems;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EtchedItemModelProvider extends ItemModelProvider {

    public EtchedItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Etched.MOD_ID, existingFileHelper);
    }

    public ItemModelBuilder basicItem(ItemLike item) {
        return this.basicItem(item.asItem());
    }

    private ItemModelBuilder generated(String name) {
        return this.getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated"));
    }

    private ItemModelBuilder generated(ItemLike item) {
        return this.generated(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())).toString());
    }

    public ItemModelBuilder getBuilder(ItemLike item) {
        return this.getBuilder(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem())).toString());
    }

    @Override
    protected void registerModels() {
        this.generated(EtchedItems.MUSIC_LABEL)
                .texture("layer0", Etched.etchedPath("item/music_label_top"))
                .texture("layer1", Etched.etchedPath("item/music_label_bottom"));
        this.basicItem(EtchedItems.BLANK_MUSIC_DISC);

        ItemModelBuilder etchedMusicDisc = this.getBuilder(EtchedItems.ETCHED_MUSIC_DISC);
        for (DiscAppearanceComponent.LabelPattern value : DiscAppearanceComponent.LabelPattern.values()) {
            Pair<ResourceLocation, ResourceLocation> textures = value.getTextures();

            ItemModelBuilder builder = this.generated("item/etched_music_disc/" + value.name().toLowerCase(Locale.ROOT));
            builder.texture("layer0", etchedMusicDisc.getLocation());
            builder.texture("layer1", textures.getFirst().withPath(p -> p.substring(9, p.length() - 4)));
            if (value.isComplex()) {
                builder.texture("layer2", textures.getSecond().withPath(p -> p.substring(9, p.length() - 4)));
            }

            if (value.ordinal() == 0) {
                etchedMusicDisc.parent(builder);
            }
            etchedMusicDisc.override().predicate(Etched.etchedPath("pattern"), value.ordinal()).model(builder);
        }

        this.generated("boombox_gui").texture("layer0", Etched.etchedPath("item/boombox"));
        this.basicItem(EtchedItems.JUKEBOX_MINECART);

        // Album Cover
        this.getBuilder(EtchedItems.ALBUM_COVER)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.FRONT);
        this.generated("item/etched_album_cover/blank")
                .texture("layer0", Etched.etchedPath("item/blank_album_cover"));
        this.generated("item/etched_album_cover/default")
                .texture("layer0", Etched.etchedPath("item/default_album_cover"));
        // Vanilla Album Covers
        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            ResourceLocation name = entry.getKey().location();
            Item item = entry.getValue();
            if (item.components().has(DataComponents.JUKEBOX_PLAYABLE)) {
                this.generated(name.withPath(p -> "item/etched_album_cover/" + p).toString())
                        .texture("layer0", Etched.etchedPath("item/vanilla_album_cover"));
            }
        }

        ItemModelBuilder boomboxInventory = new ItemModelBuilder(Etched.etchedPath("boombox_gui"), this.existingFileHelper)
                .parent(new ModelFile.ExistingModelFile(Etched.etchedPath("item/boombox_gui"), this.existingFileHelper));
        ItemModelBuilder boomboxPlaying = this.getBuilder("item/boombox_playing")
                .parent(new ModelFile.ExistingModelFile(Etched.etchedPath("item/boombox_in_hand_playing"), this.existingFileHelper))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(new ItemModelBuilder(Etched.etchedPath("boombox_in_hand_playing"), this.existingFileHelper)
                        .parent(new ModelFile.ExistingModelFile(Etched.etchedPath("item/boombox_in_hand_playing"), this.existingFileHelper)))
                .perspective(ItemDisplayContext.GUI, boomboxInventory)
                .perspective(ItemDisplayContext.GROUND, boomboxInventory)
                .perspective(ItemDisplayContext.FIXED, boomboxInventory)
                .end();
        this.getBuilder(EtchedItems.BOOMBOX)
                .parent(new ModelFile.ExistingModelFile(Etched.etchedPath("item/boombox_in_hand"), this.existingFileHelper))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(new ItemModelBuilder(Etched.etchedPath("boombox_in_hand"), this.existingFileHelper)
                        .parent(new ModelFile.ExistingModelFile(Etched.etchedPath("item/boombox_in_hand"), this.existingFileHelper)))
                .perspective(ItemDisplayContext.GUI, boomboxInventory)
                .perspective(ItemDisplayContext.GROUND, boomboxInventory)
                .perspective(ItemDisplayContext.FIXED, boomboxInventory)
                .end()
                .override()
                .model(boomboxPlaying)
                .predicate(Etched.etchedPath("playing"), 1.0F);

        this.simpleBlockItem(EtchedBlocks.ETCHING_TABLE.get());
        this.simpleBlockItem(EtchedBlocks.ALBUM_JUKEBOX.get());
        this.basicItem(EtchedBlocks.RADIO);
        this.basicItem(EtchedBlocks.PORTAL_RADIO_ITEM);
    }
}
