package hellfirepvp.astralsorcery.common.data.sync.base;

import net.minecraft.resources.ResourceLocation;

public abstract class AbstractDataProvider<T extends AbstractData, C extends ClientData<C>>
{
    private ResourceLocation key;
    
    public AbstractDataProvider(final ResourceLocation key) {
        this.key = key;
    }
    
    public abstract T provideServerData();
    
    public abstract C provideClientData();
    
    public abstract ClientDataReader<C> createReader();
    
    public final ResourceLocation getKey() {
        return this.key;
    }
}
