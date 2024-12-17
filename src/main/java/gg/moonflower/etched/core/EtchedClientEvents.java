package gg.moonflower.etched.core;

import gg.moonflower.etched.api.record.PlayableRecord;
import gg.moonflower.etched.api.sound.download.SoundSourceManager;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;

import java.util.function.Consumer;

@EventBusSubscriber(value = Dist.CLIENT, modid = Etched.MOD_ID)
public class EtchedClientEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemTooltip(AddAttributeTooltipsEvent event) {
        AttributeTooltipContext context = event.getContext();
        ItemStack stack = event.getStack();

        stack.addToTooltip(EtchedComponents.ALBUM_COVER, context, event::addTooltipLines, context.flag());
        stack.addToTooltip(EtchedComponents.MUSIC_LABEL, context, event::addTooltipLines, context.flag());
        PlayableRecord.addToTooltip(stack, context, event::addTooltipLines);
    }
}
