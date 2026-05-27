package hellfirepvp.astralsorcery.common.data.sync.server;

import hellfirepvp.astralsorcery.common.data.sync.base.ClientData;
import hellfirepvp.astralsorcery.common.data.sync.base.ClientDataReader;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientLightConnections;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractDataProvider;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionChain;
import net.minecraft.util.Tuple;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.sync.base.AbstractData;

public class DataLightConnections extends AbstractData
{
    private final Map<ResourceKey<Level>, Map<BlockPos, Set<BlockPos>>> serverPosBuffer;
    private final Map<ResourceKey<Level>, LinkedList<Tuple<TransmissionChain.LightConnection, Boolean>>> serverChangeBuffer;
    private final Set<ResourceKey<Level>> dimensionClearBuffer;
    
    private DataLightConnections(final ResourceLocation key) {
        super(key);
        this.serverPosBuffer = new HashMap<ResourceKey<Level>, Map<BlockPos, Set<BlockPos>>>();
        this.serverChangeBuffer = new HashMap<ResourceKey<Level>, LinkedList<Tuple<TransmissionChain.LightConnection, Boolean>>>();
        this.dimensionClearBuffer = new HashSet<ResourceKey<Level>>();
    }
    
    public void updateNewConnectionsThreaded(final ResourceKey<Level> dim, final List<TransmissionChain.LightConnection> newlyAddedConnections) {
        final Map<BlockPos, Set<BlockPos>> posBufferDim = this.serverPosBuffer.computeIfAbsent(dim, k -> new HashMap());
        for (final TransmissionChain.LightConnection c : newlyAddedConnections) {
            final BlockPos start = c.getStart();
            final Set<BlockPos> endpoints = posBufferDim.computeIfAbsent(start, k -> new HashSet());
            endpoints.add(c.getEnd());
        }
        this.notifyConnectionAdd(dim, newlyAddedConnections);
        if (newlyAddedConnections.size() > 0) {
            this.markDirty();
        }
    }
    
    public void removeOldConnectionsThreaded(final ResourceKey<Level> dim, final List<TransmissionChain.LightConnection> invalidConnections) {
        final Map<BlockPos, Set<BlockPos>> posBufferDim = this.serverPosBuffer.get(dim);
        if (posBufferDim != null) {
            for (final TransmissionChain.LightConnection c : invalidConnections) {
                final BlockPos start = c.getStart();
                final Set<BlockPos> ends = posBufferDim.get(start);
                if (ends == null) {
                    continue;
                }
                ends.remove(c.getEnd());
                if (!ends.isEmpty()) {
                    continue;
                }
                posBufferDim.remove(start);
            }
        }
        this.notifyConnectionRemoval(dim, invalidConnections);
        if (invalidConnections.size() > 0) {
            this.markDirty();
        }
    }
    
    @Override
    public void clear(final ResourceKey<Level> dim) {
        if (this.serverPosBuffer.remove(dim) != null) {
            this.dimensionClearBuffer.add(dim);
            this.markDirty();
        }
    }
    
    @Override
    public void clearServer() {
        this.dimensionClearBuffer.clear();
        this.serverChangeBuffer.clear();
        this.serverPosBuffer.clear();
    }
    
    private void notifyConnectionAdd(final ResourceKey<Level> dim, final List<TransmissionChain.LightConnection> added) {
        final LinkedList<Tuple<TransmissionChain.LightConnection, Boolean>> ch = this.serverChangeBuffer.computeIfAbsent(dim, k -> new LinkedList());
        for (final TransmissionChain.LightConnection l : added) {
            ch.add((Tuple<TransmissionChain.LightConnection, Boolean>)new Tuple((Object)l, (Object)true));
        }
        this.dimensionClearBuffer.remove(dim);
    }
    
    private void notifyConnectionRemoval(final ResourceKey<Level> dim, final List<TransmissionChain.LightConnection> removal) {
        final LinkedList<Tuple<TransmissionChain.LightConnection, Boolean>> ch = this.serverChangeBuffer.computeIfAbsent(dim, k -> new LinkedList());
        for (final TransmissionChain.LightConnection l : removal) {
            ch.add((Tuple<TransmissionChain.LightConnection, Boolean>)new Tuple((Object)l, (Object)false));
        }
    }
    
    @Override
    public void writeAllDataToPacket(final CompoundTag compound) {
        for (final ResourceKey<Level> dim : this.serverPosBuffer.keySet()) {
            final Map<BlockPos, Set<BlockPos>> dat = this.serverPosBuffer.get(dim);
            final ListTag dataList = new ListTag();
            for (final BlockPos start : dat.keySet()) {
                final Set<BlockPos> endPositions = dat.get(start);
                if (endPositions == null) {
                    continue;
                }
                for (final BlockPos end : endPositions) {
                    final CompoundTag cmp = new CompoundTag();
                    cmp.func_74772_a("start", start.func_218275_a());
                    cmp.func_74772_a("end", end.func_218275_a());
                    dataList.add((Object)cmp);
                }
            }
            compound.put(dim.func_240901_a_().toString(), (Tag)dataList);
        }
    }
    
    @Override
    public void writeDiffDataToPacket(final CompoundTag compound) {
        final ListTag clearList = new ListTag();
        for (final ResourceKey<Level> dim : this.dimensionClearBuffer) {
            clearList.add((Object)StringTag.valueOf(dim.func_240901_a_().withStyle()));
        }
        compound.put("clear", (Tag)clearList);
        for (final ResourceKey<Level> dim : this.serverChangeBuffer.keySet()) {
            if (this.dimensionClearBuffer.contains(dim)) {
                continue;
            }
            final LinkedList<Tuple<TransmissionChain.LightConnection, Boolean>> changes = this.serverChangeBuffer.get(dim);
            if (changes.isEmpty()) {
                continue;
            }
            final ListTag list = new ListTag();
            for (final Tuple<TransmissionChain.LightConnection, Boolean> tuple : changes) {
                final CompoundTag connection = new CompoundTag();
                connection.func_74772_a("start", ((TransmissionChain.LightConnection)tuple.getA()).getStart().func_218275_a());
                connection.func_74772_a("end", ((TransmissionChain.LightConnection)tuple.getA()).getEnd().func_218275_a());
                connection.putBoolean("connect", (boolean)tuple.getB());
                list.add((Object)connection);
            }
            compound.put(dim.func_240901_a_().toString(), (Tag)list);
        }
        this.dimensionClearBuffer.clear();
        this.serverChangeBuffer.clear();
    }
    
    public static class Provider extends AbstractDataProvider<DataLightConnections, ClientLightConnections>
    {
        public Provider(final ResourceLocation key) {
            super(key);
        }
        
        @Override
        public DataLightConnections provideServerData() {
            return new DataLightConnections(this.getKey(), null);
        }
        
        @Override
        public ClientLightConnections provideClientData() {
            return new ClientLightConnections();
        }
        
        @Override
        public ClientDataReader<ClientLightConnections> createReader() {
            return new ClientLightConnections.Reader();
        }
    }
}
