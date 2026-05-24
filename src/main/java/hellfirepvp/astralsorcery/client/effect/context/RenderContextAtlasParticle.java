package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingAtlasParticle;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextAtlasParticle extends BatchRenderContext<FXFacingAtlasParticle>
{
    public RenderContextAtlasParticle() {
        super(RenderTypesAS.EFFECT_FX_GENERIC_PARTICLE_ATLAS, (ctx, pos) -> new FXFacingAtlasParticle(pos));
    }
}
