package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeacon;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockTreeBeacon extends BlockStarlightNetwork implements CustomItemBlock
{
    private static final VoxelShape SHAPE;
    
    public BlockTreeBeacon() {
        super(PropertiesMisc.defaultPlant().func_200948_a(1.5f, 6.0f).harvestLevel(1).harvestTool(ToolType.AXE).func_235838_a_(state -> 6).func_200947_a(SoundType.field_185850_c));
    }
    
    @Override
    public void func_180633_a(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        super.func_180633_a(world, pos, state, placer, stack);
        final TileTreeBeacon ttb = MiscUtils.getTileAt((IBlockReader)world, pos, TileTreeBeacon.class, true);
        if (ttb != null && !world.func_201670_d() && placer instanceof ServerPlayer && !MiscUtils.isPlayerFakeMP((ServerPlayer)placer)) {
            ttb.setPlayerUUID(placer.getUUID());
        }
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext context) {
        return BlockTreeBeacon.SHAPE;
    }
    
    public BlockState func_196271_a(final BlockState state, final Direction placedAgainst, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!this.func_196260_a(state, (IWorldReader)world, pos)) {
            return Blocks.field_150350_a.defaultBlockState();
        }
        return state;
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return func_220064_c((IBlockReader)world, pos.func_177977_b());
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileTreeBeacon();
    }
    
    static {
        SHAPE = VoxelShapes.func_197873_a(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
    }
}
