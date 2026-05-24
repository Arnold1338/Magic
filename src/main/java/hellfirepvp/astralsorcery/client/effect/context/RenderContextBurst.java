package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextBurst extends BatchRenderContext<FXFacingParticle>
{
    public RenderContextBurst(final SpriteSheetResource sprite) {
        super(sprite, RenderTypesAS.EFFECT_FX_BURST, (ctx, pos) -> new FXFacingParticle(pos));
    }
}
