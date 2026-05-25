package hellfirepvp.astralsorcery.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemHighlighted;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class RenderEntityItemHighlighted extends ItemRenderer
{
    protected RenderEntityItemHighlighted(final EntityRendererManager renderManager) {
        super(renderManager, Minecraft.getInstance().func_175599_af());
    }
    
    public void func_225623_a_(final ItemEntity entity, final float entityYaw, final float partialTicks, final PoseStack renderStack, final MultiBufferSource buffer, final int packedLight) {
        if (entity instanceof EntityItemHighlighted && ((EntityItemHighlighted)entity).hasColor()) {
            renderStack.popPose();
            renderStack.translate(0.0, 0.3499999940395355, 0.0);
            RenderingDrawUtils.renderLightRayFan(renderStack, buffer, ((EntityItemHighlighted)entity).getHighlightColor(), 160420L + entity.func_145782_y(), 16, 12.0f, 15);
            renderStack.popPose();
        }
        super.func_225623_a_(entity, entityYaw, partialTicks, renderStack, buffer, packedLight);
    }
    
    public static class Factory implements IRenderFactory<EntityItemHighlighted>
    {
        public EntityRenderer<? super EntityItemHighlighted> createRenderFor(final EntityRendererManager manager) {
            return (EntityRenderer<? super EntityItemHighlighted>)new RenderEntityItemHighlighted(manager);
        }
    }
}
