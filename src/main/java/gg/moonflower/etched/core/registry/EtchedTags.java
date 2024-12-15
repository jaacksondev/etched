package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.core.Etched;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EtchedTags {

    public static final TagKey<Block> AUDIO_PROVIDER = BlockTags.create(Etched.etchedPath("audio_providers"));
    public static final TagKey<Block> MUSIC_DISCS = BlockTags.create(Etched.etchedPath("music_discs"));
}
