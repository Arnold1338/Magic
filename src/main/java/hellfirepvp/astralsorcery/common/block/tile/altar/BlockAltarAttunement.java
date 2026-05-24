package hellfirepvp.astralsorcery.common.block.tile.altar;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;

public class BlockAltarAttunement extends BlockAltar
{
    private final VoxelShape shape;
    
    public BlockAltarAttunement() {
        super(AltarType.ATTUNEMENT);
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final VoxelShape base = Block.func_208617_a(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        final VoxelShape pillar = Block.func_208617_a(4.0, 2.0, 4.0, 12.0, 10.0, 12.0);
        final VoxelShape head = Block.func_208617_a(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, base, pillar, head);
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return this.shape;
    }
}
