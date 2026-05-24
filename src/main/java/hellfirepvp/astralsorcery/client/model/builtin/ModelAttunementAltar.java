package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.resources.model.ModelRenderer;

public class ModelAttunementAltar extends CustomModel
{
    private final ModelRenderer base;
    private final ModelRenderer hovering;
    
    public ModelAttunementAltar() {
        super(resKey -> RenderTypesAS.MODEL_ATTUNEMENT_ALTAR);
        this.field_78090_t = 128;
        this.field_78089_u = 32;
        (this.base = new ModelRenderer((Model)this, 0, 0)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.base.func_228301_a_(-10.0f, -14.0f, -10.0f, 20.0f, 6.0f, 20.0f, 0.0f);
        (this.hovering = new ModelRenderer((Model)this, 0, 0)).func_78793_a(-2.0f, -16.0f, -2.0f);
        this.hovering.func_228301_a_(0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 0.0f);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.base.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    
    public void renderHovering(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha, final float offX, final float offZ, final float perc) {
        final float distance = 0.9453125f;
        this.hovering.func_78793_a(-2.0f + 16.0f * offX * distance, -16.0f, -2.0f + 16.0f * offZ * distance);
        this.setRotateAngle(this.hovering, offZ * 0.3926991f * perc, 0.0f, offX * -0.3926991f * perc);
        this.hovering.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
