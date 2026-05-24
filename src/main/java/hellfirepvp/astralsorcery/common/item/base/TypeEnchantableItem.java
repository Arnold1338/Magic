package hellfirepvp.astralsorcery.common.item.base;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.world.item.ItemStack;

public interface TypeEnchantableItem
{
    boolean canEnchantItem(final ItemStack p0, final EnchantmentType p1);
}
