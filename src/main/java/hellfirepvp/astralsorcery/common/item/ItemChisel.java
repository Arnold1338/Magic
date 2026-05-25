package hellfirepvp.astralsorcery.common.item;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemChisel extends Item
{
    public ItemChisel() {
        super(new Item.Properties().func_200918_c(72).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public int getItemEnchantability(final ItemStack stack) {
        return 3;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.BLOCK_FORTUNE;
    }
}
