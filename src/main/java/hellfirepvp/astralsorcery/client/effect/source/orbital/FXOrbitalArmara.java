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

public class FXOrbitalArmara extends FXSourceOrbital<FXFacingParticle, BatchRenderContext<FXFacingParticle>>
{
    private static final VFXColorFunction<FXFacingParticle> armaraPrimary;
    private static final VFXColorFunction<FXFacingParticle> armaraSecondary;
    private int count;
    
    public FXOrbitalArmara(final Vector3 pos) {
        super(pos, EffectTemplatesAS.GENERIC_PARTICLE);
        this.count = 2 + FXOrbitalArmara.rand.nextInt(2);
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
        if (FXOrbitalArmara.rand.nextInt(2) == 0) {
            effectRegistrar.apply(pos).color(FXOrbitalArmara.armaraPrimary).setScaleMultiplier(0.25f).addPosition(new Vector3(FXOrbitalArmara.rand.nextFloat() * 0.01f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1), FXOrbitalArmara.rand.nextFloat() * 0.01f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1), FXOrbitalArmara.rand.nextFloat() * 0.01f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1))).setMaxAge(45);
        }
        if (FXOrbitalArmara.rand.nextInt(3) == 0) {
            effectRegistrar.apply(pos).color(FXOrbitalArmara.armaraSecondary).setScaleMultiplier(0.25f).setMotion(new Vector3(FXOrbitalArmara.rand.nextFloat() * 0.025f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1), FXOrbitalArmara.rand.nextFloat() * 0.025f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1), FXOrbitalArmara.rand.nextFloat() * 0.025f * (FXOrbitalArmara.rand.nextBoolean() ? 1 : -1))).setMaxAge(35);
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingParticle> properties) {
    }
    
    static {
        armaraPrimary = VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_ARMARA);
        armaraSecondary = VFXColorFunction.constant(ColorsAS.RITUAL_CONSTELLATION_ARMARA_SECONDARY);
    }
}
