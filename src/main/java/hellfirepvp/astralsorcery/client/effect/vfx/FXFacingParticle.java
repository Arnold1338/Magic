package hellfirepvp.astralsorcery.client.effect.vfx;

import net.minecraft.util.Tuple;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXFacingParticle extends EntityVisualFX
{
    public FXFacingParticle(final Vector3 pos) {
        super(pos);
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IVertexBuilder vb, final float pTicks) {
        final SpriteSheetResource ssr = ctx.getSprite();
        final Vector3 vec = this.getRenderPosition(pTicks);
        final int alpha = this.getAlpha(pTicks);
        final float fScale = this.getScale(pTicks);
        final Color col = this.getColor(pTicks);
        final Tuple<Float, Float> uvOffset = ssr.getUVOffset(this.getAge());
        RenderingDrawUtils.renderFacingQuadVB(vb, renderStack, vec.getX(), vec.getY(), vec.getZ(), fScale, 0.0f, (float)uvOffset.func_76341_a(), (float)uvOffset.func_76340_b(), ssr.getULength(), ssr.getVLength(), col.getRed(), col.getGreen(), col.getBlue(), alpha);
    }
}
