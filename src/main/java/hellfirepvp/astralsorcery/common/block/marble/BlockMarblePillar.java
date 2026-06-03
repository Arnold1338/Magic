package hellfirepvp.astralsorcery.common.block.marble;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.entity.MobEntity;
import javax.annotation.Nullable;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.IWorldReader;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.state.StateContainer;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.world.level.block.IWaterLoggable;
import hellfirepvp.astralsorcery.common.block.base.template.BlockMarbleTemplate;

public class BlockMarblePillar extends BlockMarbleTemplate implements IWaterLoggable
{
    public static final BooleanProperty WATERLOGGED;
    public static final EnumProperty<PillarType> PILLAR_TYPE;
    private final VoxelShape middleShape;
    private final VoxelShape bottomShape;
    private final VoxelShape topShape;
    
    public BlockMarblePillar() {
        this.func_180632_j((BlockState)((BlockState)((BlockState)this.func_176194_O().func_177621_b()).func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE)).func_206870_a((Property)BlockMarblePillar.WATERLOGGED, (Comparable)false));
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
        return VoxelUtils.combineAll(IBooleanFunction.field_223244_o_, column, top);
    }
    
    protected VoxelShape createPillarBottomShape() {
        final VoxelShape column = Block.func_208617_a(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
        final VoxelShape bottom = Block.func_208617_a(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        return VoxelUtils.combineAll(IBooleanFunction.field_223244_o_, column, bottom);
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        super.func_206840_a((StateContainer.Builder)builder);
        builder.func_206894_a(new Property[] { (Property)BlockMarblePillar.PILLAR_TYPE, (Property)BlockMarblePillar.WATERLOGGED });
    }
    
    public VoxelShape func_220053_a(final BlockState state, final BlockGetter world, final BlockPos pos, final ISelectionContext ctx) {
        switch ((PillarType)state.func_177229_b((Property)BlockMarblePillar.PILLAR_TYPE)) {
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
    
    public BlockState func_196271_a(final BlockState thisState, final Direction otherBlockFacing, final BlockState otherBlockState, final LevelAccessor world, final BlockPos thisPos, final BlockPos otherBlockPos) {
        if (thisState.func_177229_b((Property)BlockMarblePillar.WATERLOGGED)) {
            world.func_205219_F_().func_205360_a(thisPos, (Object)Fluids.field_204546_a, Fluids.field_204546_a.func_205569_a((IWorldReader)world));
        }
        return (BlockState)this.getThisState((BlockGetter)world, thisPos).func_206870_a((Property)BlockMarblePillar.WATERLOGGED, thisState.func_177229_b((Property)BlockMarblePillar.WATERLOGGED));
    }
    
    @Nullable
    public BlockState func_196258_a(final BlockItemUseContext ctx) {
        final BlockPos blockpos = ctx.func_195995_a();
        final World world = ctx.func_195991_k();
        final FluidState ifluidstate = world.func_204610_c(blockpos);
        return (BlockState)this.getThisState((BlockGetter)world, blockpos).func_206870_a((Property)BlockMarblePillar.WATERLOGGED, (Comparable)(ifluidstate.func_206886_c() == Fluids.field_204546_a));
    }
    
    private BlockState getThisState(final BlockGetter world, final BlockPos pos) {
        final boolean hasUp = world.getBlockState(pos.func_177984_a()).func_177230_c() instanceof BlockMarblePillar;
        final boolean hasDown = world.getBlockState(pos.func_177977_b()).func_177230_c() instanceof BlockMarblePillar;
        if (hasUp) {
            if (hasDown) {
                return (BlockState)this.func_176223_P().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
            }
            return (BlockState)this.func_176223_P().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)PillarType.BOTTOM);
        }
        else {
            if (hasDown) {
                return (BlockState)this.func_176223_P().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)PillarType.TOP);
            }
            return (BlockState)this.func_176223_P().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)PillarType.MIDDLE);
        }
    }
    
    public FluidState func_204507_t(final BlockState state) {
        return state.func_177229_b((Property)BlockMarblePillar.WATERLOGGED) ? Fluids.field_204546_a.func_207204_a(false) : super.func_204507_t(state);
    }
    
    @Nullable
    public PathNodeType getAiPathNodeType(final BlockState state, final BlockGetter world, final BlockPos pos, @Nullable final MobEntity entity) {
        return PathNodeType.BLOCKED;
    }
    
    static {
        WATERLOGGED = BlockStateProperties.field_208198_y;
        PILLAR_TYPE = EnumProperty.func_177709_a("pillartype", (Class)PillarType.class);
    }
    
    public enum PillarType implements IStringSerializable
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
