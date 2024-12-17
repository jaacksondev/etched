package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.api.record.TrackData;
import gg.moonflower.etched.common.component.*;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class EtchedComponents {

    public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Etched.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TrackData>> ALBUM = register(
            "album", builder -> builder
                    .persistent(TrackData.CODEC)
                    .networkSynchronized(TrackData.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AlbumCoverComponent>> ALBUM_COVER = register(
            "album_cover", builder -> builder
                    .persistent(AlbumCoverComponent.CODEC)
                    .networkSynchronized(AlbumCoverComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DiscAppearanceComponent>> DISC_APPEARANCE = register(
            "disc_appearance", builder -> builder
                    .persistent(DiscAppearanceComponent.CODEC)
                    .networkSynchronized(DiscAppearanceComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MusicTrackComponent>> MUSIC = register(
            "music", builder -> builder
                    .persistent(MusicTrackComponent.CODEC)
                    .networkSynchronized(MusicTrackComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MusicLabelComponent>> MUSIC_LABEL = register(
            "music_label", builder -> builder
                    .persistent(MusicLabelComponent.CODEC)
                    .networkSynchronized(MusicLabelComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PausedComponent>> PAUSED = register(
            "paused", builder -> builder
                    .persistent(PausedComponent.CODEC)
                    .networkSynchronized(PausedComponent.STREAM_CODEC)
                    .cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlayingRecordComponent>> PLAYING_RECORD = register(
            "playing_record", builder -> builder
                    .persistent(PlayingRecordComponent.CODEC)
                    .networkSynchronized(PlayingRecordComponent.STREAM_CODEC)
                    .cacheEncoding());

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return REGISTRY.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
