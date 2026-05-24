package hellfirepvp.astralsorcery.common.block.tile.altar;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.block.base.LargeBlock;
import hellfirepvp.astralsorcery.common.block.tile.BlockAltar;

public class BlockAltarConstellation extends BlockAltar implements LargeBlock
{
    private static final AABB PLACEMENT_BOX;
    private final VoxelShape shape;
    
    public BlockAltarConstellation() {
        super(AltarType.CONSTELLATION);
        this.shape = this.createShape();
    }
    
    @Override
    public AABB getBlockSpace() {
        return BlockAltarConstellation.PLACEMENT_BOX;
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext context) {
        return this.canPlaceAt(context) ? this.defaultBlockState() : null;
    }
    
    protected VoxelShape createShape() {
        final VoxelShape base = Block.func_208617_a(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        final VoxelShape pillar = Block.func_208617_a(4.0, 4.0, 4.0, 12.0, 8.0, 12.0);
        final VoxelShape head = Block.func_208617_a(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, base, pillar, head);
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return this.shape;
    }
    
    static {
        PLACEMENT_BOX = new AABB(-1.0, 0.0, -1.0, 1.0, 1.0, 1.0);
    }
}
