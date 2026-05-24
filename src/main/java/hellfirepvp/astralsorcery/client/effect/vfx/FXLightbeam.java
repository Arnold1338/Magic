package hellfirepvp.astralsorcery.client.effect.vfx;

import com.mojang.math.Matrix4f;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXLightbeam extends EntityVisualFX
{
    private Vector3 from;
    private Vector3 to;
    private Vector3 aim;
    private Vector3 aimPerp;
    private double fromSize;
    private double toSize;
    
    public FXLightbeam(final Vector3 pos) {
        super(pos);
        this.from = pos;
    }
    
    public FXLightbeam setup(final Vector3 to, final double fromSize, final double toSize) {
        this.to = to;
        this.aim = to.clone().subtract(this.getPosition());
        this.aimPerp = this.aim.clone().perpendicular().normalize();
        this.fromSize = fromSize;
        this.toSize = toSize;
        return this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
        final Color c = this.getColor(pTicks);
        final int r = c.getRed();
        final int g = c.getGreen();
        final int b = c.getBlue();
        final int a = this.getAlpha(pTicks);
        final float scale = this.getScale(pTicks);
        final Vector3 renderOffset = RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks);
        this.renderCurrentTextureAroundAxis(vb, renderStack, ctx, renderOffset, Math.toRadians(0.0), scale, r, g, b, a);
        this.renderCurrentTextureAroundAxis(vb, renderStack, ctx, renderOffset, Math.toRadians(120.0), scale, r, g, b, a);
        this.renderCurrentTextureAroundAxis(vb, renderStack, ctx, renderOffset, Math.toRadians(240.0), scale, r, g, b, a);
    }
    
    private <T extends EntityVisualFX> void renderCurrentTextureAroundAxis(final VertexConsumer vb, final PoseStack renderStack, final BatchRenderContext<T> ctx, final Vector3 renderOffset, final double angle, final float scale, final int r, final int g, final int b, final int a) {
        final Vector3 perp = this.aimPerp.clone().rotate(angle, this.aim).normalize();
        final Vector3 perpTo = perp.clone().multiply(this.toSize * scale);
        final Vector3 perpFrom = perp.multiply(this.fromSize * scale);
        final SpriteSheetResource ssr = ctx.getSprite();
        final Tuple<Float, Float> uvOffset = ssr.getUVOffset(this.age);
        final float u = (float)uvOffset.func_76341_a();
        final float v = (float)uvOffset.func_76340_b();
        final float uWidth = ssr.getULength();
        final float vHeight = ssr.getVLength();
        final Matrix4f matr = renderStack.func_227866_c_().func_227870_a_();
        Vector3 vec = this.to.clone().add(perpTo.clone().multiply(-1)).subtract(renderOffset);
        vec.drawPos(matr, vb).func_225586_a_(r, g, b, a).func_225583_a_(u, v + vHeight).func_181675_d();
        vec = this.to.clone().add(perpTo).subtract(renderOffset);
        vec.drawPos(matr, vb).func_225586_a_(r, g, b, a).func_225583_a_(u + uWidth, v + vHeight).func_181675_d();
        vec = this.from.clone().add(perpFrom).subtract(renderOffset);
        vec.drawPos(matr, vb).func_225586_a_(r, g, b, a).func_225583_a_(u + uWidth, v).func_181675_d();
        vec = this.from.clone().add(perpFrom.clone().multiply(-1)).subtract(renderOffset);
        vec.drawPos(matr, vb).func_225586_a_(r, g, b, a).func_225583_a_(u, v).func_181675_d();
    }
}
