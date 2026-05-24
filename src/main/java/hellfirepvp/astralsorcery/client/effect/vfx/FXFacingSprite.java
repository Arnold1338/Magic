package hellfirepvp.astralsorcery.client.effect.vfx;

import java.awt.Color;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.effect.EntityDynamicFX;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXFacingSprite extends EntityVisualFX implements EntityDynamicFX
{
    private SpriteSheetResource sprite;
    private float spriteDisplayFactor;
    
    public FXFacingSprite(final Vector3 pos) {
        super(pos);
        this.sprite = null;
        this.spriteDisplayFactor = 1.0f;
    }
    
    public FXFacingSprite setSprite(final SpriteSheetResource sprite) {
        this.sprite = sprite;
        return this;
    }
    
    public FXFacingSprite setSpriteDisplayFactor(final float spriteDisplayFactor) {
        this.spriteDisplayFactor = spriteDisplayFactor;
        return this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
    }
    
    @Override
    public <T extends EntityVisualFX & EntityDynamicFX> void renderNow(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IDrawRenderTypeBuffer drawBuffer, final float pTicks) {
        final SpriteSheetResource ssr = (this.sprite != null) ? this.sprite : ctx.getSprite();
        final Tuple<Float, Float> uvOffset = ssr.getUVOffset(this, pTicks, this.spriteDisplayFactor);
        final int alpha = this.getAlpha(pTicks);
        final Color col = this.getColor(pTicks);
        final Vector3 vec = this.getRenderPosition(pTicks);
        final float scale = this.getScale(pTicks);
        final RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(ctx.getRenderType(), ssr::bindTexture, BlockAtlasTexture.getInstance()::bindTexture);
        final VertexConsumer buf = drawBuffer.getBuffer((RenderType)decorated);
        RenderingDrawUtils.renderFacingQuadVB(buf, renderStack, vec.getX(), vec.getY(), vec.getZ(), scale, 0.0f, (float)uvOffset.func_76341_a(), (float)uvOffset.func_76340_b(), ssr.getULength(), ssr.getVLength(), col.getRed(), col.getGreen(), col.getBlue(), alpha);
        drawBuffer.draw();
    }
}
