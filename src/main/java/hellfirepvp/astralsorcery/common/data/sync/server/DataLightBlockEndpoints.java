package hellfirepvp.astralsorcery.common.data.sync.server;

import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientLightBlockEndpoints;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;

public class DataLightBlockEndpoints extends AbstractData
{
    private final Map<RegistryKey<Level>, Set<BlockPos>> serverPositions;
    private final Map<RegistryKey<Level>, Map<BlockPos, Boolean>> serverChangeBuffer;
    private final Set<RegistryKey<Level>> dimensionClearBuffer;
    
    private DataLightBlockEndpoints(final ResourceLocation key) {
        super(key);
        this.serverPositions = new HashMap<RegistryKey<Level>, Set<BlockPos>>();
        this.serverChangeBuffer = new HashMap<RegistryKey<Level>, Map<BlockPos, Boolean>>();
        this.dimensionClearBuffer = new HashSet<RegistryKey<Level>>();
    }
    
    public void updateNewEndpoint(final RegistryKey<Level> dim, final BlockPos pos) {
        final Map<BlockPos, Boolean> posMap = this.serverChangeBuffer.computeIfAbsent(dim, k -> new HashMap());
        posMap.put(pos, true);
        final Set<BlockPos> posBuffer = this.serverPositions.computeIfAbsent(dim, k -> new HashSet());
        posBuffer.add(pos);
        this.markDirty();
    }
    
    public void updateNewEndpoints(final RegistryKey<Level> dim, final Collection<BlockPos> newPositions) {
        final Map<BlockPos, Boolean> posMap = this.serverChangeBuffer.computeIfAbsent(dim, k -> new HashMap());
        for (final BlockPos pos : newPositions) {
            posMap.put(pos, true);
        }
        final Set<BlockPos> posBuffer = this.serverPositions.computeIfAbsent(dim, k -> new HashSet());
        posBuffer.addAll(newPositions);
        this.markDirty();
    }
    
    public void removeEndpoints(final RegistryKey<Level> dim, final Collection<BlockPos> positions) {
        final Map<BlockPos, Boolean> posMap = this.serverChangeBuffer.computeIfAbsent(dim, k -> new HashMap());
        for (final BlockPos pos : positions) {
            posMap.put(pos, false);
        }
        final Set<BlockPos> posBuffer = this.serverPositions.computeIfAbsent(dim, k -> new HashSet());
        if (posBuffer.removeAll(positions)) {
            this.markDirty();
        }
    }
    
    public boolean doesPositionReceiveStarlightServer(final Level world, final BlockPos pos) {
        return this.serverPositions.getOrDefault(world.dimension(), Collections.emptySet()).contains(pos);
    }
    
    @Override
    public void clear(final RegistryKey<Level> dim) {
        if (this.serverPositions.remove(dim) != null) {
            this.serverChangeBuffer.remove(dim);
            this.dimensionClearBuffer.add(dim);
            this.markDirty();
        }
    }
    
    @Override
    public void clearServer() {
        this.dimensionClearBuffer.clear();
        this.serverChangeBuffer.clear();
        this.serverPositions.clear();
    }
    
    @Override
    public void writeAllDataToPacket(final CompoundTag compound) {
        for (final RegistryKey<Level> dim : this.serverPositions.keySet()) {
            final Set<BlockPos> dat = this.serverPositions.get(dim);
            final ListTag dataList = new ListTag();
            for (final BlockPos pos : dat) {
                final CompoundTag cmp = new CompoundTag();
                cmp.func_74772_a("pos", pos.func_218275_a());
                dataList.add((Object)cmp);
            }
            compound.put(dim.func_240901_a_().toString(), (Tag)dataList);
        }
    }
    
    @Override
    public void writeDiffDataToPacket(final CompoundTag compound) {
        final ListTag clearList = new ListTag();
        for (final RegistryKey<Level> dim : this.dimensionClearBuffer) {
            clearList.add((Object)StringTag.func_229705_a_(dim.func_240901_a_().withStyle()));
        }
        compound.put("clear", (Tag)clearList);
        for (final RegistryKey<Level> dim : this.serverChangeBuffer.keySet()) {
            if (this.dimensionClearBuffer.contains(dim)) {
                continue;
            }
            final Map<BlockPos, Boolean> data = this.serverChangeBuffer.get(dim);
            final ListTag dataList = new ListTag();
            for (final BlockPos pos : data.keySet()) {
                final CompoundTag cmp = new CompoundTag();
                cmp.func_74772_a("pos", pos.func_218275_a());
                cmp.putBoolean("add", (boolean)data.get(pos));
                dataList.add((Object)cmp);
            }
            compound.put(dim.func_240901_a_().toString(), (Tag)dataList);
        }
        this.dimensionClearBuffer.clear();
        this.serverChangeBuffer.clear();
    }
    
    public static class Provider extends AbstractDataProvider<DataLightBlockEndpoints, ClientLightBlockEndpoints>
    {
        public Provider(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public DataLightBlockEndpoints provideServerData() {
            return new DataLightBlockEndpoints(this.getKey(), null);
        }
        
        @Override
        public ClientLightBlockEndpoints provideClientData() {
            return new ClientLightBlockEndpoints();
        }
        
        @Override
        public ClientDataReader<ClientLightBlockEndpoints> createReader() {
            return new ClientLightBlockEndpoints.Reader();
        }
    }
}
