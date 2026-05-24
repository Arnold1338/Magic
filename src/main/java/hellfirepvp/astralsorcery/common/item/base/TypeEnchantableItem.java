package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.ItemStack;

public interface TypeEnchantableItem
{
    boolean canEnchantItem(final ItemStack p0, final EnchantmentCategory p1);
}
