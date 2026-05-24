package hellfirepvp.astralsorcery.common.storage;

import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;

public interface IStorageNetworkTile<T extends IStorageNetworkTile<T>> extends ILocatable
{
    T getAssociatedCore();
    
    World getNetworkWorld();
    
    void receiveMappingChange(final StorageNetworkHandler.MappingChange p0);
    
    @Nullable
    default StorageNetwork getNetwork() {
        return StorageNetworkHandler.getHandler(this.getNetworkWorld()).getNetwork(this.getAssociatedCore().getLocationPos());
    }
    
    default T resolveMasterCore() {
        T assoc;
        T next;
        for (assoc = this.getAssociatedCore(); assoc != (next = this.getAssociatedCore()); assoc = next) {}
        return assoc;
    }
}
