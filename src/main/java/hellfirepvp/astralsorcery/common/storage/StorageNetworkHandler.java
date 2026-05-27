package hellfirepvp.astralsorcery.common.storage;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.StorageNetworkBuffer;
import java.util.HashMap;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;

public class StorageNetworkHandler
{
    private static final Map<ResourceKey<Level>, NetworkHelper> mappingHelpers;
    
    public static NetworkHelper getHandler(final Level world) {
        return StorageNetworkHandler.mappingHelpers.computeIfAbsent((ResourceKey<Level>)world.dimension(), id -> new NetworkHelper(world));
    }
    
    public static void clearHandler(final Level world) {
        clearHandler((ResourceKey<Level>)world.dimension());
    }
    
    public static void clearHandler(final ResourceKey<Level> dimKey) {
        StorageNetworkHandler.mappingHelpers.remove(dimKey);
    }
    
    static {
        mappingHelpers = new HashMap<ResourceKey<Level>, NetworkHelper>();
    }
    
    public static class NetworkHelper
    {
        private final StorageNetworkBuffer buffer;
        
        private NetworkHelper(final Level world) {
            this.buffer = (StorageNetworkBuffer)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_STORAGE_NETWORK);
        }
        
        @Nullable
        public StorageNetwork getNetwork(final BlockPos networkMaster) {
            return this.buffer.getNetwork(networkMaster);
        }
    }
    
    public static class MappingChange
    {
    }
}
