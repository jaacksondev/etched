package gg.moonflower.etched.client;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GuiHook {

    private static boolean hidePlayingText = false;

    public static void setHidePlayingText(boolean hidePlayingText) {
        GuiHook.hidePlayingText = hidePlayingText;
    }

    public static boolean isHidePlayingText() {
        return hidePlayingText;
    }
}
