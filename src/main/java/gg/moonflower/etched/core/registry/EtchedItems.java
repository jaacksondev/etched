package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.component.MusicLabelComponent;
import gg.moonflower.etched.common.item.*;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EtchedItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Etched.MOD_ID);

    public static final DeferredItem<MusicLabelItem> MUSIC_LABEL = REGISTRY.register("music_label", () -> new MusicLabelItem(new Item.Properties().component(EtchedComponents.MUSIC_LABEL, MusicLabelComponent.EMPTY)));
    public static final DeferredItem<Item> BLANK_MUSIC_DISC = REGISTRY.register("blank_music_disc", () -> new Item(new Item.Properties().component(DataComponents.DYED_COLOR, new DyedItemColor(0x515151, false))));
    public static final DeferredItem<EtchedMusicDiscItem> ETCHED_MUSIC_DISC = REGISTRY.register("etched_music_disc", () -> new EtchedMusicDiscItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<MinecartJukeboxItem> JUKEBOX_MINECART = REGISTRY.register("jukebox_minecart", () -> new MinecartJukeboxItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BoomboxItem> BOOMBOX = REGISTRY.register("boombox", () -> new BoomboxItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<AlbumCoverItem> ALBUM_COVER = REGISTRY.register("album_cover", () -> new AlbumCoverItem(new Item.Properties().stacksTo(1)));

}
