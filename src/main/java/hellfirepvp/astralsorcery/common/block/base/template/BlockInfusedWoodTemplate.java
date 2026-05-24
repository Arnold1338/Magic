package hellfirepvp.astralsorcery.common.block.base.template;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesWood;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.Block;

public class BlockInfusedWoodTemplate extends Block implements CustomItemBlock
{
    public BlockInfusedWoodTemplate() {
        super(PropertiesWood.defaultInfusedWood());
    }
    
    public int getFlammability(final BlockState state, final IBlockReader world, final BlockPos pos, final Direction face) {
        return 60;
    }
}
