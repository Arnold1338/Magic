package hellfirepvp.astralsorcery.common.data.sync;

import java.util.HashMap;
import javax.annotation.Nullable;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class SyncDataRegistry
{
    private static final Map<ResourceLocation, AbstractDataProvider<?, ?>> REGISTRY;
    
    private SyncDataRegistry() {
    }
    
    static void register(final AbstractDataProvider<?, ?> provider) {
        SyncDataRegistry.REGISTRY.put(provider.getKey(), provider);
    }
    
    public static Collection<ResourceLocation> getKnownKeys() {
        return SyncDataRegistry.REGISTRY.keySet();
    }
    
    @Nullable
    public static AbstractDataProvider<?, ?> getProvider(final ResourceLocation key) {
        return SyncDataRegistry.REGISTRY.get(key);
    }
    
    static {
        REGISTRY = new HashMap<ResourceLocation, AbstractDataProvider<?, ?>>();
    }
}
