package hellfirepvp.astralsorcery.common.crafting.nojson.fountain;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.effect.function.impl.RenderOffsetNoisePlane;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import java.util.Collection;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperEntityFreeze;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.data.config.registry.TechnicalEntityRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.tile.TileFountain;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.block.tile.fountain.BlockFountainPrime;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FountainEffectVortex extends FountainEffect<VortexContext>
{
    public FountainEffectVortex() {
        super(AstralSorcery.key("effect_vortex"));
    }
    
    @Nonnull
    @Override
    public BlockFountainPrime getAssociatedPrime() {
        return BlocksAS.FOUNTAIN_PRIME_VORTEX;
    }
    
    @Nonnull
    @Override
    public VortexContext createContext(final TileFountain fountain) {
        return new VortexContext();
    }
    
    @Override
    public void tick(final TileFountain fountain, final VortexContext context, final int operationTick, final LogicalSide side, final OperationSegment currentSegment) {
        if (side.isClient()) {
            this.tickEffects(fountain, context, operationTick, currentSegment);
        }
        else if (side.isServer() && currentSegment.isLaterOrEqualTo(OperationSegment.RUNNING)) {
            this.pullEntities(fountain);
        }
    }
    
    private void pullEntities(final TileFountain fountain) {
        final Vector3 at = new Vector3(fountain).add(0.5, 0.5, 0.5);
        final Vector3 vortexAt = at.clone().addY(-4.0);
        final AABB captureBox = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).func_186670_a(fountain.getBlockState().func_177979_c(4)).func_186662_g(2.0);
        final AABB pullBox = captureBox.func_186662_g(14.0);
        final float boxCapacity = 125.0f;
        float density = 0.0f;
        final List<LivingEntity> captured = fountain.getLevel().func_217357_a((Class)LivingEntity.class, captureBox);
        final Iterator<LivingEntity> iterator = captured.iterator();
        LivingEntity le = null;
        while (iterator.hasNext()) {
            le = iterator.next();
            if (le != null && le.isAlive() && !(le instanceof Player)) {
                if (!TechnicalEntityRegistry.INSTANCE.canAffect((Entity)le)) {

                }
                final float entitySize = le.func_213302_cg() * le.func_213311_cf() * le.func_213311_cf();
                density += entitySize;
                if (entitySize > boxCapacity) {
                    final Vector3 heldPos = vortexAt.clone().addY(-1.0);
                    if (heldPos.distanceSquared((Entity)le) >= 0.4000000059604645) {
                        le.func_70080_a(heldPos.getX(), heldPos.getY(), heldPos.getZ(), le.yRot, le.xRot);
                    }
                    if (le instanceof EnderDragonEntity) {
                        final GameRules rules = fountain.getLevel().func_82736_K();
                        final boolean prev = rules.func_223586_b(GameRules.field_223599_b);
                        ((GameRules.BooleanValue)rules.func_223585_a(GameRules.field_223599_b)).func_223570_a(false, (MinecraftServer)null);
                        le.func_70636_d();
                        ((GameRules.BooleanValue)rules.func_223585_a(GameRules.field_223599_b)).func_223570_a(prev, (MinecraftServer)null);
                    }
                }
                else {
                    le.func_213317_d(Vec3.field_186680_a);
                }
                EventHelperEntityFreeze.freeze((Entity)le);
            }
        }
        final float upkeep = Math.max(0.0f, density / boxCapacity);
        fountain.consumeLiquidStarlight(Mth.func_76123_f(upkeep / 3.0f));
        final List<LivingEntity> pulling = fountain.getLevel().func_217357_a((Class)LivingEntity.class, pullBox);
        pulling.removeAll(captured);
        for (final LivingEntity le2 : pulling) {
            if (le2 != null && le2.isAlive() && !(le2 instanceof Player)) {
                if (!TechnicalEntityRegistry.INSTANCE.canAffect((Entity)le2)) {

                }
                EventHelperEntityFreeze.freeze((Entity)le2);
                EntityUtils.applyVortexMotion(() -> Vector3.atEntityCorner((Entity)le), v -> {
                    if (le instanceof EnderDragonEntity) {
                        final Vector3 nextPos = Vector3.atEntityCorner((Entity)le).add(v);
                        if (le.func_70613_aW()) {
                            le.func_70634_a(nextPos.getX(), nextPos.getY(), nextPos.getZ());
                        }
                        else {
                            le.func_70080_a(nextPos.getX(), nextPos.getY(), nextPos.getZ(), le.yRot, le.xRot);
                        }
                        le.func_213317_d(Vec3.field_186680_a);
                    }
                    else {
                        le.func_213317_d(le.func_213322_ci().func_72441_c(v.getX(), v.getY() * 2.5, v.getZ()));
                        le.field_70133_I = true;
                    }

                }, vortexAt, 48.0, 3.0);
                if (vortexAt.distanceSquared((Entity)le2) > 16.0) {

                }
                final Vector3 randomRanges = new Vector3(Math.max(0.0, (captureBox.func_216364_b() - le2.func_213311_cf()) / 2.0), Math.max(0.0, (captureBox.func_216360_c() - le2.func_213302_cg()) / 2.0), Math.max(0.0, (captureBox.func_216362_d() - le2.func_213311_cf()) / 2.0));
                final Vector3 randomPos = vortexAt.clone().add(randomRanges.getX() * FountainEffectVortex.rand.nextFloat() * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), randomRanges.getY() * FountainEffectVortex.rand.nextFloat() * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), randomRanges.getZ() * FountainEffectVortex.rand.nextFloat() * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1));
                le2.func_70080_a(randomPos.getX(), randomPos.getY(), randomPos.getZ(), le2.yRot, le2.xRot);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffects(final TileFountain fountain, final VortexContext ctx, final int operationTick, final OperationSegment currentSegment) {
        if (currentSegment.isLaterOrEqualTo(OperationSegment.STARTUP)) {
            FXSpritePlane sprite = (FXSpritePlane)ctx.fountainSprite;
            if (sprite == null) {
                sprite = EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(new Vector3(fountain).add(0.5, 0.5, 0.5)).setAxis(Vector3.RotAxis.Y_AXIS).setNoRotation(45.0f).setSprite(SpritesAS.SPR_FOUNTAIN_VORTEX).setAlphaMultiplier(1.0f).alpha((fx, alphaIn, pTicks) -> this.getSegmentPercent(OperationSegment.STARTUP, fountain.getTickActiveFountainEffect())).setScaleMultiplier(5.5f).refresh(RefreshFunction.tileExistsAnd(fountain, (tile, fx) -> tile.getCurrentEffect() == this));
            }
            else if (sprite.isRemoved() || sprite.canRemove()) {
                EffectHelper.refresh(sprite, EffectTemplatesAS.TEXTURE_SPRITE);
            }
            ctx.fountainSprite = sprite;
        }
        final BlockPos fountainPos = fountain.getBlockState();
        final float segmentPercent = this.getSegmentPercent(currentSegment, operationTick);
        switch (currentSegment) {
            case STARTUP: {
                this.playFountainVortexParticles((Vec3i)fountainPos, segmentPercent);
                this.playFountainArcs((Vec3i)fountainPos, segmentPercent);
                this.playCoreParticles(fountainPos, segmentPercent);

            }
            case PREPARATION: {
                this.playFountainArcs((Vec3i)fountainPos, 1.0f - segmentPercent);
                this.playFountainVortexParticles((Vec3i)fountainPos, 1.0f - segmentPercent);
                this.playCoreParticles(fountainPos, 1.0f - segmentPercent * 2.0f);
                this.playCorePrimerParticles(fountainPos, segmentPercent);

            }
            case RUNNING: {
                this.playFountainVortexParticles((Vec3i)fountainPos, 0.2f);
                this.playFountainArcs((Vec3i)fountainPos, 0.6f);
                this.playFountainVortexLowerParticles(fountainPos);
                this.playVortexEffects(fountainPos, fountain, ctx);

            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playVortexEffects(final BlockPos pos, final TileFountain fountain, final VortexContext ctx) {
        final Vector3 at = new Vector3((Vec3i)pos).add(0.5, 0.5, 0.5);
        final Vector3 vortexAt = at.clone().addY(-4.0);
        FXFacingSprite sprite = (FXFacingSprite)ctx.facingVortexPlane;
        if (sprite == null) {
            sprite = EffectHelper.of(EffectTemplatesAS.FACING_SPRITE).spawn(vortexAt).setSprite(SpritesAS.SPR_ATTUNEMENT_FLARE).setAlphaMultiplier(1.0f).setScaleMultiplier(2.0f).refresh(RefreshFunction.tileExistsAnd(fountain, (tile, fx) -> tile.getCurrentEffect() == this));
        }
        else if (sprite.isRemoved() || sprite.canRemove()) {
            EffectHelper.refresh(sprite, EffectTemplatesAS.FACING_SPRITE);
        }
        ctx.facingVortexPlane = sprite;
        if (ctx.ctrlEffectNoise == null) {
            ctx.ctrlEffectNoise = Lists.newArrayList(new Object[] { new RenderOffsetNoisePlane(1.2f), new RenderOffsetNoisePlane(2.0f), new RenderOffsetNoisePlane(2.8f) });
        }
        for (final Object objPlane : ctx.ctrlEffectNoise) {
            final RenderOffsetNoisePlane plane = (RenderOffsetNoisePlane)objPlane;
            for (int i = 0; i < 3; ++i) {
                plane.createParticle(vortexAt).setMotion(Vector3.random().normalize().multiply(0.005f)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectVortex.rand.nextFloat() * 0.1f).setMaxAge(30 + FountainEffectVortex.rand.nextInt(15));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playFountainVortexLowerParticles(final BlockPos pos) {
        final Vector3 at = new Vector3((Vec3i)pos).add(0.5, 0.5, 0.5);
        final Vector3 coreAt = at.clone().addY(-1.15);
        final Vector3 vortexAt = at.clone().addY(-4.0);
        for (int i = 0; i < 2; ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(coreAt).setScaleMultiplier(0.25f).setAlphaMultiplier(1.0f).setMotion(Vector3.random().normalize().multiply(0.01f)).color(VFXColorFunction.random());
        }
        for (int i = 0; i < 3; ++i) {
            final Vector3 spawnPos = vortexAt.clone().add(Vector3.random().multiply(4.5f));
            final Vector3 dir = spawnPos.clone().vectorFromHereTo(vortexAt).normalize().divide(20 + FountainEffectVortex.rand.nextInt(10));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(spawnPos).setMotion(dir).setScaleMultiplier(0.2f + FountainEffectVortex.rand.nextFloat() * 0.1f).setAlphaMultiplier(1.0f).color(VFXColorFunction.WHITE);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playCorePrimerParticles(final BlockPos pos, final float chance) {
        final float yOffset = -3.5f * Math.min(1.0f, chance * 2.0f);
        for (int i = 0; i < 15; ++i) {
            final Vector3 at = new Vector3(pos.getX() + 0.5 - 0.10000000149011612 + FountainEffectVortex.rand.nextFloat() * 0.2, pos.getY() + yOffset - 0.1f + FountainEffectVortex.rand.nextFloat() * 0.2, pos.getZ() + 0.5 - 0.10000000149011612 + FountainEffectVortex.rand.nextFloat() * 0.2);
            final float mul = (chance <= 0.5f) ? 1.0f : (1.0f - chance);
            final Vector3 dir = new Vector3(FountainEffectVortex.rand.nextFloat() * 0.035 * mul * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), FountainEffectVortex.rand.nextFloat() * 0.035 * mul * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), FountainEffectVortex.rand.nextFloat() * 0.035 * mul * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setMotion(dir).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectVortex.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMaxAge(20 + FountainEffectVortex.rand.nextInt(40));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playCoreParticles(final BlockPos pos, final float chance) {
        final Vector3 at = new Vector3((Vec3i)pos).add(0.5, -0.5, 0.5);
        for (int i = 0; i < 18; ++i) {
            if (FountainEffectVortex.rand.nextFloat() < chance) {
                final Vector3 particlePos = new Vector3(pos.getX() - 1 + FountainEffectVortex.rand.nextFloat() * 3.0f, pos.getY() - 1.5 + FountainEffectVortex.rand.nextFloat() * 2.0f, pos.getZ() - 1 + FountainEffectVortex.rand.nextFloat() * 3.0f);
                final Vector3 motion = particlePos.clone().vectorFromHereTo(at).normalize().divide(30.0);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(particlePos).setMotion(motion).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectVortex.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMaxAge(20 + FountainEffectVortex.rand.nextInt(40));
            }
        }
    }
    
    @Override
    public void transition(final TileFountain fountain, final VortexContext context, final LogicalSide side, final OperationSegment prevSegment, final OperationSegment nextSegment) {
        if (side.isClient() && nextSegment == OperationSegment.RUNNING) {
            this.doVortexExplosion(fountain.getBlockState());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doVortexExplosion(final BlockPos pos) {
        for (int i = 0; i < 140; ++i) {
            final Vector3 at = new Vector3(pos.getX() + 0.5f - 0.1f + FountainEffectVortex.rand.nextFloat() * 0.2f, pos.getY() - 3.5f - 0.1f + FountainEffectVortex.rand.nextFloat() * 0.2f, pos.getZ() + 0.5f - 0.1f + FountainEffectVortex.rand.nextFloat() * 0.2f);
            final Vector3 dir = new Vector3(FountainEffectVortex.rand.nextFloat() * 0.15 * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), FountainEffectVortex.rand.nextFloat() * 0.15 * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1), FountainEffectVortex.rand.nextFloat() * 0.15 * (FountainEffectVortex.rand.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setMotion(dir).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + FountainEffectVortex.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMaxAge(20 + FountainEffectVortex.rand.nextInt(40));
        }
    }
    
    @Override
    public void onReplace(final TileFountain fountain, final VortexContext ctx, @Nullable final FountainEffect<?> newEffect, final LogicalSide side) {
        if (side.isClient()) {
            this.removeSprite(ctx);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void removeSprite(final VortexContext ctx) {
        final FXSpritePlane sprite = (FXSpritePlane)ctx.fountainSprite;
        if (sprite != null) {
            sprite.requestRemoval();
        }
    }
}
