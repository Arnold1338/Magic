package hellfirepvp.astralsorcery.common.registry.internal;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.HashMap;
import net.minecraftforge.registries.IForgeRegistryEntry;
import java.util.List;
import java.util.Map;

public class InternalRegistryPrimer
{
    private final Map<Class<?>, List<IForgeRegistryEntry<?>>> primed;
    
    public InternalRegistryPrimer() {
        this.primed = new HashMap<Class<?>, List<IForgeRegistryEntry<?>>>();
    }
    
    public <V extends IForgeRegistryEntry<V>> V register(final V entry) {
        final Class<V> type = entry.getRegistryType();
        final List<IForgeRegistryEntry<?>> entries = this.primed.computeIfAbsent(type, k -> Lists.newLinkedList());
        entries.add(entry);
        return entry;
    }
    
     <T extends IForgeRegistryEntry<T>> List<?> getEntries(final Class<T> type) {
        return this.primed.getOrDefault(type, Collections.emptyList());
    }
    
    @Nullable
    public <V extends IForgeRegistryEntry<V>> V getCached(final IForgeRegistry<V> registry, final ResourceLocation key) {
        return MiscUtils.iterativeSearch((Collection<V>)this.primed.getOrDefault(registry.getRegistrySuperType(), Collections.emptyList()), entry -> entry.getRegistryName().equals((Object)key));
    }
}
