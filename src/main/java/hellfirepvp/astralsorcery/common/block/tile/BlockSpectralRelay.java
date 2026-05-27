package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesGlass;
import net.minecraft.world.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public class BlockSpectralRelay extends BlockStarlightNetwork implements CustomItemBlock
{
    private static final VoxelShape RELAY;
    
    public BlockSpectralRelay() {
        super(PropertiesGlass.coatedGlass().func_235838_a_(state -> 4));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext context) {
        return BlockSpectralRelay.RELAY;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.level()) {
            final ItemStack held = player.getItemInHand(hand);
            final TileSpectralRelay tar = MiscUtils.getTileAt((IBlockReader)world, pos, TileSpectralRelay.class, true);
            if (tar != null) {
                final TileInventory inv = tar.getInventory();
                if (!held.isEmpty()) {
                    if (!inv.getStackInSlot(0).isEmpty()) {
                        final ItemStack stack = inv.getStackInSlot(0);
                        player.getInventory().func_191975_a(world, stack);
                        inv.setStackInSlot(0, ItemStack.EMPTY);
                        tar.markForUpdate();
                        TileSpectralRelay.cascadeRelayProximityUpdates(world, pos);
                    }
                    if (!world.isEmptyBlock(pos.above())) {
                        return InteractionResult.PASS;
                    }
                    inv.setStackInSlot(0, ItemUtils.copyStackWithSize(held, 1));
                    world.func_184148_a((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.field_187638_cR, SoundSource.PLAYERS, 0.2f, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    if (!player.getVehicle()) {
                        held.shrink(1);
                    }
                    tar.updateAltarLinkState();
                    TileSpectralRelay.cascadeRelayProximityUpdates(world, pos);
                    tar.markForUpdate();
                }
                else if (!inv.getStackInSlot(0).isEmpty()) {
                    final ItemStack stack = inv.getStackInSlot(0);
                    player.getInventory().func_191975_a(world, stack);
                    inv.setStackInSlot(0, ItemStack.EMPTY);
                    TileSpectralRelay.cascadeRelayProximityUpdates(world, pos);
                    tar.markForUpdate();
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void func_196243_a(final BlockState state, final Level worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        super.func_196243_a(state, worldIn, pos, newState, isMoving);
        if (!worldIn.level()) {
            TileSpectralRelay.cascadeRelayProximityUpdates(worldIn, pos);
        }
    }
    
    public BlockState func_196271_a(final BlockState state, final Direction placedAgainst, final BlockState facingState, final IWorld world, final BlockPos pos, final BlockPos facingPos) {
        if (!this.func_196260_a(state, (IWorldReader)world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }
    
    public boolean func_196260_a(final BlockState state, final IWorldReader world, final BlockPos pos) {
        return func_220064_c((IBlockReader)world, pos.renderItem());
    }
    
    public boolean func_149740_M(final BlockState p_149740_1_) {
        return true;
    }
    
    public int func_180641_l(final BlockState state, final Level world, final BlockPos pos) {
        final TileSpectralRelay tsr = MiscUtils.getTileAt((IBlockReader)world, pos, TileSpectralRelay.class, false);
        if (tsr != null) {
            return tsr.getInventory().getStackInSlot(0).isEmpty() ? 0 : 15;
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
        return new TileSpectralRelay();
    }
    
    static {
        RELAY = Block.of(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);
    }
}
