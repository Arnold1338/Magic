package hellfirepvp.astralsorcery.common.block.ore;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.base.template.BlockSandTemplate;

public class BlockAquamarineSandOre extends BlockSandTemplate
{
    public int getExpDrop(final BlockState state, final IWorldReader world, final BlockPos pos, final int fortune, final int silktouch) {
        return (silktouch == 0) ? (fortune * Mth.func_76136_a(this.RANDOM, 2, 5)) : 0;
    }
}
