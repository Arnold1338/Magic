package hellfirepvp.astralsorcery.client.effect.vfx;

import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXFacingAtlasParticle extends EntityVisualFX
{
    private TextureAtlasSprite sprite;
    private float minU;
    private float minV;
    private float uLength;
    private float vLength;
    
    public FXFacingAtlasParticle(final Vector3 pos) {
        super(pos);
        this.minU = 0.0f;
        this.minV = 0.0f;
        this.uLength = 1.0f;
        this.vLength = 1.0f;
    }
    
    public <T extends FXFacingAtlasParticle> T setSprite(final TextureAtlasSprite sprite) {
        this.sprite = sprite;
        this.minU = this.sprite.func_94209_e();
        this.minV = this.sprite.func_94206_g();
        this.uLength = this.sprite.func_94212_f() - this.minU;
        this.vLength = this.sprite.func_94210_h() - this.minV;
        return (T)this;
    }
    
    public <T extends FXFacingAtlasParticle> T selectFraction(float percentage) {
        percentage = Mth.canEnchant(percentage, 0.0f, 1.0f);
        this.minU += this.uLength * (1.0f - percentage) * FXFacingAtlasParticle.rand.nextFloat();
        this.minV += this.vLength * (1.0f - percentage) * FXFacingAtlasParticle.rand.nextFloat();
        this.uLength *= percentage;
        this.vLength *= percentage;
        return (T)this;
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
        if (this.sprite == null) {
            return;
        }
        final Vector3 vec = this.getRenderPosition(pTicks);
        final int alpha = this.getAlpha(pTicks);
        final float fScale = this.getScale(pTicks);
        final Color col = this.getColor(pTicks);
        RenderingDrawUtils.renderFacingQuadVB(vb, renderStack, vec.getX(), vec.getY(), vec.getZ(), fScale, 0.0f, this.minU, this.minV, this.uLength, this.vLength, col.getRed(), col.getGreen(), col.getBlue(), alpha);
    }
}
