package hellfirepvp.astralsorcery.common.data.sync.client;

import net.minecraft.nbt.ListTag;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import java.util.HashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import java.util.Collections;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;

public class ClientLightBlockEndpoints extends ClientData<ClientLightBlockEndpoints>
{
    private final Map<ResourceKey<Level>, Set<BlockPos>> clientPositions;
    
    public ClientLightBlockEndpoints() {
        this.clientPositions = new HashMap<ResourceKey<Level>, Set<BlockPos>>();
    }
    
    public boolean doesPositionReceiveStarlightClient(final Level world, final BlockPos pos) {
        return this.clientPositions.getOrDefault(world.dimension(), Collections.emptySet()).contains(pos);
    }
    
    @Override
    public void clear(final ResourceKey<Level> dim) {
        this.clientPositions.remove(dim);
    }
    
    @Override
    public void clearClient() {
        this.clientPositions.clear();
    }
    
    public static class Reader extends ClientDataReader<ClientLightBlockEndpoints>
    {
        @Override
        public void readFromIncomingFullSync(final ClientLightBlockEndpoints data, final CompoundTag compound) {
            data.clientPositions.clear();
            for (final String dimKey : compound.func_150296_c()) {
                final ResourceKey<Level> dim = (ResourceKey<Level>)ResourceKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
                final Set<BlockPos> positions = new HashSet<BlockPos>();
                final ListTag list = compound.getList(dimKey, 10);
                for (final Tag iTag : list) {
                    final CompoundTag tag = (CompoundTag)iTag;
                    final BlockPos pos = BlockPos.func_218283_e(tag.getDouble("pos"));
                    positions.add(pos);
                }
                data.clientPositions.put(dim, positions);
            }
        }
        
        @Override
        public void readFromIncomingDiff(final ClientLightBlockEndpoints data, final CompoundTag compound) {
            final Set<String> clearedDimensions = new HashSet<String>();
            for (final Tag dimKeyNBT : compound.getList("clear", 8)) {
                final String dimKey = dimKeyNBT.func_150285_a_();
                final ResourceKey<Level> dim = (ResourceKey<Level>)ResourceKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey));
                data.clientPositions.remove(dim);
                clearedDimensions.add(dimKey);
            }
            for (final String dimKey2 : compound.func_150296_c()) {
                if (clearedDimensions.contains(dimKey2)) {

                }
                final ResourceKey<Level> dim2 = (ResourceKey<Level>)ResourceKey.func_240903_a_(Registry.field_239699_ae_, new ResourceLocation(dimKey2));
                final Set<BlockPos> positions = data.clientPositions.computeIfAbsent(dim2, k -> new HashSet());
                final ListTag list = compound.getList(dimKey2, 10);
                for (final Tag iTag : list) {
                    final CompoundTag tag = (CompoundTag)iTag;
                    final BlockPos pos = BlockPos.func_218283_e(tag.getDouble("pos"));
                    final boolean addNew = tag.getBoolean("add");
                    if (addNew) {
                        positions.add(pos);
                    }
                    else {
                        positions.remove(pos);
                    }
                }
            }
        }
    }
}
