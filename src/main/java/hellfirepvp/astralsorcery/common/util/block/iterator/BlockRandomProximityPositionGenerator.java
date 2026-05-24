package hellfirepvp.astralsorcery.common.util.block.iterator;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public class BlockRandomProximityPositionGenerator extends BlockRandomPositionGenerator
{
    @Override
    protected BlockPos genNext(final Vector3 offset, final double radius) {
        final BlockPos next1 = super.genNext(offset, radius);
        final BlockPos next2 = super.genNext(offset, radius);
        return (offset.distance((Vector3i)next1) < offset.distance((Vector3i)next2)) ? next1 : next2;
    }
}
