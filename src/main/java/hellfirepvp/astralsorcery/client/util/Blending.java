package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.Locale;
import net.minecraft.client.renderer.RenderStateShard;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

public enum Blending
{
    DEFAULT(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA), 
    ALPHA(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.SRC_ALPHA), 
    PREALPHA(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA), 
    MULTIPLY(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA), 
    ADDITIVE(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE), 
    ADDITIVEDARK(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR), 
    OVERLAYDARK(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE), 
    ADDITIVE_ALPHA(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE), 
    CONSTANT_ALPHA(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA), 
    INVERTEDADD(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
    
    private final GlStateManager.SourceFactor colorSrcFactor;
    private final GlStateManager.SourceFactor alphaSrcFactor;
    private final GlStateManager.DestFactor colorDstFactor;
    private final GlStateManager.DestFactor alphaDstFactor;
    
    private Blending(final GlStateManager.SourceFactor src, final GlStateManager.DestFactor dst) {
        this(src, dst, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }
    
    private Blending(final GlStateManager.SourceFactor src, final GlStateManager.DestFactor dst, final GlStateManager.SourceFactor srcAlpha, final GlStateManager.DestFactor dstAlpha) {
        this.colorSrcFactor = src;
        this.colorDstFactor = dst;
        this.alphaSrcFactor = srcAlpha;
        this.alphaDstFactor = dstAlpha;
    }
    
    public void apply() {
        RenderSystem.blendFuncSeparate(this.colorSrcFactor, this.colorDstFactor, this.alphaSrcFactor, this.alphaDstFactor);
    }
    
    public RenderStateShard.TransparencyState asState() {
        return new RenderStateShard.TransparencyState(AstralSorcery.key("blending_" + this.name().toLowerCase(Locale.ROOT)).toString(), () -> {
            RenderSystem.enableBlend();
            this.apply();
        }, () -> {
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        });
    }
}
