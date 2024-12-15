package gg.moonflower.etched.common.item;

import gg.moonflower.etched.common.entity.MinecartJukebox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

/**
 * @author Ocelot
 */
public class MinecartJukeboxItem extends Item {

    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            ServerLevel serverlevel = source.level();
            Vec3 vec3 = source.center();
            double d0 = vec3.x() + (double) direction.getStepX() * 1.125;
            double d1 = Math.floor(vec3.y()) + (double) direction.getStepY();
            double d2 = vec3.z() + (double) direction.getStepZ() * 1.125;
            BlockPos blockpos = source.pos().relative(direction);
            BlockState blockstate = serverlevel.getBlockState(blockpos);
            RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock railBlock
                    ? railBlock.getRailDirection(blockstate, serverlevel, blockpos, null)
                    : RailShape.NORTH_SOUTH;
            double d3;
            if (blockstate.is(BlockTags.RAILS)) {
                if (railshape.isAscending()) {
                    d3 = 0.6;
                } else {
                    d3 = 0.1;
                }
            } else {
                if (!blockstate.isAir() || !serverlevel.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }

                BlockState blockstate1 = serverlevel.getBlockState(blockpos.below());
                RailShape railshape1 = blockstate1.getBlock() instanceof BaseRailBlock railBlock
                        ? railBlock.getRailDirection(blockstate1, serverlevel, blockpos.below(), null)
                        : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railshape1.isAscending()) {
                    d3 = -0.4;
                } else {
                    d3 = -0.9;
                }
            }

            MinecartJukebox jukeboxMinecart = new MinecartJukebox(serverlevel, d0, d1 + d3, d2);
            serverlevel.addFreshEntity(jukeboxMinecart);
            stack.shrink(1);
            return stack;
        }

        @Override
        protected void playSound(BlockSource source) {
            source.level().levelEvent(1000, source.pos(), 0);
        }
    };

    public MinecartJukeboxItem(Item.Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(pos);
        if (!blockstate.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        }

        ItemStack itemstack = context.getItemInHand();
        if (level instanceof ServerLevel serverlevel) {
            RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock railBlock
                    ? railBlock.getRailDirection(blockstate, level, pos, null)
                    : RailShape.NORTH_SOUTH;
            double d0 = 0.0;
            if (railshape.isAscending()) {
                d0 = 0.5;
            }

            MinecartJukebox jukeboxMinecart = new MinecartJukebox(level, pos.getX() + 0.5D, pos.getY() + 0.0625D + d0, pos.getZ() + 0.5D);
            serverlevel.addFreshEntity(jukeboxMinecart);
            serverlevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(context.getPlayer(), serverlevel.getBlockState(pos.below())));
        }

        itemstack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
