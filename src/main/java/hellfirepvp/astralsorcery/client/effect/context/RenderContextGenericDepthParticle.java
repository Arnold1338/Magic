package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextGenericDepthParticle extends BatchRenderContext<FXFacingParticle>
{
    private static final VFXColorFunction<FXFacingParticle> defaultColor;
    
    public RenderContextGenericDepthParticle() {
        super(TexturesAS.TEX_PARTICLE_SMALL, RenderTypesAS.EFFECT_FX_GENERIC_PARTICLE_DEPTH, (ctx, pos) -> new FXFacingParticle(pos).setScaleMultiplier(0.2f).setAlphaMultiplier(0.75f).alpha(VFXAlphaFunction.PYRAMID).color(RenderContextGenericDepthParticle.defaultColor));
    }
    
    static {
        defaultColor = VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE);
    }
}
