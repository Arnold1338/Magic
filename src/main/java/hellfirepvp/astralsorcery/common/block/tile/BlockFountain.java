package hellfirepvp.astralsorcery.common.block.tile;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.tile.TileFountain;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesWood;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockFountain extends BaseEntityBlock implements CustomItemBlock
{
    private final VoxelShape shape;
    
    public BlockFountain() {
        super(PropertiesWood.defaultInfusedWood());
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final VoxelShape m1 = Block.of(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
        final VoxelShape m2 = Block.of(4.0, 6.0, 4.0, 12.0, 10.0, 12.0);
        final VoxelShape m3 = Block.of(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
        final VoxelShape m4 = Block.of(0.0, 4.0, 0.0, 16.0, 6.0, 16.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, m1, m2, m3, m4);
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return this.shape;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileFountain();
    }
}
