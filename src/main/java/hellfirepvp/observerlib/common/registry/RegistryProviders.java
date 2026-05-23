package hellfirepvp.observerlib.common.registry;

import hellfirepvp.observerlib.api.ObserverProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RegistryProviders {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("observerlib", "observer_providers");
    private static final Map<ResourceLocation, ObserverProvider> REGISTRY = new LinkedHashMap<>();

    public static void register(ObserverProvider provider) {
        if (provider.getRegistryName() == null) throw new IllegalArgumentException("ObserverProvider has no registry name!");
        REGISTRY.put(provider.getRegistryName(), provider);
    }

    @Nullable
    public static ObserverProvider getProvider(ResourceLocation key) { return REGISTRY.get(key); }

    @Nonnull
    public static Collection<ObserverProvider> getAll() { return REGISTRY.values(); }
}
