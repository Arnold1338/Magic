package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.renderer.model.ModelRenderer;

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
        (this.leg = new ModelRenderer((Model)this, 56, 0)).func_78793_a(0.0f, 8.0f, 0.0f);
        this.leg.func_228301_a_(-1.0f, -10.0f, -1.0f, 2.0f, 36.0f, 2.0f, 0.0f);
        (this.mountpiece_1 = new ModelRenderer((Model)this, 32, 0)).func_78793_a(0.0f, 0.0f, -1.0f);
        this.mountpiece_1.func_228301_a_(-2.0f, 20.0f, -1.0f, 4.0f, 6.0f, 4.0f, 0.0f);
        (this.aperture_1 = new ModelRenderer((Model)this, 28, 28)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.aperture_1.func_228301_a_(-1.0f, -3.0f, -6.0f, 6.0f, 6.0f, 2.0f, 0.0f);
        (this.aperture = new ModelRenderer((Model)this, 0, 28)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.aperture.func_228301_a_(-1.0f, -3.0f, -16.0f, 6.0f, 6.0f, 8.0f, 0.0f);
        (this.extension = new ModelRenderer((Model)this, 0, 12)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.extension.func_228301_a_(-2.0f, -6.0f, 6.0f, 2.0f, 6.0f, 2.0f, 0.0f);
        (this.detail = new ModelRenderer((Model)this, 0, 8)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.detail.func_228301_a_(1.0f, -1.0f, 10.0f, 2.0f, 2.0f, 2.0f, 0.0f);
        (this.opticalTube = new ModelRenderer((Model)this, 0, 0)).func_78793_a(1.0f, -3.0f, 0.0f);
        this.opticalTube.func_228301_a_(0.0f, -2.0f, -14.0f, 4.0f, 4.0f, 24.0f, 0.0f);
        this.setRotateAngle(this.opticalTube, -0.7853982f, 0.0f, 0.0f);
        (this.mountpiece = new ModelRenderer((Model)this, 0, 0)).func_78793_a(0.0f, -2.0f, 0.0f);
        this.mountpiece.func_228301_a_(-2.0f, 4.0f, -2.0f, 4.0f, 4.0f, 4.0f, 0.0f);
        this.opticalTube.func_78792_a(this.extension);
        this.opticalTube.func_78792_a(this.aperture_1);
        this.opticalTube.func_78792_a(this.aperture);
        this.opticalTube.func_78792_a(this.detail);
        this.mountpiece.func_78792_a(this.leg);
        this.mountpiece.func_78792_a(this.mountpiece_1);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final IVertexBuilder bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.mountpiece.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.opticalTube.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
