package gg.moonflower.etched.common.component;

import com.mojang.serialization.Codec;
import gg.moonflower.etched.core.Etched;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public enum PausedComponent implements TooltipProvider {

    INSTANCE;

    public static final Codec<PausedComponent> CODEC = Codec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, PausedComponent> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    private static final Component TOOLTIP = Component.translatable("item." + Etched.MOD_ID + ".boombox.paused").withStyle(ChatFormatting.YELLOW);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        tooltipAdder.accept(TOOLTIP);
    }
}
