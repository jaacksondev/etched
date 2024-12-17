package gg.moonflower.etched.core.mixin.jukebox;

import net.minecraft.world.item.JukeboxSongPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JukeboxSongPlayer.class)
public interface JukeboxSongPlayerAccessor {

    @Accessor
    void setTicksSinceSongStarted(long ticks);
}
