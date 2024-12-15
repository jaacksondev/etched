package gg.moonflower.etched.core;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EtchedConfig {

    public static class Client {

        public final ModConfigSpec.BooleanValue forceStereo;

        public Client(ModConfigSpec.Builder builder) {
            builder.push("Game Feel");
            this.forceStereo = builder.comment("Always plays tracks in stereo even when in-world").define("Force Stereo", false);
            builder.pop();
        }
    }

    public static class Server {

        public final ModConfigSpec.BooleanValue useBoomboxMenu;
        public final ModConfigSpec.BooleanValue useAlbumCoverMenu;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("Boombox");
            this.useBoomboxMenu = builder.comment("Disables right clicking music discs into boomboxes and allows the menu to be used by shift right-clicking").define("Use boombox menu", false);
            builder.pop();

            builder.push("Album Cover");
            this.useAlbumCoverMenu = builder.comment("Disables right clicking music discs into album covers and allows the menu to be used by shift right-clicking").define("Use album cover menu", false);
            builder.pop();
        }
    }
}
