package hellfirepvp.astralsorcery.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.item.AxeItem;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraft.client.renderer.entity.EntityRenderer;

public class RenderEntitySpectralTool extends EntityRenderer<EntitySpectralTool>
{
    protected RenderEntitySpectralTool(final EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    public void render(final EntitySpectralTool entity, final float entityYaw, final float partialTicks, final PoseStack renderStack, final MultiBufferSource buffer, final int packedLight) {
        final ItemStack stack = entity.getItem();
        if (stack.isEmpty() || !entity.isAlive()) {
            return;
        }
        renderStack.popPose();
        renderStack.translate(0.0, (double)(entity.func_213302_cg() / 2.0f), 0.0);
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).getMultiBufferSource()-entityYaw - 90.0f));
        if (stack.getItem() instanceof AxeItem) {
            renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).getMultiBufferSource()180.0f));
            renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).getMultiBufferSource()270.0f));
        }
        RenderingUtils.renderTranslucentItemStackModelGround(stack, renderStack, ColorsAS.SPECTRAL_TOOL, Blending.CONSTANT_ALPHA, 63);
        renderStack.popPose();
    }
    
    public ResourceLocation getEntityTexture(final EntitySpectralTool entity) {
        return AtlasTexture.field_110575_b;
    }
    
    public static class Factory implements IRenderFactory<EntitySpectralTool>
    {
        public EntityRenderer<? super EntitySpectralTool> createRenderFor(final EntityRendererManager manager) {
            return new RenderEntitySpectralTool(manager);
        }
    }
}
