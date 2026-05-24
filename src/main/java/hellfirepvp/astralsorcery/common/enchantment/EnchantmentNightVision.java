package hellfirepvp.astralsorcery.common.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentNightVision extends EnchantmentPlayerTick
{
    public EnchantmentNightVision() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[] { EquipmentSlot.HEAD });
    }
    
    @Override
    public void tick(final Player player, final LogicalSide side, final int level) {
        if (side.isServer()) {
            player.func_195064_c(new MobEffectInstance(Effects.field_76439_r, 300, level - 1, true, false));
        }
    }
    
    public boolean func_92089_a(final ItemStack stack) {
        return this.field_77351_y.func_77557_a(stack.getItem());
    }
    
    public int func_77325_b() {
        return 1;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack) {
        return false;
    }
}
