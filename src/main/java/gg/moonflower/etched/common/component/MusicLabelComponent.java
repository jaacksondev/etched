package gg.moonflower.etched.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.etched.core.Etched;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Locale;
import java.util.function.Consumer;

public record MusicLabelComponent(String artist, String title, int primaryColor,
                                  int secondaryColor) implements TooltipProvider {

    public static final MusicLabelComponent EMPTY = new MusicLabelComponent("", "Custom Music", -1, -1);

    private static final Codec<MusicLabelComponent> COMPLEX_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("author").forGetter(MusicLabelComponent::artist),
            Codec.STRING.fieldOf("title").forGetter(MusicLabelComponent::title),
            Codec.INT.fieldOf("primaryColor").forGetter(MusicLabelComponent::primaryColor),
            Codec.INT.fieldOf("secondaryColor").forGetter(MusicLabelComponent::secondaryColor)
    ).apply(instance, MusicLabelComponent::new));
    private static final Codec<MusicLabelComponent> SIMPLE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("author").forGetter(MusicLabelComponent::artist),
            Codec.STRING.fieldOf("title").forGetter(MusicLabelComponent::title),
            Codec.INT.fieldOf("color").forGetter(MusicLabelComponent::primaryColor)
    ).apply(instance, MusicLabelComponent::new));
    public static final Codec<MusicLabelComponent> CODEC = Codec.withAlternative(SIMPLE_CODEC, COMPLEX_CODEC);
    public static final StreamCodec<FriendlyByteBuf, MusicLabelComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MusicLabelComponent::artist,
            ByteBufCodecs.STRING_UTF8,
            MusicLabelComponent::title,
            ByteBufCodecs.INT,
            MusicLabelComponent::primaryColor,
            ByteBufCodecs.INT,
            MusicLabelComponent::secondaryColor,
            MusicLabelComponent::new);

    public MusicLabelComponent(String artist, String title, int primaryColor, int secondaryColor) {
        this.artist = artist;
        this.title = title;
        this.primaryColor = 0xFFFFFF & primaryColor;
        this.secondaryColor = 0xFFFFFF & secondaryColor;
    }

    public MusicLabelComponent(String author, String title, int color) {
        this(author, title, color, color);
    }

    public boolean simple() {
        return this.primaryColor == this.secondaryColor;
    }

    public boolean isColored() {
        return this.primaryColor != 0xFFFFFF || this.secondaryColor != 0xFFFFFF;
    }

    public MusicLabelComponent withAuthor(String author) {
        return new MusicLabelComponent(author, this.title, this.primaryColor, this.secondaryColor);
    }

    public MusicLabelComponent withTitle(String title) {
        return new MusicLabelComponent(this.artist, title, this.primaryColor, this.secondaryColor);
    }

    public MusicLabelComponent withColor(int color) {
        return new MusicLabelComponent(this.artist, this.title, color, color);
    }

    public MusicLabelComponent withColor(int primaryColor, int secondaryColor) {
        return new MusicLabelComponent(this.artist, this.title, primaryColor, secondaryColor);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (!this.artist.isEmpty() && !this.title.isEmpty()) {
            tooltipAdder.accept(Component.translatable("sound_source." + Etched.MOD_ID + ".info", this.artist, this.title).withStyle(ChatFormatting.GRAY));
        }
        if (this.primaryColor != 0xFFFFFF || this.secondaryColor != 0xFFFFFF) {
            if (tooltipFlag.isAdvanced()) {
                if (this.simple()) {
                    tooltipAdder.accept(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.primaryColor)).withStyle(ChatFormatting.GRAY));
                } else {
                    tooltipAdder.accept(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.primaryColor)).withStyle(ChatFormatting.GRAY));
                    tooltipAdder.accept(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.secondaryColor)).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltipAdder.accept(Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }
}
