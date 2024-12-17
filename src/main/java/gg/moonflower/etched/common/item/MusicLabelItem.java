package gg.moonflower.etched.common.item;

import gg.moonflower.etched.client.screen.EditMusicLabelScreen;
import gg.moonflower.etched.common.component.MusicLabelComponent;
import gg.moonflower.etched.core.registry.EtchedComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MusicLabelItem extends Item {

    public MusicLabelItem(Properties properties) {
        super(properties);
    }

    private void openMusicLabelEditScreen(Player player, InteractionHand hand, ItemStack stack) {
        Minecraft.getInstance().setScreen(new EditMusicLabelScreen(player, hand, stack));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            this.openMusicLabelEditScreen(player, hand, stack);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) {
            MusicLabelComponent label = stack.getOrDefault(EtchedComponents.MUSIC_LABEL, MusicLabelComponent.DEFAULT);
            if (label.artist().isEmpty()) {
                stack.set(EtchedComponents.MUSIC_LABEL, label.withArtist(entity.getDisplayName().getString()));
            }
        }
    }

//    @Override
//    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
//        if (!getAuthor(itemStack).isEmpty() && !getTitle(itemStack).isEmpty()) {
//            list.add(Component.translatable("sound_source." + Etched.MOD_ID + ".info", getAuthor(itemStack), getTitle(itemStack)).withStyle(ChatFormatting.GRAY));
//        }
//    }
}
