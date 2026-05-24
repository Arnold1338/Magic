package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface BlockPredicate
{
    boolean test(final World p0, final BlockPos p1, final BlockState p2);
}
