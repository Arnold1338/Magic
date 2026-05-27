package hellfirepvp.astralsorcery.common.data.sync.base;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;

public abstract class ClientData<C extends ClientData<C>>
{
    public abstract void clear(final ResourceKey<Level> p0);
    
    public abstract void clearClient();
}
