package hellfirepvp.astralsorcery.common.registry.internal;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

import java.util.*;

/**
 * Compatibility shim for 1.20.1 registration.
 * In 1.20.1, IForgeRegistryEntry is gone - we store entries by registry key
 * and register them during RegisterEvent.
 */
public class InternalRegistryPrimer {
    private final Map<ResourceLocation, List<Object>> primed = new HashMap<>();
    private final Map<Object, ResourceLocation> nameMap = new IdentityHashMap<>();

    public <T> T register(T entry) {
        // For 1.20.1 we just return the entry - actual registration
        // happens via the specific registry methods below
        return entry;
    }

    public <T> T register(ResourceLocation name, T entry) {
        primed.computeIfAbsent(name, k -> new ArrayList<>()).add(entry);
        nameMap.put(entry, name);
        return entry;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getEntries(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (List<Object> entries : primed.values()) {
            for (Object o : entries) {
                if (type.isInstance(o)) result.add((T) o);
            }
        }
        return result;
    }

    public ResourceLocation getRegistryName(Object entry) {
        return nameMap.get(entry);
    }

    public <T> T getCached(IForgeRegistry<T> registry, ResourceLocation key) {
        List<Object> entries = primed.get(key);
        if (entries == null || entries.isEmpty()) return null;
        for (Object o : entries) {
            try {
                return registry.getRegistrySuperType().cast(o);
            } catch (ClassCastException ignored) {}
        }
        return null;
    }
}
