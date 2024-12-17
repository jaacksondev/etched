package gg.moonflower.etched.core.mixin.jukebox;

import gg.moonflower.etched.api.record.PlayableRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxPlayable.class)
public class JukeboxPlayableMixin {

    @Inject(method = "tryInsertIntoJukebox", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void tryInsertIntoJukebox(Level level, BlockPos pos, ItemStack stack, Player player, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!PlayableRecord.isPlayableRecord(stack)) {
            return;
        }

        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.is(Blocks.JUKEBOX) && !blockstate.getValue(JukeboxBlock.HAS_RECORD)) {
            if (!level.isClientSide()) {
                ItemStack record = stack.consumeAndReturn(1, player);
                if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                    jukebox.setTheItem(record);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
                }

                player.awardStat(Stats.PLAY_RECORD);
            }

            cir.setReturnValue(ItemInteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}
