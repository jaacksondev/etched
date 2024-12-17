package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.api.record.PlayableRecord;
import gg.moonflower.etched.api.record.TrackData;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * @param record The record to play
 * @param pos    The position the music disk is playing at
 * @author Ocelot
 */
@ApiStatus.Internal
public record ClientboundPlayBlockMusicPacket(ItemStack record, BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundPlayBlockMusicPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("play_block_music"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayBlockMusicPacket> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ClientboundPlayBlockMusicPacket::record,
            BlockPos.STREAM_CODEC,
            ClientboundPlayBlockMusicPacket::pos,
            ClientboundPlayBlockMusicPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

//    @Override
//    public void processPacket(NetworkEvent.Context ctx) {
//        EtchedClientPlayPacketHandler.handlePlayMusicPacket(this, ctx);
//    }

    /**
     * @param registries The registry instance to get data from
     * @return The tracks to play in sequence
     */
    public List<TrackData> tracks(HolderLookup.Provider registries) {
        return PlayableRecord.getTracks(registries, this.record);
    }
}
