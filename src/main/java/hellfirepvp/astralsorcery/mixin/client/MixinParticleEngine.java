package hellfirepvp.astralsorcery.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {
    @Inject(method = "render",
            at = @At("RETURN"), remap = false)
    public void renderParticles(PoseStack poseStack, MultiBufferSource.BufferSource buffer,
                                 LightTexture lightTexture, Camera camera,
                                 float pTicks, Frustum frustum, CallbackInfo ci) {
        EffectHandler.getInstance().render(poseStack, pTicks);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }
}
