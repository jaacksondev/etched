package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.core.Etched;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param slot   The slot the music label is in
 * @param artist The new author
 * @param title  The new title
 * @author Ocelot
 */
@ApiStatus.Internal
public record ServerboundEditMusicLabelPacket(int slot, String artist, String title) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundEditMusicLabelPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("edit_music_label"));
    public static final StreamCodec<FriendlyByteBuf, ServerboundEditMusicLabelPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundEditMusicLabelPacket::slot,
            ByteBufCodecs.stringUtf8(128),
            ServerboundEditMusicLabelPacket::artist,
            ByteBufCodecs.stringUtf8(128),
            ServerboundEditMusicLabelPacket::title,
            ServerboundEditMusicLabelPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

//    @Override
//    public void processPacket(NetworkEvent.Context ctx) {
//        EtchedServerPlayPacketHandler.handleEditMusicLabel(this, ctx);
//    }
}
