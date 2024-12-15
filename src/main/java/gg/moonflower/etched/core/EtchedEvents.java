package gg.moonflower.etched.core;

import gg.moonflower.etched.common.item.AlbumCoverItem;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber( modid = Etched.MOD_ID)
public class EtchedEvents {

    @SubscribeEvent
    public static void onGrindstoneChange(GrindstoneEvent.OnPlaceItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (top.isEmpty() == bottom.isEmpty()) {
            return;
        }

        ItemStack stack = top.isEmpty() ? bottom : top;
        if (AlbumCoverItem.getCoverStack(stack).isPresent()) {
            ItemStack result = stack.copy();
            result.setCount(1);
            AlbumCoverItem.setCover(result, ItemStack.EMPTY);
            event.setOutput(result);
        }
    }

    @SubscribeEvent
    public static void onItemChangedDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ItemEntity entity) {
            if (event.getDimension() == Level.NETHER) {
                ItemStack oldStack = entity.getItem();
                if (oldStack.getItem() != EtchedBlocks.RADIO.get().asItem()) {
                    return;
                }

                ItemStack newStack = new ItemStack(EtchedBlocks.PORTAL_RADIO_ITEM.get(), oldStack.getCount());
                newStack.applyComponents(oldStack.getComponents());
                entity.setItem(newStack);
            }
        }
    }
}
