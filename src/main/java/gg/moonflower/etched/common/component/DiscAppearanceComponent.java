package gg.moonflower.etched.common.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.etched.core.Etched;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public record DiscAppearanceComponent(LabelPattern pattern, int discColor, int labelPrimaryColor,
                                      int labelSecondaryColor) {

    public static final DiscAppearanceComponent DEFAULT = new DiscAppearanceComponent(LabelPattern.FLAT, 0x515151, 0xFFFFFF, 0xFFFFFF);

    public static final Codec<DiscAppearanceComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LabelPattern.CODEC.fieldOf("pattern").forGetter(DiscAppearanceComponent::pattern),
            Codec.INT.fieldOf("discColor").forGetter(DiscAppearanceComponent::discColor),
            Codec.INT.fieldOf("labelPrimaryColor").forGetter(DiscAppearanceComponent::labelPrimaryColor),
            Codec.INT.fieldOf("labelSecondaryColor").forGetter(DiscAppearanceComponent::labelSecondaryColor)
    ).apply(instance, DiscAppearanceComponent::new));
    public static final StreamCodec<FriendlyByteBuf, DiscAppearanceComponent> STREAM_CODEC = StreamCodec.of((buffer, value) -> {
        buffer.writeEnum(value.pattern);
        buffer.writeInt(value.discColor);
        if (value.pattern.isColorable()) {
            buffer.writeInt(value.labelPrimaryColor);
            if (value.pattern.isComplex()) {
                buffer.writeInt(value.labelSecondaryColor);
            }
        }
    }, buffer -> {
        LabelPattern pattern = buffer.readEnum(LabelPattern.class);
        int discColor = buffer.readInt();
        int labelPrimaryColor = -1;
        int labelSecondaryColor = -1;

        if (pattern.isColorable()) {
            labelPrimaryColor = buffer.readInt();
            if (pattern.isComplex()) {
                labelSecondaryColor = buffer.readInt();
            }
        }

        return new DiscAppearanceComponent(pattern, discColor, labelPrimaryColor, labelSecondaryColor);
    });

    public DiscAppearanceComponent(LabelPattern pattern, int discColor, int labelPrimaryColor, int labelSecondaryColor) {
        this.pattern = pattern;
        this.discColor = 0xFFFFFF & discColor;
        this.labelPrimaryColor = 0xFFFFFF & labelPrimaryColor;
        this.labelSecondaryColor = 0xFFFFFF & labelSecondaryColor;
    }

    @Override
    public int discColor() {
        return 0xFF000000 | this.discColor;
    }

    @Override
    public int labelPrimaryColor() {
        return 0xFF000000 | this.labelPrimaryColor;
    }

    @Override
    public int labelSecondaryColor() {
        return 0xFF000000 | this.labelSecondaryColor;
    }

    /**
     * @author Jackson
     */
    public enum LabelPattern {

        FLAT, CROSS, EYE, PARALLEL, STAR, GOLD(true);

        public static final Codec<LabelPattern> CODEC = Codec.STRING.flatXmap(name -> {
            for (LabelPattern value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return DataResult.success(value);
                }
            }
            return DataResult.error(() -> "Unknown Label Pattern: " + name.toLowerCase(Locale.ROOT));
        }, pattern -> DataResult.success(pattern.name().toLowerCase(Locale.ROOT)));

        private final boolean simple;
        private final Pair<ResourceLocation, ResourceLocation> textures;

        LabelPattern() {
            this(false);
        }

        LabelPattern(boolean simple) {
            this.simple = simple;

            String name = this.name().toLowerCase(Locale.ROOT);
            this.textures = Pair.of(
                    Etched.etchedPath("textures/item/" + name + "_label" + (simple ? "" : "_top") + ".png"),
                    Etched.etchedPath("textures/item/" + name + "_label" + (simple ? "" : "_bottom") + ".png")
            );
        }

        /**
         * @return A pair of {@link ResourceLocation} for a top and bottom texture. If the pattern is simple, both locations are the same.
         */
        public Pair<ResourceLocation, ResourceLocation> getTextures() {
            return this.textures;
        }

        /**
         * @return Whether the label pattern supports two colors.
         */
        public boolean isComplex() {
            return !this.simple;
        }

        /**
         * @return Whether this label can be colored
         */
        public boolean isColorable() {
            return this != GOLD;
        }
    }
}
