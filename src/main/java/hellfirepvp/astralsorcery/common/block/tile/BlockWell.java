package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraftforge.fluids.FluidActionResult;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.world.level.Container;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Mth;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefactionContext;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.WellLiquefaction;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileWell;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockWell extends BlockStarlightNetwork implements CustomItemBlock
{
    private final VoxelShape shape;
    
    public BlockWell() {
        super(PropertiesMarble.defaultMarble().harvestLevel(1).harvestTool(ToolType.PICKAXE));
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final VoxelShape footing = Block.of(1.0, 0.0, 1.0, 15.0, 2.0, 15.0);
        final VoxelShape floor = Block.of(3.0, 2.0, 3.0, 13.0, 4.0, 13.0);
        final VoxelShape basinFloor = Block.of(1.0, 4.0, 1.0, 15.0, 5.0, 15.0);
        final VoxelShape w1 = Block.of(1.0, 5.0, 1.0, 2.0, 16.0, 14.0);
        final VoxelShape w2 = Block.of(2.0, 5.0, 1.0, 15.0, 16.0, 2.0);
        final VoxelShape w3 = Block.of(14.0, 5.0, 2.0, 15.0, 16.0, 15.0);
        final VoxelShape w4 = Block.of(1.0, 5.0, 14.0, 14.0, 16.0, 15.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, footing, floor, basinFloor, w1, w2, w3, w4);
    }
    
    public VoxelShape func_220053_a(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final CollisionContext p_220053_4_) {
        return this.shape;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.level()) {
            final ItemStack heldItem = player.getItemInHand(hand);
            if (!heldItem.isEmpty()) {
                final TileWell tw = MiscUtils.getTileAt((IBlockReader)world, pos, TileWell.class, false);
                if (tw == null) {
                    return InteractionResult.PASS;
                }
                final WellLiquefaction entry = RecipeTypesAS.TYPE_WELL.findRecipe(new WellLiquefactionContext(heldItem));
                if (entry != null) {
                    final ItemStackHandler handle = tw.getInventory();
                    if (!handle.getStackInSlot(0).isEmpty()) {
                        return InteractionResult.PASS;
                    }
                    if (!world.isEmptyBlock(pos.above())) {
                        return InteractionResult.PASS;
                    }
                    handle.setStackInSlot(0, ItemUtils.copyStackWithSize(heldItem, 1));
                    world.func_184148_a((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.field_187638_cR, SoundSource.PLAYERS, 0.2f, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    if (!player.getVehicle()) {
                        heldItem.shrink(1);
                    }
                    if (heldItem.getCount() <= 0) {
                        player.func_184611_a(hand, ItemStack.EMPTY);
                    }
                }
                tw.getCapability((net.minecraftforge.common.capabilities.Capability<Object>)CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(handler -> {
                    final FluidActionResult far = FluidUtil.tryFillContainerAndStow(heldItem, handler, (IItemHandler)new InvWrapper((Container)player.getInventory()), 1000, player, true);
                    if (far.isSuccess()) {
                        player.func_184611_a(hand, far.getResult());
                        SoundHelper.playSoundAround(SoundEvents.field_187630_M, world, (Vector3i)pos, 1.0f, 1.0f);
                        tw.markForUpdate();
                    }
                });
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void func_196243_a(final BlockState state, final Level worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        final TileWell tw = MiscUtils.getTileAt((IBlockReader)worldIn, pos, TileWell.class, true);
        if (tw != null && !worldIn.isClientSide) {
            final ItemStack stack = tw.getInventory().getStackInSlot(0);
            if (!stack.isEmpty()) {
                tw.breakCatalyst();
            }
        }
        super.func_196243_a(state, worldIn, pos, newState, isMoving);
    }
    
    public boolean func_149740_M(final BlockState p_149740_1_) {
        return true;
    }
    
    public int func_180641_l(final BlockState state, final Level world, final BlockPos pos) {
        final TileWell tw = MiscUtils.getTileAt((IBlockReader)world, pos, TileWell.class, false);
        if (tw != null) {
            final int fluidPart = Mth.func_76123_f(tw.getTank().getPercentageFilled() * 8.0f);
            return tw.getCatalyst().isEmpty() ? fluidPart : (fluidPart + 7);
        }
        return 0;
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileWell();
    }
}
