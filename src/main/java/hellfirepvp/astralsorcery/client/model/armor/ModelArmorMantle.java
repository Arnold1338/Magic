package hellfirepvp.astralsorcery.client.model.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.Model;
import net.minecraft.client.resources.model.ModelRenderer;
import net.minecraft.world.entity.LivingEntity;

public class ModelArmorMantle extends CustomArmorModel<LivingEntity>
{
    private final ModelRenderer bodyReplacement;
    private final ModelRenderer lArm;
    private final ModelRenderer rArm;
    private final ModelRenderer headReplacement;
    private final ModelRenderer cowl;
    private final ModelRenderer mantle_l;
    private final ModelRenderer mantle_r;
    private final ModelRenderer bodyAnchor;
    private final ModelRenderer body;
    private final ModelRenderer plate;
    private final ModelRenderer armLAnchor;
    private final ModelRenderer armLpauldron;
    private final ModelRenderer fitting_l;
    private final ModelRenderer armRAnchor;
    private final ModelRenderer armRpauldron;
    private final ModelRenderer fitting_r;
    
    public ModelArmorMantle() {
        final float s = 0.01f;
        this.field_78090_t = 64;
        this.field_78089_u = 128;
        (this.cowl = new ModelRenderer((Model)this, 0, 33)).drawString(0.0f, 0.0f, 0.0f);
        this.cowl.pushPose()-4.5f, -4.0f, -4.0f, 9.0f, 5.0f, 9.0f, s);
        this.setRotateAngle(this.cowl, 0.2617994f, 0.0f, 0.0f);
        this.mantle_l = new ModelRenderer((Model)this, 0, 47);
        this.mantle_l.field_78809_i = true;
        this.mantle_l.drawString(6.25f, 2.0f, 0.0f);
        this.mantle_l.pushPose()-8.0f, -3.5f, 1.0f, 9.0f, 21.0f, 5.0f, s);
        this.setRotateAngle(this.mantle_l, 0.08726646f, 0.2617994f, 0.0f);
        (this.mantle_r = new ModelRenderer((Model)this, 0, 47)).drawString(-6.25f, 2.0f, 0.0f);
        this.mantle_r.pushPose()-1.0f, -3.5f, 1.0f, 9.0f, 21.0f, 5.0f, s);
        this.setRotateAngle(this.mantle_r, 0.08726646f, -0.2617994f, 0.0f);
        (this.bodyAnchor = new ModelRenderer((Model)this, 0, 41)).drawString(0.0f, 0.0f, 0.0f);
        this.bodyAnchor.pushPose()-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        (this.body = new ModelRenderer((Model)this, 0, 0)).drawString(0.0f, 0.0f, 0.0f);
        this.body.pushPose()-4.5f, -0.5f, -3.0f, 9.0f, 6.0f, 6.0f, s);
        (this.plate = new ModelRenderer((Model)this, 0, 12)).drawString(0.0f, 1.0f, -3.0f);
        this.plate.pushPose()-3.5f, -0.5f, -1.0f, 7.0f, 7.0f, 2.0f, s);
        this.setRotateAngle(this.plate, 0.08726646f, 0.0f, 0.0f);
        this.armLAnchor = new ModelRenderer((Model)this, 0, 41);
        this.armLAnchor.field_78809_i = true;
        this.armLAnchor.drawString(4.0f, 2.0f, 0.0f);
        this.armLAnchor.pushPose()-6.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        this.armLpauldron = new ModelRenderer((Model)this, 0, 21);
        this.armLpauldron.field_78809_i = true;
        this.armLpauldron.drawString(0.0f, 0.0f, -0.0f);
        this.armLpauldron.pushPose()-5.45f, -4.0f, -3.0f, 5.0f, 6.0f, 6.0f, s);
        (this.fitting_l = new ModelRenderer((Model)this, 18, 12)).drawString(0.5f, -3.0f, 0.0f);
        this.fitting_l.pushPose()-6.0f, -2.0f, -1.0f, 4.0f, 1.0f, 2.0f, s);
        this.setRotateAngle(this.fitting_l, 0.0f, 0.0f, 0.08726646f);
        this.armRAnchor = new ModelRenderer((Model)this, 0, 41);
        this.armRAnchor.field_78809_i = true;
        this.armRAnchor.drawString(-4.0f, 2.0f, 0.0f);
        this.armRAnchor.pushPose()4.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        (this.armRpauldron = new ModelRenderer((Model)this, 0, 21)).drawString(0.0f, 0.0f, 0.0f);
        this.armRpauldron.pushPose()0.45f, -4.0f, -3.0f, 5.0f, 6.0f, 6.0f, s);
        (this.fitting_r = new ModelRenderer((Model)this, 18, 12)).drawString(0.0f, -3.0f, 0.0f);
        this.fitting_r.pushPose()1.5f, -2.0f, -1.0f, 4.0f, 1.0f, 2.0f, s);
        this.setRotateAngle(this.fitting_r, 0.0f, 0.0f, -0.08726646f);
        (this.bodyReplacement = new ModelRenderer((Model)this)).scale(this.bodyAnchor);
        this.bodyAnchor.scale(this.body);
        this.body.scale(this.plate);
        this.body.scale(this.mantle_l);
        this.body.scale(this.mantle_r);
        (this.headReplacement = new ModelRenderer((Model)this)).scale(this.cowl);
        (this.lArm = new ModelRenderer((Model)this)).scale(this.armLAnchor);
        this.armLAnchor.scale(this.armLpauldron);
        this.armLpauldron.scale(this.fitting_l);
        (this.rArm = new ModelRenderer((Model)this)).scale(this.armRAnchor);
        this.armRAnchor.scale(this.armRpauldron);
        this.armRpauldron.scale(this.fitting_r);
    }
    
    public void func_225598_a_(final PoseStack matrixStackIn, final VertexConsumer bufferIn, final int packedLightIn, final int packedOverlayIn, final float red, final float green, final float blue, final float alpha) {
        this.bodyAnchor.field_78806_j = true;
        this.armRAnchor.field_78806_j = true;
        this.armLAnchor.field_78806_j = true;
        this.field_78116_c.field_78806_j = true;
        this.field_178720_f.field_78806_j = false;
        this.field_178721_j.field_78806_j = false;
        this.field_178722_k.field_78806_j = false;
        this.field_78115_e = this.bodyReplacement;
        this.field_178723_h = this.rArm;
        this.field_178724_i = this.lArm;
        this.field_78116_c = this.headReplacement;
        super.func_225598_a_(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
