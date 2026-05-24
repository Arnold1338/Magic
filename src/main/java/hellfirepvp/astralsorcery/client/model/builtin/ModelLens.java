package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.resources.model.ModelRenderer;

public class ModelLens extends CustomModel
{
    public final ModelRenderer base;
    public final ModelRenderer frame1;
    public final ModelRenderer lens;
    public final ModelRenderer frame2;
    
    public ModelLens() {
        super(resKey -> RenderTypesAS.MODEL_LENS_SOLID);
        this.field_78090_t = 64;
        this.field_78089_u = 32;
        (this.base = new ModelRenderer((Model)this, 0, 13)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.base.func_228301_a_(-6.0f, 4.0f, -6.0f, 12.0f, 2.0f, 12.0f, 0.0f);
        (this.frame1 = new ModelRenderer((Model)this, 0, 13)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.frame1.func_228301_a_(-8.0f, -4.0f, -1.0f, 2.0f, 10.0f, 2.0f, 0.0f);
        this.frame2 = new ModelRenderer((Model)this, 0, 13);
        this.frame2.field_78809_i = true;
        this.frame2.func_78793_a(0.0f, 16.0f, 0.0f);
        this.frame2.func_228301_a_(6.0f, -4.0f, -1.0f, 2.0f, 10.0f, 2.0f, 0.0f);
        (this.lens = new ModelRenderer((Model)this, 0, 0)).func_78793_a(0.0f, 14.0f, 0.0f);
        this.lens.func_228301_a_(-6.0f, -6.0f, -0.5f, 12.0f, 12.0f, 1.0f, 0.0f);
    }
    
    public void renderFrame(final PoseStack matrixStackIn, final MultiBufferSource buffer, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        final VertexConsumer vb = buffer.getBuffer(RenderTypesAS.MODEL_LENS_SOLID);
        this.base.func_228309_a_(matrixStackIn, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.frame1.func_228309_a_(matrixStackIn, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.frame2.func_228309_a_(matrixStackIn, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        RenderingUtils.refreshDrawing(vb, RenderTypesAS.MODEL_LENS_SOLID);
    }
    
    public void renderGlass(final PoseStack matrixStackIn, final MultiBufferSource buffer, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        final VertexConsumer vb = buffer.getBuffer(RenderTypesAS.MODEL_LENS_GLASS);
        this.lens.func_228309_a_(matrixStackIn, vb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.lens.field_78795_f = 0.0f;
        RenderingUtils.refreshDrawing(vb, RenderTypesAS.MODEL_LENS_GLASS);
    }
    
    @Override
    public void render(final PoseStack matrixStackIn, final MultiBufferSource buffer, final int packedLightIn, final int packedOverlayIn) {
        super.render(matrixStackIn, buffer, packedLightIn, packedOverlayIn);
        this.renderFrame(matrixStackIn, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f);
        this.renderGlass(matrixStackIn, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
    }
}
