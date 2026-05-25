package hellfirepvp.astralsorcery.common.data.sync.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractData
{
    private final ResourceLocation key;
    
    protected AbstractData(final ResourceLocation key) {
        this.key = key;
    }
    
    public final void markDirty() {
        SyncDataHolder.markForUpdate(this.key);
    }
    
    public abstract void clear(final RegistryKey<Level> p0);
    
    public abstract void clearServer();
    
    public abstract void writeAllDataToPacket(final CompoundTag p0);
    
    public abstract void writeDiffDataToPacket(final CompoundTag p0);
}
