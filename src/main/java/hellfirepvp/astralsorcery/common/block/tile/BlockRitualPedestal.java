package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.VoxelUtils;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.observerlib.api.block.BlockStructureObserver;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockRitualPedestal extends BlockStarlightNetwork implements CustomItemBlock, BlockStructureObserver
{
    private final VoxelShape shape;
    
    public BlockRitualPedestal() {
        super(PropertiesMarble.defaultMarble().harvestLevel(1).harvestTool(ToolType.PICKAXE));
        this.shape = this.createShape();
    }
    
    protected VoxelShape createShape() {
        final VoxelShape m1 = Block.of(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        final VoxelShape m2 = Block.of(11.0, 2.0, 6.0, 15.0, 6.0, 10.0);
        final VoxelShape m3 = Block.of(12.0, 2.0, 12.0, 14.0, 7.0, 14.0);
        final VoxelShape m4 = Block.of(2.0, 2.0, 12.0, 4.0, 7.0, 14.0);
        final VoxelShape m5 = Block.of(2.0, 2.0, 12.0, 4.0, 7.0, 14.0);
        final VoxelShape m6 = Block.of(12.0, 2.0, 2.0, 14.0, 7.0, 4.0);
        final VoxelShape m7 = Block.of(2.0, 2.0, 2.0, 4.0, 7.0, 4.0);
        final VoxelShape m8 = Block.of(6.0, 2.0, 6.0, 10.0, 10.0, 10.0);
        final VoxelShape m9 = Block.of(2.0, 10.0, 2.0, 14.0, 12.0, 14.0);
        final VoxelShape m10 = Block.of(6.0, 2.0, 11.0, 10.0, 6.0, 15.0);
        final VoxelShape m11 = Block.of(6.0, 2.0, 1.0, 10.0, 6.0, 5.0);
        final VoxelShape m12 = Block.of(3.0, 12.0, 11.0, 5.0, 14.0, 13.0);
        final VoxelShape m13 = Block.of(1.0, 2.0, 6.0, 5.0, 6.0, 10.0);
        final VoxelShape m14 = Block.of(3.0, 12.0, 3.0, 5.0, 14.0, 5.0);
        final VoxelShape m15 = Block.of(11.0, 12.0, 3.0, 13.0, 14.0, 5.0);
        final VoxelShape m16 = Block.of(11.0, 12.0, 11.0, 13.0, 14.0, 13.0);
        final VoxelShape m17 = Block.of(11.0, 2.0, 11.0, 15.0, 8.0, 15.0);
        final VoxelShape m18 = Block.of(11.0, 2.0, 1.0, 15.0, 8.0, 5.0);
        final VoxelShape m19 = Block.of(1.0, 2.0, 11.0, 5.0, 8.0, 15.0);
        final VoxelShape m20 = Block.of(1.0, 2.0, 1.0, 5.0, 8.0, 5.0);
        return VoxelUtils.combineAll(BooleanOp.field_223244_o_, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20);
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult rtr) {
        if (world.level()) {
            return InteractionResult.SUCCESS;
        }
        final TileRitualPedestal pedestal = MiscUtils.getTileAt((IBlockReader)world, pos, TileRitualPedestal.class, true);
        if (pedestal == null) {
            return InteractionResult.PASS;
        }
        final ItemStack heldItem = player.getItemInHand(hand);
        final ItemStack in = pedestal.getCurrentCrystal();
        if (player.isCrouching()) {
            pedestal.tryPlaceCrystalInPedestal(ItemStack.EMPTY);
            if (player.getItemInHand(hand).isEmpty()) {
                player.func_184611_a(hand, in);
            }
            else {
                player.getInventory().func_191975_a(world, in);
            }
        }
        else {
            player.func_184611_a(hand, pedestal.tryPlaceCrystalInPedestal(heldItem));
        }
        return InteractionResult.SUCCESS;
    }
    
    public void func_220069_a(final BlockState state, final Level world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean isMoving) {
        super.func_220069_a(state, world, pos, block, fromPos, isMoving);
        final TileRitualPedestal te = MiscUtils.getTileAt((IBlockReader)world, pos, TileRitualPedestal.class, true);
        if (te != null && !world.level()) {
            final BlockPos toCheck = pos.above();
            final BlockState other = world.getBlockState(toCheck);
            if (Block.func_208061_a(other.func_196952_d((IBlockReader)world, pos), Direction.DOWN)) {
                ItemUtils.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, te.getCurrentCrystal());
                te.tryPlaceCrystalInPedestal(ItemStack.EMPTY);
            }
        }
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return this.shape;
    }
    
    public void func_180633_a(final Level world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        super.func_180633_a(world, pos, state, placer, stack);
        if (placer instanceof Player) {
            final TileRitualPedestal pedestal = MiscUtils.getTileAt((IBlockReader)world, pos, TileRitualPedestal.class, true);
            if (pedestal != null) {
                pedestal.setOwner(placer.getUUID());
            }
        }
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileRitualPedestal();
    }
}
