package hellfirepvp.astralsorcery.common.block.tile.fountain;

import hellfirepvp.astralsorcery.common.block.tile.BlockFountain;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.level.block.state.BlockState;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffect;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.level.block.Block;

public abstract class BlockFountainPrime extends Block implements CustomItemBlock
{
    public BlockFountainPrime() {
        super(PropertiesMarble.defaultMarble().func_226896_b_());
    }
    
    @Nonnull
    public abstract FountainEffect<?> provideEffect();
    
    public BlockState func_196271_a(final BlockState state, final Direction placedAgainst, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!this.func_196260_a(state, (IWorldReader)world, pos)) {
            return Blocks.field_150350_a.defaultBlockState();
        }
        return state;
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return world.getBlockState(pos.above()).getBlock() instanceof BlockFountain;
    }
}
