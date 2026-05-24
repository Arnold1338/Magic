package hellfirepvp.astralsorcery.client.effect.source.orbital;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingAtlasParticle;
import hellfirepvp.astralsorcery.client.effect.source.FXSourceOrbital;

public class FXOrbitalInfuserLiquid extends FXSourceOrbital<FXFacingAtlasParticle, BatchRenderContext<FXFacingAtlasParticle>>
{
    private final FluidStack display;
    private final Vector3 infuserTarget;
    
    public FXOrbitalInfuserLiquid(final Vector3 pos, final FluidStack displayStack) {
        super(pos, EffectTemplatesAS.GENERIC_ATLAS_PARTICLE);
        this.display = displayStack.copy();
        this.infuserTarget = pos.clone().addY(0.75);
    }
    
    @Override
    public void spawnOrbitalParticle(final Vector3 pos, final Function<Vector3, FXFacingAtlasParticle> effectRegistrar) {
        final Vector3 motion = this.getPosition().subtract(pos).crossProduct(this.getOrbitAxis()).normalize().multiply(0.2 + FXOrbitalInfuserLiquid.rand.nextFloat() * 0.04);
        motion.add(this.getOrbitAxis().normalize().multiply(0.2 + FXOrbitalInfuserLiquid.rand.nextFloat() * 0.05));
        MiscUtils.applyRandomOffset(pos, FXOrbitalInfuserLiquid.rand, 0.15f);
        if (FXOrbitalInfuserLiquid.rand.nextInt(4) != 0) {
            effectRegistrar.apply(pos).setSprite(RenderingUtils.getParticleTexture(this.display)).selectFraction(0.2f).setScaleMultiplier(0.03f).color((fx, pTicks) -> new Color(ColorUtils.getOverlayColor(this.display))).alpha(VFXAlphaFunction.proximity(() -> this.infuserTarget, 2.0f)).motion(VFXMotionController.target(this.infuserTarget::clone, 0.08f)).setMotion(motion);
        }
        else {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setScaleMultiplier(0.15f).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.proximity(() -> this.infuserTarget, 2.0f)).motion(VFXMotionController.target(this.infuserTarget::clone, 0.08f)).setMotion(motion);
        }
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXFacingAtlasParticle> properties) {
    }
}
