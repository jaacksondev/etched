package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.core.Etched;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EtchedSounds {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Etched.MOD_ID);

    public static final Supplier<SoundEvent> UI_ETCHER_TAKE_RESULT = registerSound("ui.etching_table.take_result");

    private static Supplier<SoundEvent> registerSound(String id) {
        return REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(Etched.etchedPath(id)));
    }
}
