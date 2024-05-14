package gg.moonflower.etched.core.mixin.client;

import gg.moonflower.etched.client.GuiHook;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 2000) // Make sure this is applied after other mixins
public class GuiMixin {

    @Inject(method = "setNowPlaying", at = @At("HEAD"), cancellable = true)
    public void setNowPlaying(Component displayName, CallbackInfo ci) {
        if (GuiHook.isHidePlayingText()) {
            ci.cancel();
        }
    }
}
