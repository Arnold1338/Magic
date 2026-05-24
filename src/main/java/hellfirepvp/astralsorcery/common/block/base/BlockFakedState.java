package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.level.block.RenderShape;
import javax.annotation.Nonnull;
import net.minecraft.world.level.level.block.Blocks;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.level.entity.player.Player;
import com.google.common.collect.Lists;
import net.minecraft.world.level.item.ItemStack;
import java.util.List;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.level.block.SoundType;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.LevelReader;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.phys.HitResult;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.base.TileFakedState;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.world.level.block.state.StateContainer;
import net.minecraft.world.level.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.BaseEntityBlock;

public abstract class BlockFakedState extends BaseEntityBlock
{
    protected BlockFakedState(final AbstractBlock.Properties builder) {
        super(builder);
    }
    
    protected void func_206840_a(final StateContainer.Builder<Block, BlockState> builder) {
        super.func_206840_a((StateContainer.Builder)builder);
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void playParticles(final World world, final BlockPos pos, final Random rand) {
        if (rand.nextInt(8) == 0) {
            VFXColorFunction<?> colorFn = VFXColorFunction.WHITE;
            final TileFakedState fakedState = MiscUtils.getTileAt((IBlockReader)world, pos, TileFakedState.class, false);
            if (fakedState != null) {
                colorFn = VFXColorFunction.constant(fakedState.getOverlayColor());
            }
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.random().abs().add((Vector3i)pos)).alpha(VFXAlphaFunction.FADE_OUT).color(colorFn).setScaleMultiplier(0.2f + rand.nextFloat() * 0.05f).setMaxAge(25 + rand.nextInt(5));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(final BlockState state, final World world, final BlockPos pos, final ParticleEngine manager) {
        final BlockState fakeState = this.getFakedState((IBlockReader)world, pos);
        RenderingUtils.playBlockBreakParticles(pos, state, fakeState);
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(final BlockState state, final World worldObj, final HitResult target, final ParticleEngine manager) {
        return true;
    }
    
    public boolean addLandingEffects(final BlockState state1, final ServerLevel worldserver, final BlockPos pos, final BlockState state2, final LivingEntity entity, final int numberOfParticles) {
        return true;
    }
    
    public boolean addRunningEffects(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        return true;
    }
    
    public SoundType getSoundType(final BlockState state, final IWorldReader world, final BlockPos pos, @Nullable final Entity entity) {
        final BlockState fakeState = this.getFakedState((IBlockReader)world, pos);
        return fakeState.getSoundType(world, pos, entity);
    }
    
    public boolean canEntityDestroy(final BlockState state, final IBlockReader world, final BlockPos pos, final Entity entity) {
        return false;
    }
    
    public VoxelShape func_220053_a(final BlockState state, final IBlockReader world, final BlockPos pos, final CollisionContext context) {
        final BlockState fakeState = this.getFakedState(world, pos);
        return fakeState.func_215700_a(world, pos, context);
    }
    
    public List<ItemStack> func_220076_a(final BlockState state, final LootContext.Builder builder) {
        return Lists.newArrayList();
    }
    
    public AbstractBlock.OffsetType func_176218_Q() {
        return AbstractBlock.OffsetType.NONE;
    }
    
    public VoxelShape func_220071_b(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final CollisionContext context) {
        final BlockState fakeState = this.getFakedState(worldIn, pos);
        try {
            return fakeState.func_215685_b(worldIn, pos, context);
        }
        catch (final Exception ex) {
            return super.func_220071_b(state, worldIn, pos, context);
        }
    }
    
    public VoxelShape func_196247_c(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
        final BlockState fakeState = this.getFakedState(worldIn, pos);
        try {
            return fakeState.func_196951_e(worldIn, pos);
        }
        catch (final Exception ex) {
            return super.func_196247_c(state, worldIn, pos);
        }
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World world, final BlockPos pos, final Player player, final Hand handIn, final BlockHitResult hit) {
        final BlockState fakeState = this.getFakedState((IBlockReader)world, pos);
        try {
            return fakeState.func_227031_a_(world, player, handIn, hit);
        }
        catch (final Exception ex) {
            return super.func_225533_a_(state, world, pos, player, handIn, hit);
        }
    }
    
    public ItemStack getPickBlock(final BlockState state, final HitResult target, final IBlockReader world, final BlockPos pos, final Player player) {
        final BlockState fakeState = this.getFakedState(world, pos);
        try {
            return fakeState.getPickBlock(target, world, pos, player);
        }
        catch (final Exception ex) {
            return ItemStack.field_190927_a;
        }
    }
    
    @Nonnull
    private BlockState getFakedState(final IBlockReader world, final BlockPos pos) {
        final TileFakedState tb = MiscUtils.getTileAt(world, pos, TileFakedState.class, true);
        return (tb != null) ? tb.getFakedState() : Blocks.field_150350_a.defaultBlockState();
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.INVISIBLE;
    }
}
