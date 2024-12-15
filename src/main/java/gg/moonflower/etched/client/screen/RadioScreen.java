package gg.moonflower.etched.client.screen;

import gg.moonflower.etched.common.menu.RadioMenu;
import gg.moonflower.etched.core.Etched;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

/**
 * @author Ocelot
 */
public class RadioScreen extends AbstractContainerScreen<RadioMenu> {

    private static final ResourceLocation TEXTURE = Etched.etchedPath("textures/gui/radio.png");

    private EditBox url;

    public RadioScreen(RadioMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageHeight = 39;
    }

    @Override
    protected void init() {
        super.init();
        String urlText = this.url != null ? this.url.getValue() : this.menu.getInitialUrl();
        this.url = new EditBox(this.font, this.leftPos + 10, this.topPos + 21, 154, 16, null, Component.translatable("container." + Etched.MOD_ID + ".radio.url"));
        this.url.setValue(urlText);
        this.url.setTextColor(-1);
        this.url.setTextColorUneditable(-1);
        this.url.setBordered(false);
        this.url.setMaxLength(32768);
        this.setFocused(this.url);
        this.addRenderableWidget(this.url);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
//            EtchedMessages.PLAY.sendToServer(new ServerboundSetUrlPacket(this.url.getValue()));
            Minecraft.getInstance().player.closeContainer();
        }).bounds((this.width - this.imageWidth) / 2, (this.height - this.imageHeight) / 2 + this.imageHeight + 5, this.imageWidth, 20).build());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 18, 0, 39, 160, 14);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.url.keyPressed(keyCode, scanCode, modifiers) || (this.url.isFocused() && this.url.isVisible() && keyCode != GLFW_KEY_ESCAPE) || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
