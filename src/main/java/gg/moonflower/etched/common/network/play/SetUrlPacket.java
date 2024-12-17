package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.core.Etched;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * @param url The URL to set in the etching table
 * @author Jackson, Ocelot
 */
@ApiStatus.Internal
public record SetUrlPacket(String url) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SetUrlPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("set_url"));
    public static final StreamCodec<FriendlyByteBuf, SetUrlPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SetUrlPacket::url,
            SetUrlPacket::new);

    public SetUrlPacket(@Nullable String url) {
        this.url = url != null ? url : "";
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
