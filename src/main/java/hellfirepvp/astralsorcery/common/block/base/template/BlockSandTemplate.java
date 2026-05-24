package hellfirepvp.astralsorcery.common.block.base.template;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.BlockGetter;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.FallingBlock;

public class BlockSandTemplate extends FallingBlock implements CustomItemBlock
{
    public BlockSandTemplate() {
        super(PropertiesMisc.defaultSand());
    }
    
    public int func_189876_x(final BlockState state, final IBlockReader reader, final BlockPos pos) {
        return 14409376;
    }
}
