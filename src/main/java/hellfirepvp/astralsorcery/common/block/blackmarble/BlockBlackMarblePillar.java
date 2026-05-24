package hellfirepvp.astralsorcery.common.block.blackmarble;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.entity.Mob;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.material.FluidState;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.world.level.level.material.Fluids;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.BlockGetter;
import net.minecraft.world.level.block.state.StateContainer;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.EnumProperty;
import net.minecraft.world.level.block.state.BooleanProperty;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import hellfirepvp.astralsorcery.common.block.base.template.BlockBlackMarbleTemplate;

public class BlockBlackMarblePillar extends BlockBlackMarbleTemplate implements SimpleWaterloggedBlock
{
    public static final BooleanProperty WATERLOGGED;
    public static final EnumProperty<PillarType> PILLAR_TYPE;
    private final VoxelShape middleShape;
    private final VoxelShape bottomShape;
    private final VoxelShape topShape;
    
    public BlockBlackMarblePillar() {
        this.func_180632_j((BlockState)((BlockState)((BlockState)this.func_176194_O().func_177621_b()).func_206870_a((Property)BlockBlackMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE)).func_206870_a((Property)BlockBlackMarblePillar.WATERLOGGED, (Comparable)false));
        this.middleShape = this.createPillarShape();
        this.topShape = this.createPillarTopShape();
        this.bottomShape = this.createPillarBottomShape();
    }
    
    protected VoxelShape createPillarShape() {
        return Block.func_208617_a(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }
    
    protected VoxelShape createPillarTopShape() {
        final VoxelShape column = Block.func_208617_a(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
        final VoxelShape top = Block.func_208617_a(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, column, top);
    }
    
    protected VoxelShape createPillarBottomShape() {
        final VoxelShape column = Block.func_208617_a(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
        final VoxelShape bottom = Block.func_208617_a(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, column, bottom);
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        super.func_206840_a((StateContainer.Builder)builder);
        builder.func_206894_a(new Property[] { (Property)BlockBlackMarblePillar.PILLAR_TYPE, (Property)BlockBlackMarblePillar.WATERLOGGED });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext ctx) {
        switch ((PillarType)state.getValue((Property)BlockBlackMarblePillar.PILLAR_TYPE)) {
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
        if (thisState.getValue((Property)BlockBlackMarblePillar.WATERLOGGED)) {
            world.func_205219_F_().func_205360_a(thisPos, (Object)Fluids.field_204546_a, Fluids.field_204546_a.func_205569_a((IWorldReader)world));
        }
        return (BlockState)this.getThisState((IBlockReader)world, thisPos).func_206870_a((Property)BlockBlackMarblePillar.WATERLOGGED, thisState.getValue((Property)BlockBlackMarblePillar.WATERLOGGED));
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext ctx) {
        final BlockPos blockpos = ctx.func_195995_a();
        final World world = ctx.func_195991_k();
        final FluidState fluidState = world.func_204610_c(blockpos);
        return (BlockState)this.getThisState((IBlockReader)world, blockpos).func_206870_a((Property)BlockBlackMarblePillar.WATERLOGGED, (Comparable)(fluidState.func_206886_c() == Fluids.field_204546_a));
    }
    
    private BlockState getThisState(final IBlockReader world, final BlockPos pos) {
        final boolean hasUp = world.getBlockState(pos.above()).getBlock() instanceof BlockBlackMarblePillar;
        final boolean hasDown = world.getBlockState(pos.func_177977_b()).getBlock() instanceof BlockBlackMarblePillar;
        if (hasUp) {
            if (hasDown) {
                return (BlockState)this.defaultBlockState().func_206870_a((Property)BlockBlackMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
            }
            return (BlockState)this.defaultBlockState().func_206870_a((Property)BlockBlackMarblePillar.PILLAR_TYPE, (Comparable)PillarType.BOTTOM);
        }
        else {
            if (hasDown) {
                return (BlockState)this.defaultBlockState().func_206870_a((Property)BlockBlackMarblePillar.PILLAR_TYPE, (Comparable)PillarType.TOP);
            }
            return (BlockState)this.defaultBlockState().func_206870_a((Property)BlockBlackMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
        }
    }
    
    public FluidState func_204507_t(final BlockState state) {
        return state.getValue((Property)BlockBlackMarblePillar.WATERLOGGED) ? Fluids.field_204546_a.func_207204_a(false) : super.func_204507_t(state);
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
