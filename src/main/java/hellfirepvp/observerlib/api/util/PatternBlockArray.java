package hellfirepvp.observerlib.api.util;

import hellfirepvp.observerlib.api.structure.MatchableStructure;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class PatternBlockArray extends BlockArray implements MatchableStructure {
    private final ResourceLocation registryName;

    public PatternBlockArray(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Nullable
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }
}
