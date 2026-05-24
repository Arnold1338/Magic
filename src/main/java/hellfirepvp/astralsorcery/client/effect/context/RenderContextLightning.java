package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextLightning extends BatchRenderContext<FXLightning>
{
    public RenderContextLightning() {
        super(TexturesAS.TEX_LIGHTNING_PART, RenderTypesAS.EFFECT_FX_LIGHTNING, (ctx, pos) -> new FXLightning(pos));
    }
}
