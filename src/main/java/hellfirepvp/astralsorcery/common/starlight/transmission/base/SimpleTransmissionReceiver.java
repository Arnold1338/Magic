package hellfirepvp.astralsorcery.common.starlight.transmission.base;

import java.util.Objects;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.level.level.Level;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;

public abstract class SimpleTransmissionReceiver<T extends TileReceiverBase<?>> implements ITransmissionReceiver
{
    private BlockPos thisPos;
    private final Set<BlockPos> sourcesToThis;
    private boolean needsTileSync;
    
    public SimpleTransmissionReceiver(final BlockPos thisPos) {
        this.sourcesToThis = new HashSet<BlockPos>();
        this.needsTileSync = false;
        this.thisPos = thisPos;
    }
    
    @Override
    public void update(final World world) {
        if (this.needsTileSync) {
            final T tile = this.getTileAtPos(world);
            if (tile != null && this.syncTileData(world, tile)) {
                this.needsTileSync = false;
            }
        }
    }
    
    public final void markForTileSync() {
        this.needsTileSync = true;
    }
    
    public abstract boolean syncTileData(final World p0, final T p1);
    
    public abstract Class<T> getTileClass();
    
    @Override
    public BlockPos getLocationPos() {
        return this.thisPos;
    }
    
    @Override
    public void notifySourceLink(final World world, final BlockPos source) {
        this.sourcesToThis.add(source);
    }
    
    @Override
    public void notifySourceUnlink(final World world, final BlockPos source) {
        this.sourcesToThis.remove(source);
    }
    
    @Override
    public boolean notifyBlockChange(final World world, final BlockPos changed) {
        return false;
    }
    
    @Override
    public List<BlockPos> getSources() {
        return new LinkedList<BlockPos>(this.sourcesToThis);
    }
    
    @Nullable
    public T getTileAtPos(final World world) {
        return MiscUtils.getTileAt((IBlockReader)world, this.getLocationPos(), this.getTileClass(), false);
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
        this.sourcesToThis.clear();
        this.thisPos = NBTHelper.readBlockPosFromNBT(compound);
        this.needsTileSync = compound.getBoolean("needsTileSync");
        final ListTag list = compound.getList("sources", 10);
        for (int i = 0; i < list.size(); ++i) {
            this.sourcesToThis.add(NBTHelper.readBlockPosFromNBT(list.getCompound(i)));
        }
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
        NBTHelper.writeBlockPosToNBT(this.thisPos, compound);
        compound.putBoolean("needsTileSync", this.needsTileSync);
        final ListTag sources = new ListTag();
        for (final BlockPos source : this.sourcesToThis) {
            final CompoundTag comp = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(source, comp);
            sources.add((Object)comp);
        }
        compound.put("sources", (Tag)sources);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimpleTransmissionReceiver<?> that = (SimpleTransmissionReceiver<?>)o;
        return Objects.equals(this.thisPos, that.thisPos);
    }
}
