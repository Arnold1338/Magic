package hellfirepvp.astralsorcery.client.effect;

import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public interface EntityDynamicFX
{
     <T extends EntityVisualFX & EntityDynamicFX> void renderNow(final BatchRenderContext<T> p0, final PoseStack p1, final IDrawRenderTypeBuffer p2, final float p3);
}
