package hellfirepvp.astralsorcery.common.data.sync.base;

import net.minecraft.nbt.CompoundTag;

public abstract class ClientDataReader<C extends ClientData<C>>
{
    public abstract void readFromIncomingFullSync(final C p0, final CompoundTag p1);
    
    public abstract void readFromIncomingDiff(final C p0, final CompoundTag p1);
}
