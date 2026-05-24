package hellfirepvp.astralsorcery.client.effect.source.orbital;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceOrbital;

public class FXOrbitalPelotrio extends FXSourceOrbital<FXFacingParticle, BatchRenderContext<FXFacingParticle>>
{
    private static final VFXColorFunction<FXFacingParticle> pelotrioColor;
    
    public FXOrbitalPelotrio(final Vector3 pos) {
        super(pos, EffectTemplatesAS.GENERIC_PARTICLE);
    }
    
    @Override
    public void spawnOrbitalParticle(final Vector3 pos, final Function<Vector3, FXFacingParticle> effectRegistrar) {
        if (FXOrbitalPelotrio.rand.nextInt(4) == 0) {
            effectRegistrar.apply(pos).color(FXOrbitalPelotrio.pelotrioColor).setScaleMultiplier(0.3f + FXOrbitalPelotrio.rand.nextFloat() * 0.2f).setMotion(new Vector3(FXOrbitalPelotrio.rand.nextFloat() * 0.02f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1), FXOrbitalPelotrio.rand.nextFloat() * 0.02f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1), FXOrbitalPelotrio.rand.nextFloat() * 0.02f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1))).setMaxAge(45);
        }
        if (FXOrbitalPelotrio.rand.nextInt(4) == 0) {
            effectRegistrar.apply(pos).color(VFXColorFunction.WHITE).setScaleMultiplier(0.3f + FXOrbitalPelotrio.rand.nextFloat() * 0.2f).setMotion(new Vector3(FXOrbitalPelotrio.rand.nextFloat() * 0.025f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1), FXOrbitalPelotrio.rand.nextFloat() * 0.025f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1), FXOrbitalPelotrio.rand.nextFloat() * 0.025f * (FXOrbitalPelotrio.rand.nextBoolean() ? 1 : -1))).setMaxAge(30);
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingParticle> properties) {
    }
    
    static {
        pelotrioColor = VFXColorFunction.constant(ColorsAS.CONSTELLATION_PELOTRIO);
    }
}
