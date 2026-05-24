package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.BlockGetter;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockInventory;

public class BlockInfuser extends BlockInventory implements CustomItemBlock
{
    private static final VoxelShape INFUSER;
    
    public BlockInfuser() {
        super(PropertiesMarble.defaultMarble().harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        return BlockInfuser.INFUSER;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.isClientSide) {
            final ItemStack held = player.func_184586_b(hand);
            final TileInfuser ti = MiscUtils.getTileAt((IBlockReader)world, pos, TileInfuser.class, true);
            if (ti != null) {
                final ItemStack stored = ti.getItemInput();
                if (!held.isEmpty()) {
                    if (!stored.isEmpty()) {
                        player.getInventory().func_191975_a(world, stored);
                        ti.setItemInput(ItemStack.field_190927_a);
                        ti.markForUpdate();
                    }
                    if (!world.isEmptyBlock(pos.above())) {
                        return InteractionResult.PASS;
                    }
                    ti.setItemInput(ItemUtils.copyStackWithSize(held, 1));
                    world.func_184148_a((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.field_187638_cR, SoundSource.PLAYERS, 0.2f, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    if (!player.func_184812_l_()) {
                        held.shrink(1);
                    }
                    ti.markForUpdate();
                }
                else if (!stored.isEmpty()) {
                    player.getInventory().func_191975_a(world, stored);
                    ti.setItemInput(ItemStack.field_190927_a);
                    ti.markForUpdate();
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    public boolean func_149740_M(final BlockState p_149740_1_) {
        return true;
    }
    
    public int func_180641_l(final BlockState state, final World world, final BlockPos pos) {
        final TileInfuser ti = MiscUtils.getTileAt((IBlockReader)world, pos, TileInfuser.class, false);
        if (ti != null) {
            return ti.getItemInput().isEmpty() ? 0 : 15;
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
        return new TileInfuser();
    }
    
    static {
        INFUSER = VoxelShapes.func_197873_a(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
    }
}
