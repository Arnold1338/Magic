package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCrystal;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextCrystal extends BatchRenderContext<FXCrystal>
{
    public RenderContextCrystal(final AbstractRenderableTexture resource) {
        super(resource, RenderTypesAS.EFFECT_FX_CRYSTAL, (ctx, pos) -> new FXCrystal(pos));
    }
}
