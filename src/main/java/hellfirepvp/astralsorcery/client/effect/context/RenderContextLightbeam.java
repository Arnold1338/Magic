package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextLightbeam extends BatchRenderContext<FXLightbeam>
{
    public RenderContextLightbeam(final SpriteSheetResource sprite) {
        super(sprite, RenderTypesAS.EFFECT_FX_LIGHTBEAM, (ctx, pos) -> new FXLightbeam(pos).alpha(VFXAlphaFunction.PYRAMID));
    }
}
