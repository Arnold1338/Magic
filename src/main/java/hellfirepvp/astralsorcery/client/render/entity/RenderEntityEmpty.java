package hellfirepvp.astralsorcery.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRenderer;

public class RenderEntityEmpty extends EntityRenderer<Entity>
{
    public RenderEntityEmpty(final EntityRendererManager mgr) {
        super(mgr);
    }
    
    public void func_225623_a_(final Entity entity, final float entityYaw, final float partialTicks, final PoseStack matrixStack, final MultiBufferSource buffer, final int packedLight) {
    }
    
    public ResourceLocation func_110775_a(final Entity entity) {
        return AtlasTexture.field_110575_b;
    }
    
    public static class Factory implements IRenderFactory<Entity>
    {
        public EntityRenderer<? super Entity> createRenderFor(final EntityRendererManager manager) {
            return new RenderEntityEmpty(manager);
        }
    }
}
