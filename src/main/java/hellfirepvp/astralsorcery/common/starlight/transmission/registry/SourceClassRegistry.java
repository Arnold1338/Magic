package hellfirepvp.astralsorcery.common.starlight.transmission.registry;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import java.util.HashMap;
import net.minecraftforge.eventbus.api.Event;
import hellfirepvp.astralsorcery.common.event.StarlightNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.IndependentCrystalSource;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class SourceClassRegistry
{
    public static final SourceClassRegistry eventInstance;
    private static final Map<ResourceLocation, SourceProvider> providerMap;
    
    private SourceClassRegistry() {
    }
    
    public void registerProvider(final SourceProvider provider) {
        register(provider);
    }
    
    @Nullable
    public static SourceProvider getProvider(final ResourceLocation identifier) {
        return SourceClassRegistry.providerMap.get(identifier);
    }
    
    public static void register(final SourceProvider provider) {
        if (SourceClassRegistry.providerMap.containsKey(provider.getIdentifier())) {
            throw new IllegalArgumentException("Already registered identifier SourceProvider: " + provider.getIdentifier());
        }
        SourceClassRegistry.providerMap.put(provider.getIdentifier(), provider);
    }
    
    public static void setupRegistry() {
        register(new IndependentCrystalSource.Provider());
        MinecraftForge.EVENT_BUS.post((Event)new StarlightNetworkEvent.SourceProviderRegistry(SourceClassRegistry.eventInstance));
    }
    
    static {
        eventInstance = new SourceClassRegistry();
        providerMap = new HashMap<ResourceLocation, SourceProvider>();
    }
    
    public interface SourceProvider
    {
        IIndependentStarlightSource provideEmptySource();
        
        @Nonnull
        ResourceLocation getIdentifier();
    }
}
