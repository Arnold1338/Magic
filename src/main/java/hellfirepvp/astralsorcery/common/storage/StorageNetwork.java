package hellfirepvp.astralsorcery.common.storage;

import java.util.Iterator;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.MapStream;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import java.util.Map;

public class StorageNetwork
{
    private CoreArea master;
    private final Map<BlockPos, AABB> cores;
    
    public StorageNetwork() {
        this.master = null;
        this.cores = Maps.newHashMap();
    }
    
    public boolean setMaster(@Nullable final BlockPos pos) {
        if (pos == null) {
            this.master = null;
            return true;
        }
        if (this.cores.containsKey(pos)) {
            this.master = new CoreArea(pos, (AABB)this.cores.get(pos));
            return true;
        }
        return false;
    }
    
    @Nullable
    public CoreArea getMaster() {
        return this.master;
    }
    
    public boolean addCore(final BlockPos pos, final AABB box) {
        return this.cores.put(pos, box) == null;
    }
    
    public boolean removeCore(final BlockPos pos) {
        return this.cores.remove(pos) != null;
    }
    
    public List<CoreArea> getCores() {
        return MapStream.of(this.cores).toList((x$0, x$1) -> new CoreArea(x$0, x$1));
    }
    
    public void writeToNBT(final CompoundTag tag) {
        final ListTag list = new ListTag();
        for (final CoreArea coreData : this.getCores()) {
            final CompoundTag coreTag = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(coreData.getPos(), coreTag);
            NBTHelper.writeBoundingBox(coreData.getOffsetBox(), coreTag);
            list.add((Object)coreTag);
        }
        tag.put("cores", (Tag)list);
        final CoreArea master;
        if ((master = this.getMaster()) != null) {
            NBTHelper.setAsSubTag(tag, "master", nbt -> NBTHelper.writeBlockPosToNBT(master.getPos(), nbt));
        }
    }
    
    public void readFromNBT(final CompoundTag tag) {
        this.cores.clear();
        final ListTag list = tag.getList("cores", 10);
        for (int i = 0; i < list.size(); ++i) {
            final CompoundTag coreTag = list.getCompound(i);
            final BlockPos pos = NBTHelper.readBlockPosFromNBT(coreTag);
            final AABB box = NBTHelper.readBoundingBox(coreTag);
            this.addCore(pos, box);
        }
        this.setMaster(NBTHelper.readFromSubTag(tag, "master", NBTHelper::readBlockPosFromNBT));
    }
    
    public static class CoreArea
    {
        private final BlockPos pos;
        private final AABB offsetBox;
        
        private CoreArea(final BlockPos pos, final AABB offsetBox) {
            this.pos = pos;
            this.offsetBox = offsetBox;
        }
        
        public BlockPos getPos() {
            return this.pos;
        }
        
        public AABB getOffsetBox() {
            return this.offsetBox;
        }
        
        public AABB getRealBox() {
            return this.offsetBox.func_186670_a(this.getPos());
        }
    }
}
