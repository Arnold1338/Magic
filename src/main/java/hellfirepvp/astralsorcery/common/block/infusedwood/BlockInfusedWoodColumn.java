package hellfirepvp.astralsorcery.common.block.infusedwood;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.entity.Mob;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.StateContainer;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.block.state.BooleanProperty;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import hellfirepvp.astralsorcery.common.block.base.template.BlockInfusedWoodTemplate;

public class BlockInfusedWoodColumn extends BlockInfusedWoodTemplate implements SimpleWaterloggedBlock
{
    public static final BooleanProperty WATERLOGGED;
    public static final EnumProperty<PillarType> PILLAR_TYPE;
    private final VoxelShape middleShape;
    private final VoxelShape bottomShape;
    private final VoxelShape topShape;
    
    public BlockInfusedWoodColumn() {
        this.func_180632_j((BlockState)((BlockState)((BlockState)this.func_176194_O().func_177621_b()).setValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Comparable)PillarType.MIDDLE)).setValue((Property)BlockInfusedWoodColumn.WATERLOGGED, (Comparable)false));
        this.middleShape = this.createPillarShape();
        this.topShape = this.createPillarTopShape();
        this.bottomShape = this.createPillarBottomShape();
    }
    
    protected VoxelShape createPillarShape() {
        return Block.of(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    }
    
    protected VoxelShape createPillarTopShape() {
        final VoxelShape column = Block.of(4.0, 0.0, 4.0, 12.0, 14.0, 12.0);
        final VoxelShape top = Block.of(2.0, 14.0, 2.0, 14.0, 16.0, 14.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, column, top);
    }
    
    protected VoxelShape createPillarBottomShape() {
        final VoxelShape column = Block.of(4.0, 2.0, 4.0, 12.0, 16.0, 12.0);
        final VoxelShape bottom = Block.of(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, column, bottom);
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        super.func_206840_a((StateContainer.Builder)builder);
        builder.func_206894_a(new Property[] { (Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Property)BlockInfusedWoodColumn.WATERLOGGED });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext ctx) {
        switch ((PillarType)state.getValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE)) {
            case TOP: {
                return this.topShape;
            }
            case BOTTOM: {
                return this.bottomShape;
            }
            default: {
                return this.middleShape;
            }
        }
    }
    
    public BlockState func_196271_a(final BlockState thisState, final Direction otherBlockFacing, final BlockState otherBlockState, final IWorld world, final BlockPos thisPos, final BlockPos otherBlockPos) {
        if (thisState.getValue((Property)BlockInfusedWoodColumn.WATERLOGGED)) {
            world.func_205219_F_().func_205360_a(thisPos, (Object)Fluids.field_204546_a, Fluids.field_204546_a.func_205569_a((IWorldReader)world));
        }
        return (BlockState)this.getThisState((IBlockReader)world, thisPos).setValue((Property)BlockInfusedWoodColumn.WATERLOGGED, thisState.getValue((Property)BlockInfusedWoodColumn.WATERLOGGED));
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext ctx) {
        final BlockPos blockpos = ctx.func_195995_a();
        final Level world = ctx.func_195991_k();
        final FluidState ifluidstate = world.func_204610_c(blockpos);
        return (BlockState)this.getThisState((IBlockReader)world, blockpos).setValue((Property)BlockInfusedWoodColumn.WATERLOGGED, (Comparable)(ifluidstate.func_206886_c() == Fluids.field_204546_a));
    }
    
    private BlockState getThisState(final IBlockReader world, final BlockPos pos) {
        final boolean hasUp = world.getBlockState(pos.above()).getBlock() instanceof BlockInfusedWoodColumn;
        final boolean hasDown = world.getBlockState(pos.renderItem()).getBlock() instanceof BlockInfusedWoodColumn;
        if (hasUp) {
            if (hasDown) {
                return (BlockState)this.defaultBlockState().setValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
            }
            return (BlockState)this.defaultBlockState().setValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Comparable)PillarType.BOTTOM);
        }
        else {
            if (hasDown) {
                return (BlockState)this.defaultBlockState().setValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Comparable)PillarType.TOP);
            }
            return (BlockState)this.defaultBlockState().setValue((Property)BlockInfusedWoodColumn.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
        }
    }
    
    public FluidState func_204507_t(final BlockState state) {
        return state.getValue((Property)BlockInfusedWoodColumn.WATERLOGGED) ? Fluids.field_204546_a.func_207204_a(false) : super.func_204507_t(state);
    }
    
    @Nullable
    public BlockPathTypes getAiPathNodeType(final BlockState state, final IBlockReader world, final BlockPos pos, @Nullable final MobEntity entity) {
        return BlockPathTypes.BLOCKED;
    }
    
    static {
        WATERLOGGED = BlockStateProperties.field_208198_y;
        PILLAR_TYPE = EnumProperty.func_177709_a("pillartype", (Class)PillarType.class);
    }
    
    public enum PillarType implements StringRepresentable
    {
        TOP, 
        MIDDLE, 
        BOTTOM;
        
        public String func_176610_l() {
            return this.name().toLowerCase(Locale.ROOT);
        }
        
        @Override
        public String toString() {
            return this.func_176610_l();
        }
    }
}
