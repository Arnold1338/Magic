package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.phys.shapes.Shapes;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesWood;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BlockTelescope extends BaseEntityBlock implements CustomItemBlock
{
    private static final VoxelShape TELESCOPE;
    
    public BlockTelescope() {
        super(PropertiesWood.defaultInfusedWood());
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(final BlockState state, final Level world, final BlockPos pos, final ParticleEngine manager) {
        RenderingUtils.playBlockBreakParticles(pos.above(), BlocksAS.TELESCOPE.defaultBlockState(), BlocksAS.TELESCOPE.defaultBlockState());
        return false;
    }
    
    public VoxelShape func_220053_a(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final CollisionContext p_220053_4_) {
        return BlockTelescope.TELESCOPE;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final Level world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult rayTraceResult) {
        if (world.level()) {
            AstralSorcery.getProxy().openGui(player, GuiType.TELESCOPE, pos);
        }
        return InteractionResult.SUCCESS;
    }
    
    public void func_180633_a(final Level world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        world.func_175656_a(pos.above(), (BlockState)BlocksAS.STRUCTURAL.defaultBlockState().setValue((Property)BlockStructural.BLOCK_TYPE, (Comparable)BlockStructural.BlockType.TELESCOPE));
        super.func_180633_a(world, pos, state, placer, stack);
    }
    
    public void func_220069_a(final BlockState state, final Level world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean isMoving) {
        if (world.isEmptyBlock(pos.above())) {
            world.func_217377_a(pos, isMoving);
        }
        super.func_220069_a(state, world, pos, block, fromPos, isMoving);
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileTelescope();
    }
    
    static {
        TELESCOPE = VoxelShapes.func_197873_a(0.0625, 0.0, 0.0625, 0.9375, 2.0, 0.9375);
    }
}
