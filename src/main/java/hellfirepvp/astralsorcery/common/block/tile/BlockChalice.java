package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.phys.shapes.Shapes;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockChalice extends BaseEntityBlock implements CustomItemBlock
{
    private static final VoxelShape CHALICE;
    
    public BlockChalice() {
        super(PropertiesMisc.defaultGoldMachinery().harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockChalice.CHALICE;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult brtr) {
        final ItemStack interact = player.getItemInHand(hand);
        final TileChalice tc = MiscUtils.getTileAt((IBlockReader)world, pos, TileChalice.class, true);
        if (tc != null) {
            final IFluidHandlerItem handlerItem = (IFluidHandlerItem)FluidUtil.getFluidHandler(interact).orElse((Object)null);
            if (handlerItem != null) {
                if (!world.level()) {
                    final FluidStack st = FluidUtil.getFluidContained(interact).orElse(FluidStack.EMPTY);
                    if (st.isEmpty()) {
                        final FluidActionResult far = FluidUtil.tryFillContainer(interact, tc.getTankAccess(), 1000, player, true);
                        if (far.isSuccess() && !player.getVehicle()) {
                            interact.shrink(1);
                            player.func_184611_a(hand, interact);
                            player.getInventory().func_191975_a(world, far.getResult());
                        }
                    }
                    else {
                        final FluidActionResult far = FluidUtil.tryEmptyContainer(interact, tc.getTankAccess(), 1000, player, true);
                        if (far.isSuccess() && !player.getVehicle()) {
                            interact.shrink(1);
                            player.func_184611_a(hand, interact);
                            player.getInventory().func_191975_a(world, far.getResult());
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    
    public boolean func_149740_M(final BlockState p_149740_1_) {
        return true;
    }
    
    public int func_180641_l(final BlockState state, final Level world, final BlockPos pos) {
        final TileChalice tc = MiscUtils.getTileAt((IBlockReader)world, pos, TileChalice.class, false);
        if (tc != null) {
            return Mth.func_76123_f(tc.getTank().getPercentageFilled() * 15.0f);
        }
        return 0;
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileChalice();
    }
    
    static {
        CHALICE = VoxelShapes.func_197873_a(0.125, 0.0, 0.125, 0.875, 0.875, 0.875);
    }
}
