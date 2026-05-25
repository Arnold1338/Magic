package hellfirepvp.astralsorcery.client.effect.vfx;

import hellfirepvp.astralsorcery.client.render.ObjModelRender;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.resource.query.TextureQuery;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.effect.EntityDynamicFX;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXCrystal extends EntityVisualFX implements EntityDynamicFX
{
    private AbstractRenderableTexture alternativeTexture;
    private Color lightRayColor;
    private Vector3 rotation;
    
    public FXCrystal(final Vector3 pos) {
        super(pos);
        this.alternativeTexture = null;
        this.lightRayColor = null;
        this.rotation = new Vector3();
    }
    
    public FXCrystal rotation(final float x, final float y, final float z) {
        this.rotation = new Vector3(x, y, z);
        return this;
    }
    
    public FXCrystal setLightRayColor(final Color lightRayColor) {
        this.lightRayColor = lightRayColor;
        return this;
    }
    
    public FXCrystal setTexture(final TextureQuery query) {
        this.alternativeTexture = query.resolve();
        return this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
    }
    
    @Override
    public <T extends EntityVisualFX & EntityDynamicFX> void renderNow(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IDrawRenderTypeBuffer drawBuffer, final float pTicks) {
        if (this.alternativeTexture != null) {
            this.alternativeTexture.bindTexture();
        }
        final int alpha = this.getAlpha(pTicks);
        final Color c = this.getColor(pTicks);
        final Vector3 vec = this.getRenderPosition(pTicks).subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
        final float scale = this.getScale(pTicks);
        if (this.lightRayColor != null) {
            final long seed = 5863439008313086302L;
            renderStack.popPose();
            renderStack.func_227861_a_(vec.getX(), vec.getY(), vec.getZ());
            RenderingDrawUtils.renderLightRayFan(renderStack, (MultiBufferSource)drawBuffer, this.lightRayColor, seed, 5, 1.0f, 50);
            renderStack.scale();
            drawBuffer.draw();
        }
        renderStack.popPose();
        renderStack.func_227861_a_(vec.getX(), vec.getY() - 0.05000000074505806, vec.getZ());
        renderStack.translate(scale, scale, scale);
        renderStack.mulPose(Vector3f.field_229179_b_.getMultiBufferSource()(float)this.rotation.getX()));
        renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()(float)this.rotation.getY()));
        renderStack.mulPose(Vector3f.field_229183_f_.getMultiBufferSource()(float)this.rotation.getZ()));
        BufferDecoratorBuilder.withColor((r, g, b, a) -> new int[] { c.getRed(), c.getGreen(), c.getBlue(), alpha }).decorate(drawBuffer.getBuffer(ctx.getRenderType()), decorated -> ObjModelRender.renderCrystal(renderStack, decorated, drawBuffer::draw));
        renderStack.scale();
        if (this.alternativeTexture != null) {
            ctx.getSprite().bindTexture();
        }
    }
}
