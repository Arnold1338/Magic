package hellfirepvp.astralsorcery.client.model.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ModelArmorMantle extends CustomArmorModel<LivingEntity>
{
    private final ModelPart bodyReplacement;
    private final ModelPart lArm;
    private final ModelPart rArm;
    private final ModelPart headReplacement;
    private final ModelPart cowl;
    private final ModelPart mantle_l;
    private final ModelPart mantle_r;
    private final ModelPart bodyAnchor;
    private final ModelPart body;
    private final ModelPart plate;
    private final ModelPart armLAnchor;
    private final ModelPart armLpauldron;
    private final ModelPart fitting_l;
    private final ModelPart armRAnchor;
    private final ModelPart armRpauldron;
    private final ModelPart fitting_r;
    
    public ModelArmorMantle() {
        final float s = 0.01f;
        this.field_78090_t = 64;
        this.field_78089_u = 128;
        (this.cowl = new ModelPart((Model)this, 0, 33)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.cowl.func_228301_a_(-4.5f, -4.0f, -4.0f, 9.0f, 5.0f, 9.0f, s);
        this.setRotateAngle(this.cowl, 0.2617994f, 0.0f, 0.0f);
        this.mantle_l = new ModelPart((Model)this, 0, 47);
        this.mantle_l.field_78809_i = true;
        this.mantle_l.func_78793_a(6.25f, 2.0f, 0.0f);
        this.mantle_l.func_228301_a_(-8.0f, -3.5f, 1.0f, 9.0f, 21.0f, 5.0f, s);
        this.setRotateAngle(this.mantle_l, 0.08726646f, 0.2617994f, 0.0f);
        (this.mantle_r = new ModelPart((Model)this, 0, 47)).func_78793_a(-6.25f, 2.0f, 0.0f);
        this.mantle_r.func_228301_a_(-1.0f, -3.5f, 1.0f, 9.0f, 21.0f, 5.0f, s);
        this.setRotateAngle(this.mantle_r, 0.08726646f, -0.2617994f, 0.0f);
        (this.bodyAnchor = new ModelPart((Model)this, 0, 41)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.bodyAnchor.func_228301_a_(-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        (this.body = new ModelPart((Model)this, 0, 0)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.body.func_228301_a_(-4.5f, -0.5f, -3.0f, 9.0f, 6.0f, 6.0f, s);
        (this.plate = new ModelPart((Model)this, 0, 12)).func_78793_a(0.0f, 1.0f, -3.0f);
        this.plate.func_228301_a_(-3.5f, -0.5f, -1.0f, 7.0f, 7.0f, 2.0f, s);
        this.setRotateAngle(this.plate, 0.08726646f, 0.0f, 0.0f);
        this.armLAnchor = new ModelPart((Model)this, 0, 41);
        this.armLAnchor.field_78809_i = true;
        this.armLAnchor.func_78793_a(4.0f, 2.0f, 0.0f);
        this.armLAnchor.func_228301_a_(-6.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        this.armLpauldron = new ModelPart((Model)this, 0, 21);
        this.armLpauldron.field_78809_i = true;
        this.armLpauldron.func_78793_a(0.0f, 0.0f, -0.0f);
        this.armLpauldron.func_228301_a_(-5.45f, -4.0f, -3.0f, 5.0f, 6.0f, 6.0f, s);
        (this.fitting_l = new ModelPart((Model)this, 18, 12)).func_78793_a(0.5f, -3.0f, 0.0f);
        this.fitting_l.func_228301_a_(-6.0f, -2.0f, -1.0f, 4.0f, 1.0f, 2.0f, s);
        this.setRotateAngle(this.fitting_l, 0.0f, 0.0f, 0.08726646f);
        this.armRAnchor = new ModelPart((Model)this, 0, 41);
        this.armRAnchor.field_78809_i = true;
        this.armRAnchor.func_78793_a(-4.0f, 2.0f, 0.0f);
        this.armRAnchor.func_228301_a_(4.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, s);
        (this.armRpauldron = new ModelPart((Model)this, 0, 21)).func_78793_a(0.0f, 0.0f, 0.0f);
        this.armRpauldron.func_228301_a_(0.45f, -4.0f, -3.0f, 5.0f, 6.0f, 6.0f, s);
        (this.fitting_r = new ModelPart((Model)this, 18, 12)).func_78793_a(0.0f, -3.0f, 0.0f);
        this.fitting_r.func_228301_a_(1.5f, -2.0f, -1.0f, 4.0f, 1.0f, 2.0f, s);
        this.setRotateAngle(this.fitting_r, 0.0f, 0.0f, -0.08726646f);
        (this.bodyReplacement = new ModelPart((Model)this)).func_78792_a(this.bodyAnchor);
        this.bodyAnchor.func_78792_a(this.body);
        this.body.func_78792_a(this.plate);
        this.body.func_78792_a(this.mantle_l);
        this.body.func_78792_a(this.mantle_r);
        (this.headReplacement = new ModelPart((Model)this)).func_78792_a(this.cowl);
        (this.lArm = new ModelPart((Model)this)).func_78792_a(this.armLAnchor);
        this.armLAnchor.func_78792_a(this.armLpauldron);
        this.armLpauldron.func_78792_a(this.fitting_l);
        (this.rArm = new ModelPart((Model)this)).func_78792_a(this.armRAnchor);
        this.armRAnchor.func_78792_a(this.armRpauldron);
        this.armRpauldron.func_78792_a(this.fitting_r);
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
