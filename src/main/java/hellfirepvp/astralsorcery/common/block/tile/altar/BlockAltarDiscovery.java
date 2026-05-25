package hellfirepvp.astralsorcery.common.block.tile.altar;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;

public class BlockAltarDiscovery extends BlockAltar
{
    private final VoxelShape shape;
    
    public BlockAltarDiscovery() {
        super(AltarType.DISCOVERY);
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final VoxelShape base = Block.of(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);
        final VoxelShape pillar = VoxelShapes.func_197873_a(0.25, 0.125, 0.25, 0.75, 0.59375, 0.75);
        final VoxelShape head = VoxelShapes.func_197873_a(0.0, 0.59375, 0.0, 1.0, 0.96875, 1.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, base, pillar, head);
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return this.shape;
    }
}
