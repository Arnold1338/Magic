package hellfirepvp.astralsorcery.client.effect.function.impl;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Random;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXRenderOffsetFunction;

public class RenderOffsetNoisePlane implements VFXRenderOffsetFunction<EntityVisualFX>
{
    private static final String KEY_PLANE_DATA = "plane";
    private static final Random T_RAND;
    private static final int SAMPLE_TIME_MIN = 35;
    private static final int SAMPLE_TIME_MAX = 55;
    private long lastSample;
    private long targetSample;
    private final float ringSize;
    private Vector3 prevRotationDeg;
    private Vector3 rotationDeg;
    
    public RenderOffsetNoisePlane(final float ringSizeDiameter) {
        this.ringSize = ringSizeDiameter;
        this.buildRotations();
        this.lastSample = ClientScheduler.getClientTick() - this.randomSampleTime();
        this.targetSample = this.lastSample + this.randomSampleTime();
    }
    
    public FXFacingParticle createParticle(final Vector3 position) {
        final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(position).color(VFXColorFunction.WHITE).renderOffset(this);
        p.getOrCreateData("plane", () -> new PlanarRotationData(RenderOffsetNoisePlane.T_RAND.nextFloat() * 360.0f, this.ringSize * 0.9f + RenderOffsetNoisePlane.T_RAND.nextFloat() * this.ringSize * 0.2f));
        return p;
    }
    
    private Vector3 getCurrentRotationDegree(final float partial) {
        this.checkRotations();
        final long current = ClientScheduler.getClientTick();
        final double perc = 1.0 - (this.targetSample - current - partial) / (double)(this.targetSample - this.lastSample);
        return this.interpolateRotation(perc, this.prevRotationDeg, this.rotationDeg);
    }
    
    private void checkRotations() {
        if (ClientScheduler.getClientTick() >= this.targetSample) {
            this.buildRotations();
        }
    }
    
    private void buildRotations() {
        this.lastSample = ClientScheduler.getClientTick();
        if (this.rotationDeg != null) {
            this.prevRotationDeg = this.rotationDeg;
        }
        else {
            this.prevRotationDeg = Vector3.positiveYRandom();
        }
        this.rotationDeg = Vector3.positiveYRandom();
        this.targetSample = this.lastSample + this.randomSampleTime();
    }
    
    private int randomSampleTime() {
        return RenderOffsetNoisePlane.T_RAND.nextInt(55);
    }
    
    private Vector3 interpolateRotation(final double partial, final Vector3 vZero, final Vector3 vOne) {
        double v = 20.0 * Mth.func_151237_a(partial, 0.0, 1.0) - 10.0;
        v = Mth.func_151237_a(Math.atan(v) / 2.9423 + 0.5, 0.0, 1.0);
        return this.getInterpolatedVectorRotation((float)v, vZero, vOne);
    }
    
    private Vector3 getInterpolatedVectorRotation(final float percent, final Vector3 vZero, final Vector3 vOne) {
        return new Vector3(RenderingVectorUtils.interpolate(vZero.getX(), vOne.getX(), percent), RenderingVectorUtils.interpolate(vZero.getY(), vOne.getY(), percent), RenderingVectorUtils.interpolate(vZero.getZ(), vOne.getZ(), percent));
    }
    
    @Nonnull
    @Override
    public Vector3 changeRenderPosition(@Nonnull final EntityVisualFX fx, final Vector3 interpolatedPos, final float pTicks) {
        final PlanarRotationData data = fx.getData("plane");
        if (data == null) {
            return interpolatedPos;
        }
        final Vector3 angle = this.getCurrentRotationDegree(pTicks);
        final Vector3 v = angle.clone().perpendicular().normalize().multiply(data.initialDistance);
        v.rotate(Math.toRadians(data.degreeRotation), angle);
        return interpolatedPos.add(v);
    }
    
    static {
        T_RAND = new Random();
    }
    
    private static class PlanarRotationData
    {
        private final float degreeRotation;
        private final float initialDistance;
        
        private PlanarRotationData(final float degreeRotation, final float initialDistance) {
            this.degreeRotation = degreeRotation;
            this.initialDistance = initialDistance;
        }
    }
}
