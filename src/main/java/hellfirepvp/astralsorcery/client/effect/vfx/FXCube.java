package hellfirepvp.astralsorcery.client.effect.vfx;

import java.awt.Color;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.util.LightmapUtil;
import org.joml.Vector3f;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import hellfirepvp.astralsorcery.client.effect.EntityDynamicFX;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXCube extends EntityVisualFX implements EntityDynamicFX
{
    private TextureAtlasSprite tas;
    private Vector3 rotationDegreeAxis;
    private Vector3 prevRotationDegreeAxis;
    private Vector3 rotationChange;
    private float tumbleIntensityMultiplier;
    private float textureSubSizePercentage;
    
    public FXCube(final Vector3 pos) {
        super(pos);
        this.tas = null;
        this.rotationDegreeAxis = new Vector3();
        this.prevRotationDegreeAxis = new Vector3();
        this.rotationChange = new Vector3();
        this.tumbleIntensityMultiplier = 1.0f;
        this.textureSubSizePercentage = 1.0f;
    }
    
    public FXCube setTextureAtlasSprite(final TextureAtlasSprite tas) {
        this.tas = tas;
        return this;
    }
    
    public FXCube setTextureSubSizePercentage(final float textureSubSizePercentage) {
        this.textureSubSizePercentage = textureSubSizePercentage;
        return this;
    }
    
    public FXCube setTumbleIntensityMultiplier(final float tumbleIntensityMultiplier) {
        this.tumbleIntensityMultiplier = tumbleIntensityMultiplier;
        return this;
    }
    
    public FXCube tumble() {
        this.rotationDegreeAxis = Vector3.positiveYRandom().multiply(360);
        this.rotationChange = Vector3.random().multiply(12);
        return this;
    }
    
    public Vector3 getInterpolatedRotation(final float pTicks) {
        return new Vector3(RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getX(), this.rotationDegreeAxis.getX(), pTicks), RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getY(), this.rotationDegreeAxis.getY(), pTicks), RenderingVectorUtils.interpolate(this.prevRotationDegreeAxis.getZ(), this.rotationDegreeAxis.getZ(), pTicks));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.tumbleIntensityMultiplier > 0.0f && this.rotationChange.lengthSquared() > 0.0) {
            final Vector3 degAxis = this.rotationDegreeAxis.clone();
            final Vector3 modify = this.rotationChange.clone().multiply(this.tumbleIntensityMultiplier);
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.clone();
            this.rotationDegreeAxis.add(modify);
            final Vector3 newDegAxis = this.rotationDegreeAxis;
            newDegAxis.setX(newDegAxis.getX() % 360.0).setY(newDegAxis.getY() % 360.0).setZ(newDegAxis.getZ() % 360.0);
            if (!degAxis.add(modify).equals(newDegAxis)) {
                this.prevRotationDegreeAxis = this.rotationDegreeAxis.clone().subtract(modify);
            }
        }
        else {
            this.prevRotationDegreeAxis = this.rotationDegreeAxis.clone();
        }
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
    }
    
    @Override
    public <T extends EntityVisualFX & EntityDynamicFX> void renderNow(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IDrawRenderTypeBuffer drawBuffer, final float pTicks) {
        float u;
        float v;
        float uLength;
        float vLength;
        if (this.tas != null) {
            u = this.tas.func_94209_e();
            v = this.tas.func_94206_g();
            uLength = (this.tas.func_94212_f() - u) * this.textureSubSizePercentage;
            vLength = (this.tas.func_94210_h() - v) * this.textureSubSizePercentage;
        }
        else {
            final SpriteSheetResource ssr = ctx.getSprite();
            final Tuple<Float, Float> uv = ssr.getUVOffset(this.getAge());
            u = (float)uv.getA();
            v = (float)uv.getB();
            uLength = ssr.getULength() * this.textureSubSizePercentage;
            vLength = ssr.getVLength() * this.textureSubSizePercentage;
        }
        final int alpha = this.getAlpha(pTicks);
        final Color c = this.getColor(pTicks);
        final Vector3 translateTo = this.getRenderPosition(pTicks).subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
        final Vector3 rotation = this.getInterpolatedRotation(pTicks);
        final float scale = this.getScale(pTicks);
        renderStack.popPose();
        renderStack.translate(translateTo.getX(), translateTo.getY(), translateTo.getZ());
        renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()(float)rotation.getX()));
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()(float)rotation.getY()));
        renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).getMultiBufferSource()(float)rotation.getZ()));
        renderStack.translate(scale, scale, scale);
        final VertexConsumer buf = drawBuffer.getBuffer(ctx.getRenderType());
        RenderingDrawUtils.renderTexturedCubeCentralColorLighted(buf, renderStack, u, v, uLength, vLength, c.getRed(), c.getGreen(), c.getBlue(), alpha, LightmapUtil.getPackedFullbrightCoords());
        renderStack.popPose();
    }
}
