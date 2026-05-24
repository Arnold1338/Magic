package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceLiquidFountain;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.function.VFXScaleFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import java.util.Random;

public class MiscPlayEffect
{
    private static final Random rand;
    
    @OnlyIn(Dist.CLIENT)
    public static void fireLightning(final PktPlayEffect effect) {
        final Vector3 start = ByteBufUtils.readVector(effect.getExtraData());
        final Vector3 end = ByteBufUtils.readVector(effect.getExtraData());
        Color color = Color.WHITE;
        if (effect.getExtraData().isReadable()) {
            color = new Color(effect.getExtraData().readInt(), true);
        }
        EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(start).makeDefault(end).color(VFXColorFunction.constant(color));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playSingleBlockTumbleDepthEffect(final Vector3 at, final BlockState displayState) {
        EffectHelper.of(EffectTemplatesAS.BLOCK_TRANSLUCENT_IGNORE_DEPTH).spawn(at.clone()).tumble().setBlockState(displayState).setMotion(new Vector3(0.0, 0.035, 0.0)).scale(VFXScaleFunction.SHRINK_EXP).setMaxAge(40 + MiscPlayEffect.rand.nextInt(10));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playTumbleBlockEffects(final PktPlayEffect event) {
        final BlockPos pos = ByteBufUtils.readPos(event.getExtraData());
        final BlockState state = ByteBufUtils.readBlockState(event.getExtraData());
        final Vector3 vec = new Vector3((Vector3i)pos).add(0.5f, 0.5f, 0.5f);
        playBlockParticles(state, pos);
        EffectHelper.of(EffectTemplatesAS.BLOCK_TRANSLUCENT).spawn(vec).tumble().setBlockState(state).setMotion(new Vector3(0.0, 0.035, 0.0)).scale(VFXScaleFunction.SHRINK_EXP).setMaxAge(20 + MiscPlayEffect.rand.nextInt(15));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playBlockEffects(final PktPlayEffect event) {
        final BlockPos pos = ByteBufUtils.readPos(event.getExtraData());
        final BlockState state = ByteBufUtils.readBlockState(event.getExtraData());
        playBlockParticles(state, pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void playBlockParticles(final BlockState state, final BlockPos pos) {
        RenderingUtils.playBlockBreakParticles(pos, null, state);
        final Vector3 vec = new Vector3((Vector3i)pos).add(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < 6; ++i) {
            final Vector3 at = vec.add(MiscPlayEffect.rand.nextFloat() * 0.1 * (MiscPlayEffect.rand.nextBoolean() ? 1 : -1), MiscPlayEffect.rand.nextFloat() * 0.1 * (MiscPlayEffect.rand.nextBoolean() ? 1 : -1), MiscPlayEffect.rand.nextFloat() * 0.1 * (MiscPlayEffect.rand.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setMotion(Vector3.random().multiply(0.045f)).setScaleMultiplier(0.25f + MiscPlayEffect.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void liquidFountain(final PktPlayEffect event) {
        final FluidStack stack = ByteBufUtils.readFluidStack(event.getExtraData());
        final Vector3 at = ByteBufUtils.readVector(event.getExtraData()).add(MiscPlayEffect.rand.nextFloat(), 0.0f, MiscPlayEffect.rand.nextFloat());
        EffectHelper.spawnSource(new FXSourceLiquidFountain(at, stack));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void catalystBurst(final PktPlayEffect event) {
        final Vector3 vec = ByteBufUtils.readVector(event.getExtraData());
        BatchRenderContext<? extends FXFacingParticle> ctx = null;
        switch (MiscPlayEffect.rand.nextInt(3)) {
            case 2: {
                ctx = EffectTemplatesAS.CRYSTAL_BURST_3;
                break;
            }
            case 1: {
                ctx = EffectTemplatesAS.CRYSTAL_BURST_2;
                break;
            }
            default: {
                ctx = EffectTemplatesAS.CRYSTAL_BURST_1;
                break;
            }
        }
        EffectHelper.of(ctx).spawn(vec).setScaleMultiplier(1.5f);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void gemCrystalBurst(final PktPlayEffect event) {
        final Vector3 vec = ByteBufUtils.readVector(event.getExtraData());
        final BlockGemCrystalCluster.GrowthStageType type = MiscUtils.getEnumEntry(BlockGemCrystalCluster.GrowthStageType.class, event.getExtraData().readInt());
        BatchRenderContext<? extends FXFacingParticle> ctx = EffectTemplatesAS.GEM_CRYSTAL_BURST;
        float scale = 0.5f;
        switch (type) {
            case STAGE_2_SKY: {
                ctx = EffectTemplatesAS.GEM_CRYSTAL_BURST_SKY;
                scale = 1.2f;
                break;
            }
            case STAGE_2_DAY: {
                ctx = EffectTemplatesAS.GEM_CRYSTAL_BURST_DAY;
                scale = 1.2f;
                break;
            }
            case STAGE_2_NIGHT: {
                ctx = EffectTemplatesAS.GEM_CRYSTAL_BURST_NIGHT;
                scale = 1.2f;
                break;
            }
        }
        EffectHelper.of(ctx).spawn(vec.add(0.5, 0.3, 0.5)).setScaleMultiplier(scale);
    }
    
    static {
        rand = new Random();
    }
}
