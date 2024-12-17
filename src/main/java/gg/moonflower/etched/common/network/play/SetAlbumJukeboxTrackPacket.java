package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.core.Etched;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param playingIndex The playing index to set the jukebox to
 * @param track        The track to set the jukebox to
 * @author Ocelot
 */
@ApiStatus.Internal
public record SetAlbumJukeboxTrackPacket(int playingIndex, int track) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SetAlbumJukeboxTrackPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("set_album_jukebox_track"));
    public static final StreamCodec<FriendlyByteBuf, SetAlbumJukeboxTrackPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SetAlbumJukeboxTrackPacket::playingIndex,
            ByteBufCodecs.VAR_INT,
            SetAlbumJukeboxTrackPacket::track,
            SetAlbumJukeboxTrackPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

//    @Override
//    public void processPacket(NetworkEvent.Context ctx) {
//        switch (ctx.getDirection().getReceptionSide()) {
//            case CLIENT -> EtchedClientPlayPacketHandler.handleSetAlbumJukeboxTrack(this, ctx);
//            case SERVER -> EtchedServerPlayPacketHandler.handleSetAlbumJukeboxTrack(this, ctx);
//        }
//    }
}
