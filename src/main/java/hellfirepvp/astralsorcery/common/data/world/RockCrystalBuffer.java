package hellfirepvp.astralsorcery.common.data.world;

import java.util.Iterator;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.ListTag;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.observerlib.common.data.base.WorldSection;
import java.util.Collection;
import net.minecraft.core.Vec3i;
import java.util.LinkedList;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.world.level.ChunkPos;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.observerlib.common.data.base.SectionWorldData;

public class RockCrystalBuffer extends SectionWorldData<BufferSection>
{
    public RockCrystalBuffer(final WorldCacheDomain.SaveKey<?> key) {
        super((WorldCacheDomain.SaveKey)key, 10);
    }
    
    protected BufferSection createNewSection(final int sectionX, final int sectionZ) {
        return new BufferSection(sectionX, sectionZ);
    }
    
    public List<BlockPos> collectPositions(final ChunkPos center, final int chunkRadius) {
        final List<BlockPos> out = new LinkedList<BlockPos>();
        for (int xx = -chunkRadius; xx <= chunkRadius; ++xx) {
            for (int zz = -chunkRadius; zz <= chunkRadius; ++zz) {
                final ChunkPos other = new ChunkPos(center.field_77276_a + xx, center.field_77275_b + zz);
                final BufferSection section = (BufferSection)this.getSection((Vector3i)other.func_206849_h());
                if (section != null) {
                    this.read(() -> out.addAll(section.crystalPositions));
                }
            }
        }
        return out;
    }
    
    public void addOre(final BlockPos pos) {
        final BufferSection section = (BufferSection)this.getOrCreateSection((Vector3i)pos);
        this.write(() -> section.crystalPositions.add(pos));
        this.markDirty((WorldSection)section);
    }
    
    public void removeOre(final BlockPos pos) {
        final BufferSection section = (BufferSection)this.getSection((Vector3i)pos);
        if (section != null) {
            this.write(() -> section.crystalPositions.remove(pos));
            this.markDirty((WorldSection)section);
        }
    }
    
    public void writeToNBT(final CompoundTag nbt) {
    }
    
    public void readFromNBT(final CompoundTag nbt) {
    }
    
    public void updateTick(final Level world) {
    }
    
    public static class BufferSection extends WorldSection
    {
        private final Set<BlockPos> crystalPositions;
        
        private BufferSection(final int sX, final int sZ) {
            super(sX, sZ);
            this.crystalPositions = new HashSet<BlockPos>();
        }
        
        public void writeToNBT(final CompoundTag tag) {
            final ListTag posList = new ListTag();
            for (final BlockPos exactPos : this.crystalPositions) {
                posList.add((Object)NBTHelper.writeBlockPosToNBT(exactPos, new CompoundTag()));
            }
            tag.put("posList", (Tag)posList);
        }
        
        public void readFromNBT(final CompoundTag tag) {
            this.crystalPositions.clear();
            final ListTag entries = tag.getList("posList", 10);
            for (int j = 0; j < entries.size(); ++j) {
                this.crystalPositions.add(NBTHelper.readBlockPosFromNBT(entries.getCompound(j)));
            }
        }
    }
}
