package hellfirepvp.astralsorcery.client.effect.vfx;

import java.util.Iterator;
import com.mojang.math.Matrix4f;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.util.SphereBuilder;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXColorEffectSphere extends EntityVisualFX
{
    private double alphaFadeMaxDist;
    private boolean removeIfInvisible;
    private List<SphereBuilder.TriangleFace> sphereFaces;
    
    public FXColorEffectSphere(final Vector3 pos) {
        super(pos);
        this.alphaFadeMaxDist = -1.0;
        this.removeIfInvisible = false;
        this.sphereFaces = new LinkedList<SphereBuilder.TriangleFace>();
    }
    
    public FXColorEffectSphere setupSphere(final Vector3 axis, final float scale) {
        return this.setupSphere(axis, scale, 8, 10);
    }
    
    public FXColorEffectSphere setupSphere(final Vector3 axis, final float scale, int fractionsSplit, int fractionsCircle) {
        this.setScaleMultiplier(scale);
        final Vector3 actAxis = axis.clone().normalize().multiply(scale);
        fractionsSplit = MathHelper.func_76125_a(fractionsSplit, 2, Integer.MAX_VALUE);
        fractionsCircle = MathHelper.func_76125_a(fractionsCircle, 3, Integer.MAX_VALUE);
        this.sphereFaces = SphereBuilder.buildFaces(actAxis, fractionsSplit, fractionsCircle);
        return this;
    }
    
    public FXColorEffectSphere setAlphaFadeDistance(final double fadeDistance) {
        if (fadeDistance > 0.0) {
            this.alphaFadeMaxDist = fadeDistance;
            this.alpha((fx, alpha, pTicks) -> {
                Entity rView = Minecraft.func_71410_x().func_175606_aa();
                if (rView == null) {
                    rView = (Entity)Minecraft.func_71410_x().field_71439_g;
                }
                final Vector3 plVec = Vector3.atEntityCenter(rView);
                final double dst = plVec.distance(this.getRenderPosition(pTicks)) - 1.2;
                alpha *= (float)(1.0 - dst / this.alphaFadeMaxDist);
                alpha = MathHelper.func_76131_a(alpha, 0.0f, 1.0f);
                return alpha;
            });
        }
        else {
            this.alpha(VFXAlphaFunction.CONSTANT);
        }
        return this;
    }
    
    public FXColorEffectSphere setRemoveIfInvisible(final boolean removeIfInvisible) {
        this.removeIfInvisible = removeIfInvisible;
        return this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final IVertexBuilder vb, final float pTicks) {
        final int alpha = this.getAlpha(pTicks);
        if (this.removeIfInvisible && alpha <= 0) {
            this.requestRemoval();
            return;
        }
        final Color c = this.getColor(pTicks);
        final int r = c.getRed();
        final int g = c.getGreen();
        final int b = c.getBlue();
        final Matrix4f matr = renderStack.func_227866_c_().func_227870_a_();
        final Vector3 pos = this.getRenderPosition(pTicks);
        pos.subtract(RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks));
        for (final SphereBuilder.TriangleFace face : this.sphereFaces) {
            pos.clone().add(face.getV1()).drawPos(matr, vb).func_225586_a_(r, g, b, alpha).func_181675_d();
            pos.clone().add(face.getV2()).drawPos(matr, vb).func_225586_a_(r, g, b, alpha).func_181675_d();
            pos.clone().add(face.getV3()).drawPos(matr, vb).func_225586_a_(r, g, b, alpha).func_181675_d();
        }
    }
}
