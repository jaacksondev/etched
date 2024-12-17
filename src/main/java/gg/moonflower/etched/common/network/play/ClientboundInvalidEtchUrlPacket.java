package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.core.Etched;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

/**
 * @param exception The exception to set in the etching table
 * @author Jackson, Ocelot
 */
@ApiStatus.Internal
public record ClientboundInvalidEtchUrlPacket(String exception) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundInvalidEtchUrlPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("invalid_etch_url"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundInvalidEtchUrlPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ClientboundInvalidEtchUrlPacket::exception,
            ClientboundInvalidEtchUrlPacket::new);

//    @Override
//    public void processPacket(NetworkEvent.Context ctx) {
//        EtchedClientPlayPacketHandler.handleSetInvalidEtch(this, ctx);
//    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
