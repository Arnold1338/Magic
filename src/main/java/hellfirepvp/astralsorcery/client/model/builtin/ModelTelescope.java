package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.resources.model.ModelRenderer;

public class ModelTelescope extends CustomModel
{
    private final ModelRenderer mountpiece;
    private final ModelRenderer opticalTube;
    private final ModelRenderer leg;
    private final ModelRenderer mountpiece_1;
    private final ModelRenderer aperture;
    private final ModelRenderer extension;
    private final ModelRenderer detail;
    private final ModelRenderer aperture_1;
    
    public ModelTelescope() {
        super(resKey -> RenderTypesAS.MODEL_TELESCOPE);
        this.field_78090_t = 64;
        this.field_78089_u = 64;
        (this.leg = new ModelRenderer((Model)this, 56, 0)).drawString(0.0f, 8.0f, 0.0f);
        this.leg.pushPose()-1.0f, -10.0f, -1.0f, 2.0f, 36.0f, 2.0f, 0.0f);
        (this.mountpiece_1 = new ModelRenderer((Model)this, 32, 0)).drawString(0.0f, 0.0f, -1.0f);
        this.mountpiece_1.pushPose()-2.0f, 20.0f, -1.0f, 4.0f, 6.0f, 4.0f, 0.0f);
        (this.aperture_1 = new ModelRenderer((Model)this, 28, 28)).drawString(0.0f, 0.0f, 0.0f);
        this.aperture_1.pushPose()-1.0f, -3.0f, -6.0f, 6.0f, 6.0f, 2.0f, 0.0f);
        (this.aperture = new ModelRenderer((Model)this, 0, 28)).drawString(0.0f, 0.0f, 0.0f);
        this.aperture.pushPose()-1.0f, -3.0f, -16.0f, 6.0f, 6.0f, 8.0f, 0.0f);
        (this.extension = new ModelRenderer((Model)this, 0, 12)).drawString(0.0f, 0.0f, 0.0f);
        this.extension.pushPose()-2.0f, -6.0f, 6.0f, 2.0f, 6.0f, 2.0f, 0.0f);
        (this.detail = new ModelRenderer((Model)this, 0, 8)).drawString(0.0f, 0.0f, 0.0f);
        this.detail.pushPose()1.0f, -1.0f, 10.0f, 2.0f, 2.0f, 2.0f, 0.0f);
        (this.opticalTube = new ModelRenderer((Model)this, 0, 0)).drawString(1.0f, -3.0f, 0.0f);
        this.opticalTube.pushPose()0.0f, -2.0f, -14.0f, 4.0f, 4.0f, 24.0f, 0.0f);
        this.setRotateAngle(this.opticalTube, -0.7853982f, 0.0f, 0.0f);
        (this.mountpiece = new ModelRenderer((Model)this, 0, 0)).drawString(0.0f, -2.0f, 0.0f);
        this.mountpiece.pushPose()-2.0f, 4.0f, -2.0f, 4.0f, 4.0f, 4.0f, 0.0f);
        this.opticalTube.scale(this.extension);
        this.opticalTube.scale(this.aperture_1);
        this.opticalTube.scale(this.aperture);
        this.opticalTube.scale(this.detail);
        this.mountpiece.scale(this.leg);
        this.mountpiece.scale(this.mountpiece_1);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.mountpiece.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.opticalTube.mulPose(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
