package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.phys.shapes.Shapes;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.block.ContainerBlock;

public class BlockChalice extends ContainerBlock implements CustomItemBlock
{
    private static final VoxelShape CHALICE;
    
    public BlockChalice() {
        super(PropertiesMisc.defaultGoldMachinery().harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return BlockChalice.CHALICE;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World world, final BlockPos pos, final Player player, final Hand hand, final BlockRayTraceResult brtr) {
        final ItemStack interact = player.func_184586_b(hand);
        final TileChalice tc = MiscUtils.getTileAt((IBlockReader)world, pos, TileChalice.class, true);
        if (tc != null) {
            final IFluidHandlerItem handlerItem = (IFluidHandlerItem)FluidUtil.getFluidHandler(interact).orElse((Object)null);
            if (handlerItem != null) {
                if (!world.func_201670_d()) {
                    final FluidStack st = FluidUtil.getFluidContained(interact).orElse(FluidStack.EMPTY);
                    if (st.isEmpty()) {
                        final FluidActionResult far = FluidUtil.tryFillContainer(interact, tc.getTankAccess(), 1000, player, true);
                        if (far.isSuccess() && !player.func_184812_l_()) {
                            interact.shrink(1);
                            player.func_184611_a(hand, interact);
                            player.getInventory().func_191975_a(world, far.getResult());
                        }
                    }
                    else {
                        final FluidActionResult far = FluidUtil.tryEmptyContainer(interact, tc.getTankAccess(), 1000, player, true);
                        if (far.isSuccess() && !player.func_184812_l_()) {
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
    
    public int func_180641_l(final BlockState state, final World world, final BlockPos pos) {
        final TileChalice tc = MiscUtils.getTileAt((IBlockReader)world, pos, TileChalice.class, false);
        if (tc != null) {
            return MathHelper.func_76123_f(tc.getTank().getPercentageFilled() * 15.0f);
        }
        return 0;
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public BlockRenderType func_149645_b(final BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileChalice();
    }
    
    static {
        CHALICE = VoxelShapes.func_197873_a(0.125, 0.0, 0.125, 0.875, 0.875, 0.875);
    }
}
