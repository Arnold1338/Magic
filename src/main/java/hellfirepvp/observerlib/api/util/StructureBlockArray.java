package hellfirepvp.observerlib.api.util;

import hellfirepvp.observerlib.api.structure.PlaceableStructure;
import net.minecraft.resources.ResourceLocation;

public class StructureBlockArray extends PatternBlockArray implements PlaceableStructure {
    public StructureBlockArray(ResourceLocation registryName) {
        super(registryName);
    }
}
