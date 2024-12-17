package gg.moonflower.etched.core.mixin.client.render;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {

    @Accessor
    Map<BlockPos, SoundInstance> getPlayingJukeboxSongs();
}
