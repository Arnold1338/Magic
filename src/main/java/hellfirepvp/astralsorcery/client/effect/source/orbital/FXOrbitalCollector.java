package hellfirepvp.astralsorcery.client.effect.source.orbital;

import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceOrbital;

public class FXOrbitalCollector extends FXSourceOrbital<FXFacingParticle, BatchRenderContext<FXFacingParticle>>
{
    private final VFXColorFunction<FXFacingParticle> primaryColor;
    private final VFXColorFunction<FXFacingParticle> secondaryColor;
    
    public FXOrbitalCollector(final Vector3 pos, final Color color) {
        super(pos, EffectTemplatesAS.GENERIC_PARTICLE);
        this.primaryColor = VFXColorFunction.constant(color);
        this.secondaryColor = VFXColorFunction.constant(color.brighter());
    }
    
    @Override
    public void spawnOrbitalParticle(final Vector3 pos, final Function<Vector3, FXFacingParticle> effectRegistrar) {
        if (FXOrbitalCollector.rand.nextInt(2) == 0) {
            effectRegistrar.apply(pos).color(this.primaryColor).setScaleMultiplier(0.15f).setMaxAge(15);
        }
        if (FXOrbitalCollector.rand.nextInt(5) == 0) {
            effectRegistrar.apply(pos).color(this.secondaryColor).setMotion(Vector3.random().normalize().multiply(0.02f + FXOrbitalCollector.rand.nextFloat() * 0.01f)).setScaleMultiplier(0.1f + FXOrbitalCollector.rand.nextFloat() * 0.1f).setMaxAge(25);
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingParticle> properties) {
    }
}
