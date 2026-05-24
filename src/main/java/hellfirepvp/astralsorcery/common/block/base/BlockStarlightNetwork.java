package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.level.block.state.BlockBehaviour;

public abstract class BlockStarlightNetwork extends BlockInventory
{
    protected BlockStarlightNetwork(final AbstractBlock.Properties builder) {
        super(builder);
    }
    
    @Override
    public void func_196243_a(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (state != newState) {
            final TileNetwork<?> te = MiscUtils.getTileAt((IBlockReader)worldIn, pos, TileNetwork.class, true);
            if (te != null) {
                te.onBreak();
            }
        }
        super.func_196243_a(state, worldIn, pos, newState, isMoving);
    }
}
