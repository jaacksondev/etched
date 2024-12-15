package gg.moonflower.etched.common.menu;

import gg.moonflower.etched.common.blockentity.RadioBlockEntity;
import gg.moonflower.etched.core.registry.EtchedBlocks;
import gg.moonflower.etched.core.registry.EtchedMenus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

/**
 * @author Ocelot
 */
public class RadioMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private String initialUrl;

    public RadioMenu(int id, String url) {
        this(id, ContainerLevelAccess.NULL);
        this.initialUrl = url;
    }

    public RadioMenu(int id, ContainerLevelAccess access) {
        super(EtchedMenus.RADIO_MENU.get(), id);
        this.access = access;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, EtchedBlocks.RADIO.get());
    }

    /**
     * Sets the URL for the resulting stack to the specified value.
     *
     * @param url The new URL
     */
    public void setUrl(String url) {
        this.access.execute((level, pos) -> {
            if (level.getBlockEntity(pos) instanceof RadioBlockEntity be) {
                be.setUrl(url);
            }
        });
    }

    /**
     * @return The original URL inside the radio before opening the menu
     */
    public String getInitialUrl() {
        return this.initialUrl;
    }
}