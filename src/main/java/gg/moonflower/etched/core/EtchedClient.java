package gg.moonflower.etched.core;

import gg.moonflower.etched.client.render.EtchedModelLayers;
import gg.moonflower.etched.client.render.JukeboxMinecartRenderer;
import gg.moonflower.etched.client.render.item.AlbumCoverItemRenderer;
import gg.moonflower.etched.client.screen.*;
import gg.moonflower.etched.common.component.DiscAppearanceComponent;
import gg.moonflower.etched.common.component.MusicLabelComponent;
import gg.moonflower.etched.common.item.BoomboxItem;
import gg.moonflower.etched.core.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(value = Etched.MOD_ID, dist = Dist.CLIENT)
public class EtchedClient {

    public EtchedClient(IEventBus bus) {
        bus.addListener(this::clientInit);
        bus.addListener(this::registerScreens);
        bus.addListener(this::registerClientExtensions);
        bus.addListener(this::registerReloadListeners);
        bus.addListener(this::registerItemGroups);
        bus.addListener(this::registerCustomModels);
        bus.addListener(this::registerEntityRenders);
        bus.addListener(this::registerEntityLayers);
        bus.addListener(this::registerItemColors);
    }

    private void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(EtchedItems.BOOMBOX.get(), Etched.etchedPath("playing"), (stack, level, entity, i) -> {
                InteractionHand hand = entity != null ? BoomboxItem.getPlayingHand(entity) : null;
                return hand != null && stack == entity.getItemInHand(hand) ? 1 : 0;
            });
            ItemProperties.register(EtchedItems.ETCHED_MUSIC_DISC.get(), Etched.etchedPath("pattern"), (stack, level, entity, i) -> {
                DiscAppearanceComponent discAppearance = stack.get(EtchedComponents.DISC_APPEARANCE);
                if (discAppearance != null) {
                    return discAppearance.pattern().ordinal() / 10.0F;
                }
                return 0.0F;
            });
        });
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(EtchedMenus.ETCHING_MENU.get(), EtchingScreen::new);
        event.register(EtchedMenus.ALBUM_JUKEBOX_MENU.get(), AlbumJukeboxScreen::new);
        event.register(EtchedMenus.BOOMBOX_MENU.get(), BoomboxScreen::new);
        event.register(EtchedMenus.ALBUM_COVER_MENU.get(), AlbumCoverScreen::new);
        event.register(EtchedMenus.RADIO_MENU.get(), RadioScreen::new);
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AlbumCoverItemRenderer.INSTANCE;
            }
        }, EtchedItems.ALBUM_COVER.asItem());
    }

    private void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(AlbumCoverItemRenderer.INSTANCE);
    }

    private void registerItemGroups(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tab = event.getTabKey();
        if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EtchedItems.MUSIC_LABEL);
            event.accept(EtchedItems.BLANK_MUSIC_DISC);
            event.accept(EtchedItems.BOOMBOX);
            event.accept(EtchedItems.ALBUM_COVER);
        } else if (tab == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(EtchedItems.JUKEBOX_MINECART);
        } else if (tab == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(EtchedBlocks.ETCHING_TABLE);
            event.accept(EtchedBlocks.ALBUM_JUKEBOX);
            event.accept(EtchedBlocks.RADIO);
        }
    }

    private void registerCustomModels(ModelEvent.RegisterAdditional event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String folder = "models/" + AlbumCoverItemRenderer.FOLDER_NAME;
        event.register(new ModelResourceLocation(Etched.etchedPath("item/boombox_in_hand"), "standalone"));
        for (ResourceLocation location : resourceManager.listResources(folder, name -> name.getPath().endsWith(".json")).keySet()) {
            event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath().substring(7, location.getPath().length() - 5)), "standalone"));
        }
        event.register(AlbumCoverItemRenderer.BLANK_ALBUM_COVER);
        event.register(AlbumCoverItemRenderer.DEFAULT_ALBUM_COVER);
    }

    private void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EtchedEntities.JUKEBOX_MINECART.get(), JukeboxMinecartRenderer::new);
    }

    private void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EtchedModelLayers.JUKEBOX_MINECART, MinecartModel::createBodyLayer);
    }

    private void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> index == 0 || index == 1 ? DyedItemColor.getOrDefault(stack, -1) : -1, EtchedItems.MUSIC_LABEL.get());
        event.register((stack, index) -> {
            MusicLabelComponent label = stack.getOrDefault(EtchedComponents.MUSIC_LABEL, MusicLabelComponent.DEFAULT);
            if (index == 0) {
                return label.primaryColor();
            }
            if (index == 1) {
                return label.secondaryColor();
            }
            return -1;
        }, EtchedItems.MUSIC_LABEL.get());

        event.register((stack, index) -> index > 0 ? -1 : 0xFF000000 | DyedItemColor.getOrDefault(stack, 0x515151), EtchedItems.BLANK_MUSIC_DISC.get());
        event.register((stack, index) -> {
            DiscAppearanceComponent discAppearance = stack.getOrDefault(EtchedComponents.DISC_APPEARANCE, DiscAppearanceComponent.DEFAULT);
            if (index == 0) {
                return discAppearance.discColor();
            }
            if (discAppearance.pattern().isColorable()) {
                if (index == 1) {
                    return discAppearance.labelPrimaryColor();
                }
                if (!discAppearance.pattern().isSimple() && index == 2) {
                    return discAppearance.labelSecondaryColor();
                }
            }
            return -1;
        }, EtchedItems.ETCHED_MUSIC_DISC.get());
    }
}
