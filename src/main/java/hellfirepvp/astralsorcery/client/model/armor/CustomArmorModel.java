package hellfirepvp.astralsorcery.client.model.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.resources.model.ModelRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.client.model.BipedModel;
import net.minecraft.world.entity.LivingEntity;

public class CustomArmorModel<T extends LivingEntity> extends BipedModel<T>
{
    public CustomArmorModel() {
        super(0.0f);
    }
    
    public void func_225597_a_(final T entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch) {
        if (entity instanceof ArmorStandEntity) {
            final ArmorStandEntity armorStand = (ArmorStandEntity)entity;
            this.field_78116_c.field_78795_f = 0.017453292f * armorStand.func_175418_s().func_179415_b();
            this.field_78116_c.field_78796_g = 0.017453292f * armorStand.func_175418_s().func_179416_c();
            this.field_78116_c.field_78808_h = 0.017453292f * armorStand.func_175418_s().func_179413_d();
            this.field_78116_c.func_78793_a(0.0f, 1.0f, 0.0f);
            this.field_78115_e.field_78795_f = 0.017453292f * armorStand.func_175408_t().func_179415_b();
            this.field_78115_e.field_78796_g = 0.017453292f * armorStand.func_175408_t().func_179416_c();
            this.field_78115_e.field_78808_h = 0.017453292f * armorStand.func_175408_t().func_179413_d();
            this.field_178724_i.field_78795_f = 0.017453292f * armorStand.func_175404_u().func_179415_b();
            this.field_178724_i.field_78796_g = 0.017453292f * armorStand.func_175404_u().func_179416_c();
            this.field_178724_i.field_78808_h = 0.017453292f * armorStand.func_175404_u().func_179413_d();
            this.field_178723_h.field_78795_f = 0.017453292f * armorStand.func_175411_v().func_179415_b();
            this.field_178723_h.field_78796_g = 0.017453292f * armorStand.func_175411_v().func_179416_c();
            this.field_178723_h.field_78808_h = 0.017453292f * armorStand.func_175411_v().func_179413_d();
            this.field_178722_k.field_78795_f = 0.017453292f * armorStand.func_175403_w().func_179415_b();
            this.field_178722_k.field_78796_g = 0.017453292f * armorStand.func_175403_w().func_179416_c();
            this.field_178722_k.field_78808_h = 0.017453292f * armorStand.func_175403_w().func_179413_d();
            this.field_178722_k.func_78793_a(1.9f, 11.0f, 0.0f);
            this.field_178721_j.field_78795_f = 0.017453292f * armorStand.func_175407_x().func_179415_b();
            this.field_178721_j.field_78796_g = 0.017453292f * armorStand.func_175407_x().func_179416_c();
            this.field_178721_j.field_78808_h = 0.017453292f * armorStand.func_175407_x().func_179413_d();
            this.field_178721_j.func_78793_a(-1.9f, 11.0f, 0.0f);
            this.field_178720_f.func_217177_a(this.field_78116_c);
        }
        else {
            super.func_225597_a_((LivingEntity)entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
    
    protected void setRotateAngle(final ModelRenderer modelPart, final float x, final float y, final float z) {
        modelPart.field_78795_f = x;
        modelPart.field_78796_g = y;
        modelPart.field_78808_h = z;
    }
}
