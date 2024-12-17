package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.core.Etched;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EtchedTags {

    public static final TagKey<Block> RECORD_PLAYERS = BlockTags.create(Etched.etchedPath("record_players"));
}
