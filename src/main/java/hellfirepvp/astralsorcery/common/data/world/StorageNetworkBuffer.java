package hellfirepvp.astralsorcery.common.data.world;

import net.minecraft.world.level.Level;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.phys.AABB;
import java.util.Iterator;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import java.util.List;
import net.minecraft.world.level.ChunkPos;
import hellfirepvp.astralsorcery.common.storage.StorageNetwork;
import net.minecraft.core.BlockPos;
import java.util.Map;
import hellfirepvp.observerlib.common.data.base.GlobalWorldData;

public class StorageNetworkBuffer extends GlobalWorldData
{
    private final Map<BlockPos, StorageNetwork> rawNetworks;
    private final Map<ChunkPos, List<StorageNetwork>> availableNetworks;
    
    public StorageNetworkBuffer(final WorldCacheDomain.SaveKey<?> key) {
        super((WorldCacheDomain.SaveKey)key);
        this.rawNetworks = Maps.newHashMap();
        this.availableNetworks = Maps.newHashMap();
    }
    
    @Nullable
    public StorageNetwork getNetwork(final BlockPos masterPos) {
        return this.rawNetworks.get(masterPos);
    }
    
    private void rebuildAccessContext() {
        this.availableNetworks.clear();
        for (final StorageNetwork network : this.rawNetworks.values()) {
            for (final StorageNetwork.CoreArea core : network.getCores()) {
                final AABB box = core.getRealBox();
                final ChunkPos from = Vector3.getMin(box).toChunkPos();
                final ChunkPos to = Vector3.getMax(box).toChunkPos();
                for (int chX = from.field_77276_a; chX <= to.field_77276_a; ++chX) {
                    for (int chZ = from.field_77275_b; chZ <= to.field_77275_b; ++chZ) {
                        this.availableNetworks.computeIfAbsent(new ChunkPos(chX, chZ), pos -> Lists.newArrayList()).add(network);
                    }
                }
            }
        }
    }
    
    public void writeToNBT(final CompoundTag compound) {
        final ListTag networks = new ListTag();
        for (final StorageNetwork network : this.rawNetworks.values()) {
            final CompoundTag tag = new CompoundTag();
            network.writeToNBT(tag);
            networks.add((Object)tag);
        }
        compound.put("networks", (Tag)networks);
    }
    
    public void readFromNBT(final CompoundTag compound) {
        this.rawNetworks.clear();
        final ListTag networks = compound.getList("networks", 10);
        for (int i = 0; i < networks.size(); ++i) {
            final CompoundTag tag = networks.getCompound(i);
            final StorageNetwork net = new StorageNetwork();
            net.readFromNBT(tag);
            if (!net.getCores().isEmpty()) {
                StorageNetwork.CoreArea master = net.getMaster();
                if (master == null) {
                    master = MiscUtils.getRandomEntry(net.getCores(), this.rand);
                }
                this.rawNetworks.put(master.getPos(), net);
            }
        }
        this.rebuildAccessContext();
    }
    
    public void updateTick(final World world) {
    }
}
