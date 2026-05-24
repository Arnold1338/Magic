package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;

public class ItemCrystalPickaxe extends ItemCrystalTierItem implements TypeEnchantableItem
{
    public ItemCrystalPickaxe() {
        super(ToolType.PICKAXE, new Item.Properties(), Sets.newHashSet((Object[])new Material[] { Material.field_151576_e, Material.field_151573_f, Material.field_151574_g }));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> stacks) {
        if (this.func_194125_a(group)) {
            final ItemStack stack = new ItemStack((ItemLike)this);
            CrystalPropertiesAS.CREATIVE_CRYSTAL_TOOL_ATTRIBUTES.store(stack);
            stacks.add((Object)stack);
        }
    }
    
    @Override
    public boolean canEnchantItem(final ItemStack stack, final EnchantmentType type) {
        return type == EnchantmentType.BREAKABLE || type == EnchantmentType.DIGGER;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        final EnchantmentType type = enchantment.field_77351_y;
        return type == EnchantmentType.DIGGER || type == EnchantmentType.BREAKABLE;
    }
    
    @Override
    double getAttackDamage() {
        return 5.0;
    }
    
    @Override
    double getAttackSpeed() {
        return -1.0;
    }
}
