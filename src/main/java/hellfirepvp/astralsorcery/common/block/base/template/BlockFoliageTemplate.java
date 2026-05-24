package hellfirepvp.astralsorcery.common.block.base.template;

import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.BlockGetter;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.level.block.state.BlockBehaviour;
import net.minecraftforge.common.IPlantable;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.level.block.Block;

public abstract class BlockFoliageTemplate extends Block implements CustomItemBlock, IPlantable
{
    public BlockFoliageTemplate(final AbstractBlock.Properties properties) {
        super(properties);
    }
    
    protected abstract boolean isValidGround(final BlockState p0, final IBlockReader p1, final BlockPos p2);
    
    public BlockState func_196271_a(final BlockState state, final Direction dir, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!state.func_196955_c((IWorldReader)world, pos)) {
            return Blocks.field_150350_a.defaultBlockState();
        }
        return super.func_196271_a(state, dir, facingState, world, pos, facingPos);
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        final BlockPos blockpos = pos.func_177977_b();
        if (state.getBlock() == this) {
            return world.getBlockState(blockpos).canSustainPlant((IBlockReader)world, blockpos, Direction.UP, (IPlantable)this);
        }
        return this.isValidGround(world.getBlockState(blockpos), (IBlockReader)world, blockpos);
    }
    
    public boolean func_200123_i(final BlockState p_200123_1_, final IBlockReader p_200123_2_, final BlockPos p_200123_3_) {
        return true;
    }
    
    public BlockState getPlant(final IBlockReader world, final BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) {
            return this.defaultBlockState();
        }
        return state;
    }
}
