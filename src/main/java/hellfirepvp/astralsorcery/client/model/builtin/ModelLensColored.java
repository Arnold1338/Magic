package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.resources.model.ModelRenderer;

public class ModelLensColored extends CustomModel
{
    public final ModelRenderer glass;
    public final ModelRenderer detail1;
    public final ModelRenderer detail1_1;
    public final ModelRenderer fitting2;
    public final ModelRenderer fitting1;
    
    public ModelLensColored() {
        super(resKey -> RenderTypesAS.MODEL_LENS_COLORED_SOLID);
        this.field_78090_t = 32;
        this.field_78089_u = 16;
        (this.glass = new ModelRenderer((Model)this, 0, 0)).drawString(0.0f, 14.0f, 0.0f);
        this.glass.pushPose()-5.0f, -5.0f, -1.51f, 10.0f, 10.0f, 1.0f, 0.0f);
        (this.fitting1 = new ModelRenderer((Model)this, 22, 0)).drawString(0.0f, 14.0f, 0.0f);
        this.fitting1.pushPose()-5.0f, -7.0f, -1.5f, 2.0f, 1.0f, 2.0f, 0.0f);
        (this.detail1_1 = new ModelRenderer((Model)this, 22, 3)).drawString(0.0f, 14.0f, 0.0f);
        this.detail1_1.pushPose()3.0f, -6.0f, -1.5f, 2.0f, 1.0f, 1.0f, 0.0f);
        (this.fitting2 = new ModelRenderer((Model)this, 22, 0)).drawString(0.0f, 14.0f, 0.0f);
        this.fitting2.pushPose()3.0f, -7.0f, -1.5f, 2.0f, 1.0f, 2.0f, 0.0f);
        (this.detail1 = new ModelRenderer((Model)this, 22, 3)).drawString(0.0f, 14.0f, 0.0f);
        this.detail1.pushPose()-5.0f, -6.0f, -1.5f, 2.0f, 1.0f, 1.0f, 0.0f);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.fitting1.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.detail1_1.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.fitting2.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.detail1.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    
    public void renderGlass(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.glass.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
