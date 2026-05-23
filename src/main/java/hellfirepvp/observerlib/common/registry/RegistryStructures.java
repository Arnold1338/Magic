package hellfirepvp.observerlib.common.registry;

import hellfirepvp.observerlib.api.structure.MatchableStructure;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RegistryStructures {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("observerlib", "matchable_structures");
    private static final Map<ResourceLocation, MatchableStructure> REGISTRY = new LinkedHashMap<>();

    public static void register(MatchableStructure structure) {
        ResourceLocation name = structure.getRegistryName();
        if (name == null) throw new IllegalArgumentException("MatchableStructure has no registry name!");
        REGISTRY.put(name, structure);
    }

    @Nullable
    public static MatchableStructure getStructure(ResourceLocation key) { return REGISTRY.get(key); }

    @Nonnull
    public static Collection<MatchableStructure> getAll() { return REGISTRY.values(); }
}
