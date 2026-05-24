package hellfirepvp.astralsorcery.client.render;

import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.RenderStateUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.client.renderer.RenderState;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.client.renderer.RenderType;

public class RenderStateBuilder
{
    private final RenderType.State.Builder builder;
    
    private RenderStateBuilder(final RenderType.State.Builder builder) {
        this.builder = builder;
    }
    
    public static RenderStateBuilder builder() {
        return new RenderStateBuilder(RenderType.State.func_228694_a_());
    }
    
    public RenderStateBuilder texture(final AbstractRenderableTexture texture) {
        this.builder.func_228724_a_(texture.asState());
        return this;
    }
    
    public RenderStateBuilder altasTexture() {
        this.builder.func_228724_a_(BlockAtlasTexture.getInstance().asState());
        return this;
    }
    
    public RenderStateBuilder disableTexture() {
        this.builder.func_228724_a_(new RenderState.TextureState());
        return this;
    }
    
    public RenderStateBuilder blend(final Blending blendMode) {
        this.builder.func_228726_a_(blendMode.asState());
        return this;
    }
    
    public RenderStateBuilder smoothShade() {
        this.builder.func_228723_a_(new RenderState.ShadeModelState(true));
        return this;
    }
    
    public RenderStateBuilder enableItemRendering() {
        this.builder.func_228716_a_(new RenderState.DiffuseLightingState(true));
        return this;
    }
    
    public RenderStateBuilder disableDepth() {
        this.builder.func_228715_a_((RenderState.DepthTestState)new RenderState.DepthTestState("always", 519) {
            public void func_228547_a_() {
                RenderSystem.disableDepthTest();
                super.func_228547_a_();
            }
        });
        return this;
    }
    
    public RenderStateBuilder disableDepthMask() {
        this.builder.func_228727_a_((RenderState.WriteMaskState)new RenderStateUtil.WriteMaskState(true, false));
        return this;
    }
    
    public RenderStateBuilder enableLighting() {
        this.builder.func_228719_a_(new RenderState.LightmapState(true));
        return this;
    }
    
    public RenderStateBuilder enableDiffuseLighting() {
        this.builder.func_228716_a_(new RenderState.DiffuseLightingState(true));
        return this;
    }
    
    public RenderStateBuilder enableOverlay() {
        this.builder.func_228722_a_(new RenderState.OverlayState(true));
        return this;
    }
    
    public RenderStateBuilder disableCull() {
        this.builder.func_228714_a_((RenderState.CullState)new RenderStateUtil.CullState(false));
        return this;
    }
    
    public RenderStateBuilder alpha(final float alphaThreshold) {
        this.builder.func_228713_a_(new RenderState.AlphaState(alphaThreshold));
        return this;
    }
    
    public RenderStateBuilder defaultAlpha() {
        return this.alpha(0.003921569f);
    }
    
    public RenderStateBuilder particleShaderTarget() {
        this.builder.func_228721_a_((RenderState.TargetState)ParticleTarget.INSTANCE);
        return this;
    }
    
    public RenderType.State.Builder vanillaBuilder() {
        return this.builder;
    }
    
    public RenderType.State buildAsOverlay() {
        return this.builder.func_228728_a_(true);
    }
    
    public RenderType.State build() {
        return this.builder.func_228728_a_(false);
    }
    
    private static class ParticleTarget extends RenderState.TargetState
    {
        private static final ParticleTarget INSTANCE;
        
        private ParticleTarget() {
            super("as_particle_target", () -> {
                if (Minecraft.func_238218_y_()) {
                    Minecraft.func_71410_x().field_71438_f.func_239230_s_().func_147610_a(false);
                }
            }, () -> {
                if (Minecraft.func_238218_y_()) {
                    Minecraft.func_71410_x().func_147110_a().func_147610_a(false);
                }
            });
        }
        
        static {
            INSTANCE = new ParticleTarget();
        }
    }
}
