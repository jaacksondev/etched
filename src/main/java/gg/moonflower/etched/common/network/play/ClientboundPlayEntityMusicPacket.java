package gg.moonflower.etched.common.network.play;

import gg.moonflower.etched.core.Etched;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author Ocelot
 */
@ApiStatus.Internal
public class ClientboundPlayEntityMusicPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundPlayEntityMusicPacket> TYPE = new CustomPacketPayload.Type<>(Etched.etchedPath("play_entity_music"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayEntityMusicPacket> CODEC = StreamCodec.of((buffer, value) -> value.writePacketData(buffer), ClientboundPlayEntityMusicPacket::new);

    private final Action action;
    private final ItemStack record;
    private final int entityId;

    public ClientboundPlayEntityMusicPacket(ItemStack record, Entity entity, boolean restart) {
        this.action = restart ? Action.RESTART : Action.START;
        this.record = record;
        this.entityId = entity.getId();
    }

    public ClientboundPlayEntityMusicPacket(Entity entity) {
        this.action = Action.STOP;
        this.record = ItemStack.EMPTY;
        this.entityId = entity.getId();
    }

    private ClientboundPlayEntityMusicPacket(RegistryFriendlyByteBuf buf) {
        this.action = buf.readEnum(Action.class);
        this.record = this.action == Action.STOP ? ItemStack.EMPTY : ItemStack.STREAM_CODEC.decode(buf);
        this.entityId = buf.readVarInt();
    }

    private void writePacketData(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(this.action);
        if (this.action != Action.STOP) {
            ItemStack.STREAM_CODEC.encode(buf, this.record);
        }
        buf.writeVarInt(this.entityId);
    }

//    @Override
//    public void processPacket(NetworkEvent.Context ctx) {
//        EtchedClientPlayPacketHandler.handlePlayEntityMusicPacket(this, ctx);
//    }

    /**
     * @return The action to be performed on the client
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * @return The id of the record item
     */
    public ItemStack getRecord() {
        return this.record;
    }

    /**
     * @return The id of the minecart entity
     */
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * @author Ocelot
     */
    public enum Action {
        START, STOP, RESTART
    }
}
