package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentScorchingHeat extends Enchantment
{
    public EnchantmentScorchingHeat() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }
    
    protected boolean func_77326_a(final Enchantment ench) {
        return super.func_77326_a(ench) && ench != Enchantments.SILK_TOUCH;
    }
    
    public boolean func_92089_a(final ItemStack stack) {
        return stack.canApplyAtEnchantingTable((Enchantment)this);
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack) {
        return false;
    }
}
