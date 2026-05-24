package hellfirepvp.astralsorcery.common.util.block.iterator;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import java.util.function.BiPredicate;

public abstract class BlockPositionGenerator
{
    private BiPredicate<BlockPos, Double> posFilter;
    
    public BlockPositionGenerator() {
        this.posFilter = ((pos, radius) -> true);
    }
    
    public final BlockPositionGenerator andFilter(final Predicate<BlockPos> filter) {
        return this.andFilter((pos, radius) -> filter.test(pos));
    }
    
    public final BlockPositionGenerator andFilter(final BiPredicate<BlockPos, Double> filter) {
        this.posFilter = this.posFilter.and(filter);
        return this;
    }
    
    public final BlockPositionGenerator copyFilterFrom(final BlockPositionGenerator other) {
        other.andFilter(this.posFilter);
        return this;
    }
    
    public final BlockPos generateNextPosition(final Vector3 offset, final double selectionRadius) {
        BlockPos next;
        do {
            next = this.genNext(offset, selectionRadius);
        } while (!this.posFilter.test(next, selectionRadius));
        return next;
    }
    
    protected abstract BlockPos genNext(final Vector3 p0, final double p1);
    
    public abstract void writeToNBT(final CompoundTag p0);
    
    public abstract void readFromNBT(final CompoundTag p0);
}
