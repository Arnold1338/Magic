package hellfirepvp.astralsorcery.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.entity.technical.EntityGrapplingHook;
import net.minecraft.client.renderer.entity.EntityRenderer;

public class RenderEntityGrapplingHook extends EntityRenderer<EntityGrapplingHook>
{
    protected RenderEntityGrapplingHook(final EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    public void render(final EntityGrapplingHook entity, final float entityYaw, final float partialTicks, final PoseStack matrixStack, final MultiBufferSource buffer, final int packedLight) {
        int alphaMultiplier;
        if (entity.isDespawning()) {
            alphaMultiplier = MathHelper.func_76125_a(127 - (int)(entity.despawnPercentage(partialTicks) * 255.0f), 0, 255);
        }
        else {
            alphaMultiplier = 255;
        }
        if (alphaMultiplier <= 1.0E-4) {
            return;
        }
        final Vector3 entityPos = RenderingVectorUtils.interpolatePosition((Entity)entity, partialTicks);
        final List<Vector3> line = entity.buildLine(partialTicks);
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.disableCull();
        SpritesAS.SPR_GRAPPLING_HOOK.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> RenderingDrawUtils.renderFacingSpriteVB((IVertexBuilder)buf, matrixStack, entityPos.getX(), entityPos.getY(), entityPos.getZ(), 1.3f, 0.0f, SpritesAS.SPR_GRAPPLING_HOOK, ClientScheduler.getClientTick() + entity.field_70173_aa, 255, 255, 255, alphaMultiplier));
        TexturesAS.TEX_PARTICLE_LARGE.bindTexture();
        Blending.ADDITIVE_ALPHA.apply();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
            line.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final Vector3 pos = iterator.next();
                final Vector3 at = pos.multiply(2).add(entityPos);
                RenderingDrawUtils.renderFacingFullQuadVB((IVertexBuilder)buf, matrixStack, at.getX(), at.getY(), at.getZ(), 0.3f, 0.0f, 50, 40, 180, (int)(alphaMultiplier * 0.8f));
            }
            return;
        });
        RenderSystem.enableCull();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
    
    public ResourceLocation getEntityTexture(final EntityGrapplingHook entity) {
        return AtlasTexture.field_110575_b;
    }
    
    public static class Factory implements IRenderFactory<EntityGrapplingHook>
    {
        public EntityRenderer<? super EntityGrapplingHook> createRenderFor(final EntityRendererManager manager) {
            return new RenderEntityGrapplingHook(manager);
        }
    }
}
