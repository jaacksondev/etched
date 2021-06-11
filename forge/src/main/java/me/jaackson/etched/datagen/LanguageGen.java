package me.jaackson.etched.datagen;

import me.jaackson.etched.Etched;
import me.jaackson.etched.EtchedRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageGen extends LanguageProvider {

    public LanguageGen(DataGenerator gen) {
        super(gen, Etched.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(EtchedRegistry.ETCHER, "Etcher");
        this.addBlock(EtchedRegistry.ALBUM_JUKEBOX, "Album Jukebox");
        this.addItem(EtchedRegistry.BLANK_MUSIC_DISC, "Blank Music Disc");
        this.addItem(EtchedRegistry.ETCHED_MUSIC_DISC, "Etched Music Disc");
        this.addItem(EtchedRegistry.MUSIC_LABEL, "Music Label");
        this.add("container." + Etched.MOD_ID + ".album_jukebox", "Album Jukebox");
        this.add("record." + Etched.MOD_ID + ".downloadProgress", "Downloading (%s MB / %s MB): %s");
        this.add("record." + Etched.MOD_ID + ".downloadFail", "Failed to download %s");
    }
}