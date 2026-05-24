package hellfirepvp.astralsorcery.common.item.armor;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.IArmorMaterial;

public class ArmorMaterialImbuedLeather implements IArmorMaterial
{
    public int func_200896_a(final EquipmentSlot slot) {
        return 486;
    }
    
    public int func_200902_b(final EquipmentSlot slot) {
        switch (slot) {
            case CHEST: {
                return 7;
            }
            default: {
                return 0;
            }
        }
    }
    
    public int func_200900_a() {
        return 24;
    }
    
    public SoundEvent func_200899_b() {
        return ArmorMaterial.LEATHER.func_200899_b();
    }
    
    public Ingredient func_200898_c() {
        return Ingredient.func_199804_a(new ItemLike[] { (ItemLike)ItemsAS.STARDUST });
    }
    
    @OnlyIn(Dist.CLIENT)
    public String func_200897_d() {
        return "imbued_leather";
    }
    
    public float func_200901_e() {
        return 1.5f;
    }
    
    public float func_230304_f_() {
        return 0.0f;
    }
}
