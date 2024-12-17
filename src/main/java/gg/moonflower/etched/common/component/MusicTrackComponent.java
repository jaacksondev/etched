package gg.moonflower.etched.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import gg.moonflower.etched.api.record.TrackData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record MusicTrackComponent(List<TrackData> tracks) {

    public static final Codec<MusicTrackComponent> CODEC = TrackData.CODEC
            .orElse(TrackData.EMPTY)
            .listOf()
            .flatXmap(tracks -> {
                List<TrackData> validTracks = new ArrayList<>(tracks.size());
                for (TrackData track : tracks) {
                    if (track.isValid()) {
                        validTracks.add(track);
                    }
                }
                return !validTracks.isEmpty() ? DataResult.success(new MusicTrackComponent(validTracks)) : DataResult.error(() -> "At least 1 valid track is required");
            }, component -> !component.tracks.isEmpty() ? DataResult.success(component.tracks) : DataResult.error(() -> "At least 1 valid track is required"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MusicTrackComponent> STREAM_CODEC = TrackData.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(MusicTrackComponent::new, MusicTrackComponent::tracks);

    public MusicTrackComponent(List<TrackData> tracks) {
        this.tracks = Collections.unmodifiableList(tracks);
    }
}
