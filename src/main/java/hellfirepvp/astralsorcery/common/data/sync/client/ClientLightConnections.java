package hellfirepvp.astralsorcery.common.data.sync.client;

import net.minecraft.nbt.ListTag;
import java.util.Iterator;
import java.util.HashSet;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;

public class ClientLightConnections extends ClientData<ClientLightConnections>
{
    private final Map<RegistryKey<World>, Map<BlockPos, Set<BlockPos>>> clientPosBuffer;
    
    public ClientLightConnections() {
        this.clientPosBuffer = new HashMap<RegistryKey<World>, Map<BlockPos, Set<BlockPos>>>();
    }
    
    @Nonnull
    public Map<BlockPos, Set<BlockPos>> getClientConnections(final RegistryKey<World> dim) {
        return this.clientPosBuffer.getOrDefault(dim, new HashMap<BlockPos, Set<BlockPos>>());
    }
    
    @Override
    public void clear(final RegistryKey<World> dim) {
        this.clientPosBuffer.remove(dim);
    }
    
    @Override
    public void clearClient() {
        this.clientPosBuffer.clear();
    }
    
    public static class Reader extends ClientDataReader<ClientLightConnections>
    {
        @Override
        public void readFromIncomingFullSync(final ClientLightConnections cl, final CompoundTag compound) {
            cl.clientPosBuffer.clear();
            for (final String dimKey : compound.func_150296_c()) {
                final RegistryKey<World> dim = (RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
                final Map<BlockPos, Set<BlockPos>> posMap = new HashMap<BlockPos, Set<BlockPos>>();
                final ListTag list = compound.getList(dimKey, 10);
                for (final Tag iTag : list) {
                    final CompoundTag tag = (CompoundTag)iTag;
                    final BlockPos start = BlockPos.func_218283_e(tag.getDouble("start"));
                    final BlockPos end = BlockPos.func_218283_e(tag.getDouble("end"));
                    posMap.computeIfAbsent(start, s -> new HashSet()).add(end);
                }
                cl.clientPosBuffer.put(dim, posMap);
            }
        }
        
        @Override
        public void readFromIncomingDiff(final ClientLightConnections cl, final CompoundTag compound) {
            final Set<String> clearedDimensions = new HashSet<String>();
            for (final Tag dimKeyNBT : compound.getList("clear", 8)) {
                final String dimKey = dimKeyNBT.func_150285_a_();
                final RegistryKey<World> dim = (RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
                cl.clientPosBuffer.remove(dim);
                clearedDimensions.add(dimKey);
            }
            for (final String dimKey2 : compound.func_150296_c()) {
                if (clearedDimensions.contains(dimKey2)) {
                    continue;
                }
                final RegistryKey<World> dim2 = (RegistryKey<World>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey2));
                final Map<BlockPos, Set<BlockPos>> posMap = cl.clientPosBuffer.computeIfAbsent(dim2, d -> new HashMap());
                final ListTag list = compound.getList(dimKey2, 10);
                for (final Tag iTag : list) {
                    final CompoundTag tag = (CompoundTag)iTag;
                    final BlockPos start = BlockPos.func_218283_e(tag.getDouble("start"));
                    final BlockPos end = BlockPos.func_218283_e(tag.getDouble("end"));
                    final boolean newConnection = tag.getBoolean("connect");
                    if (newConnection) {
                        posMap.computeIfAbsent(start, s -> new HashSet()).add(end);
                    }
                    else {
                        final Set<BlockPos> endPoints = posMap.get(start);
                        if (endPoints == null || !endPoints.remove(end) || !endPoints.isEmpty()) {
                            continue;
                        }
                        posMap.remove(start);
                    }
                }
            }
        }
    }
}
