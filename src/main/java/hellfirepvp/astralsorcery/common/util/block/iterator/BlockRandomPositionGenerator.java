package hellfirepvp.astralsorcery.common.util.block.iterator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public class BlockRandomPositionGenerator extends BlockPositionGenerator
{
    @Override
    protected BlockPos genNext(final Vector3 offset, final double radius) {
        if (radius <= 0.0) {
            return offset.toBlockPos();
        }
        return offset.clone().add(Vector3.random().multiply(radius)).toBlockPos();
    }
    
    @Override
    public void writeToNBT(final CompoundTag nbt) {
    }
    
    @Override
    public void readFromNBT(final CompoundTag nbt) {
    }
}
