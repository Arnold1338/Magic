package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import net.minecraft.client.model.geom.ModelPart;

public class ModelRefractionTable extends CustomModel
{
    private final ModelPart fitting_l;
    private final ModelPart fitting_r;
    private final ModelPart support_1;
    private final ModelPart support_2;
    private final ModelPart support_3;
    private final ModelPart support_4;
    private final ModelPart platform_l;
    private final ModelPart platform_r;
    private final ModelPart platform_f;
    private final ModelPart platform_b;
    private final ModelPart basin_l;
    private final ModelPart basim_r;
    private final ModelPart basin_f;
    private final ModelPart basin_b;
    private final ModelPart socket;
    private final ModelPart base;
    private final ModelPart leg_1;
    private final ModelPart leg_2;
    private final ModelPart leg_3;
    private final ModelPart leg_4;
    private final ModelPart parchment;
    private final ModelPart black_mirror;
    private final ModelPart treated_glass;
    
    public ModelRefractionTable() {
        super(resKey -> RenderTypesAS.MODEL_REFRACTION_TABLE);
        this.field_78090_t = 128;
        this.field_78089_u = 128;
        (this.fitting_l = new ModelPart((Model)this, 0, 48)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.fitting_l.func_228301_a_(-14.0f, 0.0f, -12.0f, 4.0f, 4.0f, 24.0f, 0.0f);
        (this.fitting_r = new ModelPart((Model)this, 56, 48)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.fitting_r.func_228301_a_(10.0f, 0.0f, -12.0f, 4.0f, 4.0f, 24.0f, 0.0f);
        (this.support_1 = new ModelPart((Model)this, 24, 76)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.support_1.func_228301_a_(-14.0f, 4.0f, -12.0f, 4.0f, 6.0f, 2.0f, 0.0f);
        (this.support_2 = new ModelPart((Model)this, 24, 76)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.support_2.func_228301_a_(10.0f, 4.0f, -12.0f, 4.0f, 6.0f, 2.0f, 0.0f);
        (this.support_3 = new ModelPart((Model)this, 24, 76)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.support_3.func_228301_a_(10.0f, 4.0f, 10.0f, 4.0f, 6.0f, 2.0f, 0.0f);
        (this.support_4 = new ModelPart((Model)this, 24, 76)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.support_4.func_228301_a_(-14.0f, 4.0f, 10.0f, 4.0f, 6.0f, 2.0f, 0.0f);
        (this.platform_l = new ModelPart((Model)this, 0, 0)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.platform_l.func_228301_a_(-14.0f, -6.0f, -12.0f, 4.0f, 2.0f, 24.0f, 0.0f);
        (this.platform_r = new ModelPart((Model)this, 0, 0)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.platform_r.func_228301_a_(10.0f, -6.0f, -12.0f, 4.0f, 2.0f, 24.0f, 0.0f);
        (this.platform_f = new ModelPart((Model)this, 32, 0)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.platform_f.func_228301_a_(-10.0f, -6.0f, -12.0f, 20.0f, 2.0f, 2.0f, 0.0f);
        (this.platform_b = new ModelPart((Model)this, 32, 0)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.platform_b.func_228301_a_(-10.0f, -6.0f, 10.0f, 20.0f, 2.0f, 2.0f, 0.0f);
        (this.basin_l = new ModelPart((Model)this, 84, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.basin_l.func_228301_a_(-10.0f, -8.0f, -10.0f, 2.0f, 6.0f, 20.0f, 0.0f);
        (this.basim_r = new ModelPart((Model)this, 84, 102)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.basim_r.func_228301_a_(8.0f, -8.0f, -10.0f, 2.0f, 6.0f, 20.0f, 0.0f);
        (this.basin_f = new ModelPart((Model)this, 36, 84)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.basin_f.func_228301_a_(-8.0f, -8.0f, -10.0f, 16.0f, 6.0f, 2.0f, 0.0f);
        (this.basin_b = new ModelPart((Model)this, 36, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.basin_b.func_228301_a_(-8.0f, -8.0f, 8.0f, 16.0f, 6.0f, 2.0f, 0.0f);
        (this.socket = new ModelPart((Model)this, 0, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.socket.func_228301_a_(-3.0f, -4.0f, -3.0f, 6.0f, 2.0f, 6.0f, 0.0f);
        (this.base = new ModelPart((Model)this, 0, 26)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.base.func_228301_a_(-10.0f, -2.0f, -10.0f, 20.0f, 2.0f, 20.0f, 0.0f);
        (this.leg_1 = new ModelPart((Model)this, 0, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.leg_1.func_228301_a_(-10.0f, 0.0f, -10.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        (this.leg_2 = new ModelPart((Model)this, 0, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.leg_2.func_228301_a_(4.0f, 0.0f, -10.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        (this.leg_3 = new ModelPart((Model)this, 0, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.leg_3.func_228301_a_(4.0f, 0.0f, 4.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        (this.leg_4 = new ModelPart((Model)this, 0, 76)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.leg_4.func_228301_a_(-10.0f, 0.0f, 4.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        (this.parchment = new ModelPart((Model)this, 66, 28)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.parchment.func_228301_a_(-7.0f, -8.5f, -7.0f, 14.0f, 0.0f, 14.0f, 0.0f);
        (this.black_mirror = new ModelPart((Model)this, 64, 12)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.black_mirror.func_228301_a_(-8.0f, -8.0f, -8.0f, 16.0f, 0.0f, 16.0f, 0.0f);
        (this.treated_glass = new ModelPart((Model)this, 0, 107)).func_78793_a(0.0f, 16.0f, 0.0f);
        this.treated_glass.func_228301_a_(-10.0f, -15.0f, -10.0f, 20.0f, 1.0f, 20.0f, 0.0f);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
    }
    
    public void renderFrame(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha, final boolean hasParchment) {
        this.fitting_l.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.fitting_r.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.support_1.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.support_2.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.support_3.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.support_4.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.platform_l.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.platform_r.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.platform_f.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.platform_b.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.basin_l.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.basim_r.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.basin_f.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.basin_b.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.socket.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.base.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leg_1.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leg_2.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leg_3.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leg_4.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (hasParchment) {
            this.parchment.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.black_mirror.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
    
    public void renderGlass(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.treated_glass.func_228309_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
