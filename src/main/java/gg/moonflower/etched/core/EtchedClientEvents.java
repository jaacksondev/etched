package gg.moonflower.etched.core;

import gg.moonflower.etched.core.registry.EtchedComponents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = Etched.MOD_ID)
public class EtchedClientEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        event.getItemStack().addToTooltip(EtchedComponents.MUSIC_LABEL, event.getContext(), component -> event.getToolTip().add(component), event.getFlags());
    }
}
