package hellfirepvp.astralsorcery.client.effect.vfx;

import java.awt.Color;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityDynamicFX;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXSpritePlane extends EntityVisualFX implements EntityDynamicFX
{
    private float lastRenderDegree;
    private Vector3 axis;
    private int ticksPerFullRot;
    private float fixDegree;
    private float spriteDisplayFactor;
    private SpriteSheetResource sprite;
    
    public FXSpritePlane(final Vector3 pos) {
        super(pos);
        this.lastRenderDegree = 0.0f;
        this.axis = Vector3.RotAxis.Y_AXIS;
        this.ticksPerFullRot = 100;
        this.fixDegree = 0.0f;
        this.spriteDisplayFactor = 1.0f;
        this.sprite = null;
    }
    
    public FXSpritePlane setSprite(final AbstractRenderableTexture tex) {
        return this.setSprite(new SpriteSheetResource(tex));
    }
    
    public FXSpritePlane setSprite(final SpriteSheetResource sprite) {
        this.sprite = sprite;
        return this;
    }
    
    public FXSpritePlane setSpriteDisplayFactor(final float spriteDisplayFactor) {
        this.spriteDisplayFactor = spriteDisplayFactor;
        return this;
    }
    
    public FXSpritePlane setAxis(final Vector3 axis) {
        this.axis = axis.clone();
        return this;
    }
    
    public FXSpritePlane setTicksPerFullRotation(final int ticksPerFullRot) {
        this.ticksPerFullRot = ticksPerFullRot;
        return this;
    }
    
    public FXSpritePlane setNoRotation(final float fixedDregree) {
        this.ticksPerFullRot = -1;
        this.fixDegree = fixedDregree;
        return this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IVertexBuilder vb, final float pTicks) {
    }
    
    @Override
    public <T extends EntityVisualFX & EntityDynamicFX> void renderNow(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IDrawRenderTypeBuffer drawBuffer, final float pTicks) {
        final SpriteSheetResource ssr = (this.sprite != null) ? this.sprite : ctx.getSprite();
        final Tuple<Float, Float> uvOffset = ssr.getUVOffset(this, pTicks, this.spriteDisplayFactor);
        final Vector3 vec = this.getRenderPosition(pTicks);
        vec.subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
        final float scale = this.getScale(pTicks);
        final int alpha = this.getAlpha(pTicks);
        final Color color = this.getColor(pTicks);
        final Vector3 axis = this.axis.clone();
        float deg;
        if (this.ticksPerFullRot >= 0) {
            final float anglePercent = this.getAge() / (float)this.ticksPerFullRot;
            deg = anglePercent * 360.0f;
            deg = RenderingVectorUtils.interpolateRotation(this.lastRenderDegree, deg, pTicks);
            this.lastRenderDegree = deg;
        }
        else {
            deg = this.fixDegree;
        }
        final RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(ctx.getRenderType(), ssr::bindTexture, () -> BlockAtlasTexture.getInstance().bindTexture());
        final IVertexBuilder buf = drawBuffer.getBuffer((RenderType)decorated);
        RenderingDrawUtils.renderAngleRotatedTexturedRectVB(buf, renderStack, vec, axis, (float)Math.toRadians(deg), scale, (float)uvOffset.func_76341_a(), (float)uvOffset.func_76340_b(), ssr.getULength(), ssr.getVLength(), color.getRed(), color.getGreen(), color.getBlue(), alpha);
        drawBuffer.draw();
    }
}
