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

public record EtchedMusicComponent(List<TrackData> tracks) {

    public static final Codec<EtchedMusicComponent> CODEC = TrackData.CODEC
            .orElse(TrackData.EMPTY)
            .listOf()
            .flatXmap(tracks -> {
                List<TrackData> validTracks = new ArrayList<>(tracks.size());
                for (TrackData track : tracks) {
                    if (track.isValid()) {
                        validTracks.add(track);
                    }
                }
                return !validTracks.isEmpty() ? DataResult.success(new EtchedMusicComponent(validTracks)) : DataResult.error(() -> "At least 1 valid track is required");
            }, component -> !component.tracks.isEmpty() ? DataResult.success(component.tracks) : DataResult.error(() -> "At least 1 valid track is required"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EtchedMusicComponent> STREAM_CODEC = TrackData.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(EtchedMusicComponent::new, EtchedMusicComponent::tracks);

    public EtchedMusicComponent(List<TrackData> tracks) {
        this.tracks = Collections.unmodifiableList(tracks);
    }

    public EtchedMusicComponent() {
        this(Collections.emptyList());
    }
}
