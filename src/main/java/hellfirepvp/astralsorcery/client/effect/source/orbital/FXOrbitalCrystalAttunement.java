package hellfirepvp.astralsorcery.client.effect.source.orbital;

import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceOrbital;

public class FXOrbitalCrystalAttunement extends FXSourceOrbital<FXFacingParticle, BatchRenderContext<FXFacingParticle>>
{
    private final Vector3 targetPoint;
    private final IConstellation cst;
    
    public FXOrbitalCrystalAttunement(final Vector3 pos, final Vector3 floatTarget, final IConstellation constellation) {
        super(pos, EffectTemplatesAS.GENERIC_PARTICLE);
        this.targetPoint = floatTarget.clone();
        this.cst = constellation;
    }
    
    @Override
    public void spawnOrbitalParticle(final Vector3 pos, final Function<Vector3, FXFacingParticle> effectRegistrar) {
        final Vector3 motion = this.getPosition().subtract(pos).crossProduct(this.getOrbitAxis()).normalize().multiply(0.1 + FXOrbitalCrystalAttunement.rand.nextFloat() * 0.1);
        motion.add(this.getOrbitAxis().normalize().multiply(0.15 + FXOrbitalCrystalAttunement.rand.nextFloat() * 0.15));
        final Vector3 vortexPos = pos.clone();
        MiscUtils.applyRandomOffset(vortexPos, FXOrbitalCrystalAttunement.rand, 0.4f);
        final FXFacingParticle p = effectRegistrar.apply(vortexPos).color(VFXColorFunction.WHITE).setScaleMultiplier(0.2f + FXOrbitalCrystalAttunement.rand.nextFloat() * 0.1f).setAlphaMultiplier(0.75f).alpha(VFXAlphaFunction.proximity(this.targetPoint::clone, 3.0f)).motion(VFXMotionController.target(this.targetPoint::clone, 0.075f)).setMotion(motion).setMaxAge(60);
        if (FXOrbitalCrystalAttunement.rand.nextInt(3) == 0) {
            p.color(VFXColorFunction.constant(this.cst.getConstellationColor()));
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingParticle> properties) {
    }
}
