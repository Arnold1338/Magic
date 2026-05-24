package hellfirepvp.astralsorcery.client.effect.source.orbital;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceOrbital;

public class FXOrbitalLucerna extends FXSourceOrbital<FXFacingParticle, BatchRenderContext<FXFacingParticle>>
{
    private static final VFXColorFunction<FXFacingParticle> lucernaColor;
    private int count;
    
    public FXOrbitalLucerna(final Vector3 pos) {
        super(pos, EffectTemplatesAS.GENERIC_PARTICLE);
        this.count = 2 + FXOrbitalLucerna.rand.nextInt(2);
        this.refresh(fx -> {
            --this.count;
            return this.count > 0;
        });
    }
    
    @Override
    public void tick() {
        super.tick();
        this.move(new Vector3(0.0, 0.05, 0.0));
    }
    
    @Override
    public void spawnOrbitalParticle(final Vector3 pos, final Function<Vector3, FXFacingParticle> effectRegistrar) {
        if (FXOrbitalLucerna.rand.nextInt(2) == 0) {
            effectRegistrar.apply(pos).color(FXOrbitalLucerna.lucernaColor).setScaleMultiplier(0.25f).addPosition(new Vector3(FXOrbitalLucerna.rand.nextFloat() * 0.01f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1), FXOrbitalLucerna.rand.nextFloat() * 0.01f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1), FXOrbitalLucerna.rand.nextFloat() * 0.01f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1))).setMaxAge(45);
        }
        if (FXOrbitalLucerna.rand.nextInt(3) == 0) {
            effectRegistrar.apply(pos).color(VFXColorFunction.WHITE).setScaleMultiplier(0.25f).setMotion(new Vector3(FXOrbitalLucerna.rand.nextFloat() * 0.025f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1), FXOrbitalLucerna.rand.nextFloat() * 0.025f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1), FXOrbitalLucerna.rand.nextFloat() * 0.025f * (FXOrbitalLucerna.rand.nextBoolean() ? 1 : -1))).setMaxAge(35);
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingParticle> properties) {
    }
    
    static {
        lucernaColor = VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_LUCERNA);
    }
}
