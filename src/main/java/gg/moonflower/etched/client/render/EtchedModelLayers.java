package gg.moonflower.etched.client.render;

import gg.moonflower.etched.core.Etched;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class EtchedModelLayers {

    public static final ModelLayerLocation JUKEBOX_MINECART = create("jukebox_minecart");

    public static ModelLayerLocation create(String model) {
        return create(model, "main");
    }

    public static ModelLayerLocation create(String model, String layer) {
        return new ModelLayerLocation(Etched.etchedPath(model), layer);
    }
}
