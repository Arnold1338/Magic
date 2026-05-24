package hellfirepvp.astralsorcery.client.effect.context;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class RenderContextFacingSprite extends BatchRenderContext<FXFacingSprite>
{
    public RenderContextFacingSprite() {
        super(new SpriteSheetResource(TexturesAS.TEX_BLANK), RenderTypesAS.EFFECT_FX_TEXTURE_SPRITE, (ctx, pos) -> new FXFacingSprite(pos));
    }
}
