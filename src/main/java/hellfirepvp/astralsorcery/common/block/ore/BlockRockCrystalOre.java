package hellfirepvp.astralsorcery.common.block.ore;

import net.minecraft.util.math.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.Block;

public class BlockRockCrystalOre extends Block implements CustomItemBlock
{
    public BlockRockCrystalOre() {
        super(PropertiesMisc.defaultRock().harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }
    
    public int getExpDrop(final BlockState state, final IWorldReader world, final BlockPos pos, final int fortune, final int silktouch) {
        return fortune * MathHelper.func_76136_a(this.RANDOM, 8, 14);
    }
}
