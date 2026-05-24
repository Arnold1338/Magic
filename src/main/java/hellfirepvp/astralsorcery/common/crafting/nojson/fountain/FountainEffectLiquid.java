package hellfirepvp.astralsorcery.common.crafting.nojson.fountain;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.capability.ChunkFluidEntry;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.BlockDropCaptureAssist;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import hellfirepvp.astralsorcery.common.lib.CapabilitiesAS;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.tile.TileFountain;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrime;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FountainEffectLiquid extends FountainEffect<LiquidContext>
{
    public FountainEffectLiquid() {
        super(AstralSorcery.key("effect_liquid"));
    }
    
    @Nonnull
    @Override
    public BlockFountainPrime getAssociatedPrime() {
        return BlocksAS.FOUNTAIN_PRIME_LIQUID;
    }
    
    @Nonnull
    @Override
    public LiquidContext createContext(final TileFountain fountain) {
        return new LiquidContext(fountain.func_174877_v());
    }
    
    @Override
    public void tick(final TileFountain fountain, final LiquidContext ctx, final int operationTick, final LogicalSide side, final OperationSegment currentSegment) {
        if (side.isClient()) {
            this.tickEffects(fountain, ctx, operationTick, currentSegment);
            return;
        }
        if (currentSegment.isLaterOrEqualTo(OperationSegment.RUNNING)) {
            final World w = fountain.func_145831_w();
            if (fountain.getTicksExisted() % 32 == 0) {
                this.digCone(w, ctx);
            }
            if (ctx.tickLiquidProduction()) {
                ctx.resetLiquidProductionTick(FountainEffectLiquid.rand);
                this.produceLiquid(fountain);
            }
        }
    }
    
    private void produceLiquid(final TileFountain fountain) {
        final Chunk ch = fountain.func_145831_w().func_175726_f(fountain.func_174877_v());
        ch.getCapability((Capability)CapabilitiesAS.CHUNK_FLUID).ifPresent(entry -> {
            final int drain = 200 + FountainEffectLiquid.rand.nextInt(400);
            FluidStack drained;
            if (!entry.isEmpty() && entry.isInitialized()) {
                drained = entry.drain(drain, IFluidHandler.FluidAction.SIMULATE);
            }
            else {
                drained = new FluidStack((Fluid)Fluids.field_204546_a, drain);
            }
            final int fillable = fountain.getTank().fill(drained, IFluidHandler.FluidAction.SIMULATE);
            if (fillable > 0) {
                final FluidStack actual = entry.drain(fillable, IFluidHandler.FluidAction.EXECUTE);
                fountain.getTank().fill(actual, IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }
    
    private void digCone(final World world, final LiquidContext ctx) {
        if (world instanceof ServerLevel) {
            this.dig((ServerLevel)world, ctx.getDigPositions());
        }
    }
    
    private void dig(final ServerLevel world, final List<BlockPos> positions) {
        BlockDropCaptureAssist.startCapturing();
        try {
            positions.forEach(pos -> MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                final BlockState state = world.getBlockState(pos);
                if (!state.isAir((IBlockReader)world, pos) && world.func_175625_s(pos) == null && state.func_185887_b((IBlockReader)world, pos) >= 0.0f && !BlockUtils.isFluidBlock(state)) {
                    BlockUtils.breakBlockWithoutPlayer(world, pos, state, ItemStack.EMPTY, true, true, false);
                }
            }));
        }
        finally {
            BlockDropCaptureAssist.getCapturedStacksAndStop();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffects(final TileFountain fountain, final LiquidContext ctx, final int operationTick, final OperationSegment currentSegment) {
        if (currentSegment.isLaterOrEqualTo(OperationSegment.STARTUP)) {
            FXSpritePlane sprite = (FXSpritePlane)ctx.fountainSprite;
            if (sprite == null) {
                sprite = EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(new Vector3(fountain).add(0.5, 0.5, 0.5)).setAxis(Vector3.RotAxis.Y_AXIS).setNoRotation(45.0f).setSprite(SpritesAS.SPR_FOUNTAIN_LIQUID).setAlphaMultiplier(1.0f).alpha((fx, alphaIn, pTicks) -> this.getSegmentPercent(OperationSegment.STARTUP, fountain.getTickActiveFountainEffect())).setScaleMultiplier(5.5f).refresh(RefreshFunction.tileExistsAnd(fountain, (tile, fx) -> tile.getCurrentEffect() == this));
            }
            else if (sprite.isRemoved() || sprite.canRemove()) {
                EffectHelper.refresh(sprite, EffectTemplatesAS.TEXTURE_SPRITE);
            }
            ctx.fountainSprite = sprite;
        }
        final BlockPos fountainPos = fountain.func_174877_v();
        final float segmentPercent = this.getSegmentPercent(currentSegment, operationTick);
        switch (currentSegment) {
            case STARTUP: {
                this.playFountainVortexParticles((Vector3i)fountainPos, segmentPercent);
                this.playFountainArcs((Vector3i)fountainPos, segmentPercent);
                break;
            }
            case PREPARATION: {
                this.playFountainArcs((Vector3i)fountainPos, 1.0f - segmentPercent);
                this.playFountainVortexParticles((Vector3i)fountainPos, 1.0f - segmentPercent);
                this.playDigPreparation((Vector3i)fountainPos, segmentPercent);
                break;
            }
            case RUNNING: {
                this.playFountainVortexParticles((Vector3i)fountainPos, 0.2f);
                this.playFountainArcs((Vector3i)fountainPos, 0.6f);
                this.playDigParticles((Vector3i)fountainPos);
                if (fountain.getTicksExisted() % 40 == 0) {
                    this.playDigLightbeam((Vector3i)fountainPos);
                    break;
                }
                break;
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playDigPreparation(final Vector3i pos, final float chance) {
        final Vector3 at = new Vector3(pos).add(0.5, 0.5, 0.5);
        for (int i = 0; i < 12; ++i) {
            if (FountainEffectLiquid.rand.nextFloat() < chance) {
                final Vector3 particlePos = new Vector3(pos.getX() - 0.4 + FountainEffectLiquid.rand.nextFloat() * 1.8, pos.getY() - FountainEffectLiquid.rand.nextFloat() * 3.0f, pos.getZ() - 0.4 + FountainEffectLiquid.rand.nextFloat() * 1.8);
                final Vector3 motion = particlePos.clone().vectorFromHereTo(at).normalize().divide(30.0);
                final EntityVisualFX fx = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(particlePos).setMotion(motion).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectLiquid.rand.nextFloat() * 0.1f).setMaxAge(20 + FountainEffectLiquid.rand.nextInt(40));
                if (FountainEffectLiquid.rand.nextBoolean()) {
                    fx.color(VFXColorFunction.WHITE);
                }
                else {
                    fx.color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE));
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playDigParticles(final Vector3i pos) {
        for (int i = 0; i < 2; ++i) {
            final Vector3 at = new Vector3(pos).add(0.3 + FountainEffectLiquid.rand.nextFloat() * 0.4, -FountainEffectLiquid.rand.nextFloat() * 1.7, 0.3 + FountainEffectLiquid.rand.nextFloat() * 0.4);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setScaleMultiplier(0.25f).setAlphaMultiplier(1.0f).setMotion(new Vector3(0.0f, -FountainEffectLiquid.rand.nextFloat() * 0.008f, 0.0f)).color(VFXColorFunction.random());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playDigLightbeam(final Vector3i pos) {
        final Vector3 from = new Vector3(pos).add(0.5, 1.5, 0.5);
        MiscUtils.applyRandomOffset(from, FountainEffectLiquid.rand, 0.1f);
        final Vector3 to = from.clone().setY(0);
        final float size = 6.0f + FountainEffectLiquid.rand.nextFloat() * 2.0f;
        EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(to, size, size);
    }
    
    @Override
    public void transition(final TileFountain fountain, final LiquidContext ctx, final LogicalSide side, final OperationSegment prevSegment, final OperationSegment nextSegment) {
        if (side.isServer()) {
            if (nextSegment == OperationSegment.RUNNING) {
                this.digCone(fountain.func_145831_w(), ctx);
            }
        }
        else if (nextSegment == OperationSegment.RUNNING) {
            this.markDigProcess(fountain.func_174877_v());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void markDigProcess(final BlockPos pos) {
        for (int yy = 0; yy <= pos.getY(); ++yy) {
            for (int i = 0; i < 4; ++i) {
                final Vector3 at = new Vector3((Vector3i)pos).setY(yy).add(FountainEffectLiquid.rand.nextFloat(), FountainEffectLiquid.rand.nextFloat(), FountainEffectLiquid.rand.nextFloat());
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectLiquid.rand.nextFloat() * 0.1f).setMaxAge(20 + FountainEffectLiquid.rand.nextInt(40));
            }
        }
        final Vector3 from = new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5);
        final Vector3 to = from.clone().setY(0);
        EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(to, 1.5, 1.5).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.EFFECT_BLUE_LIGHT));
    }
    
    @Override
    public void onReplace(final TileFountain fountain, final LiquidContext ctx, @Nullable final FountainEffect<?> newEffect, final LogicalSide side) {
        if (side.isClient()) {
            this.removeSprite(ctx);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void removeSprite(final LiquidContext ctx) {
        final FXSpritePlane sprite = (FXSpritePlane)ctx.fountainSprite;
        if (sprite != null) {
            sprite.requestRemoval();
        }
    }
}
