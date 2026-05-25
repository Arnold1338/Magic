package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.HashMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.IItemTier;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import net.minecraft.world.item.SwordItem;

public class ItemCrystalSword extends SwordItem implements CrystalAttributeItem, TypeEnchantableItem
{
    public ItemCrystalSword() {
        super((IItemTier)CrystalToolTier.getInstance(), 0, 0.0f, new Item.Properties().setNoRepair().func_200918_c(CrystalToolTier.getInstance().func_200926_a()).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> stacks) {
        if (this.func_194125_a(group)) {
            final ItemStack stack = new ItemStack((ItemLike)this);
            CrystalPropertiesAS.CREATIVE_CRYSTAL_TOOL_ATTRIBUTES.store(stack);
            stacks.add((Object)stack);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            attr.addTooltip(tooltip, CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_TOOL_DURABILITY).addUsage(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS).build());
        }
        super.func_77624_a(stack, world, (List)tooltip, flag);
    }
    
    public int getMaxDamage(final ItemStack stack) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            return CrystalCalculations.getToolDurability(super.getMaxDamage(stack), stack);
        }
        return super.getMaxDamage(stack);
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        final EnchantmentCategory type = enchantment.field_77351_y;
        return type == EnchantmentCategory.WEAPON || type == EnchantmentCategory.BREAKABLE;
    }
    
    public boolean canHarvestBlock(final ItemStack stack, final BlockState state) {
        return state.getBlock() instanceof WebBlock;
    }
    
    public float func_200894_d() {
        return CrystalToolTier.getInstance().func_200929_c();
    }
    
    public float getAttackDamage(final ItemStack stack) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            return CrystalCalculations.getToolEfficiency(this.func_200894_d(), stack);
        }
        return this.func_200894_d();
    }
    
    private double getAttackSpeed() {
        return -2.4;
    }
    
    @Nullable
    public CrystalAttributes getAttributes(final ItemStack stack) {
        return CrystalAttributes.getCrystalAttributes(stack);
    }
    
    public void setAttributes(final ItemStack stack, @Nullable final CrystalAttributes attributes) {
        if (attributes != null) {
            attributes.store(stack);
        }
        else {
            CrystalAttributes.storeNull(stack);
        }
    }
    
    public boolean isRepairable(final ItemStack stack) {
        return false;
    }
    
    public boolean func_82789_a(final ItemStack toRepair, final ItemStack repair) {
        return false;
    }
    
    public boolean canEnchantItem(final ItemStack stack, final EnchantmentCategory type) {
        return type == EnchantmentCategory.BREAKABLE || type == EnchantmentCategory.WEAPON;
    }
    
    public int getItemEnchantability(final ItemStack stack) {
        return CrystalToolTier.getInstance().func_200927_e();
    }
    
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlot slot, final ItemStack stack) {
        final Multimap<Attribute, AttributeModifier> multimap = (Multimap<Attribute, AttributeModifier>)HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND) {
            multimap.put((Object)Attributes.field_233823_f_, (Object)new AttributeModifier(ItemCrystalSword.field_111210_e, "Tool modifier", (double)this.getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            multimap.put((Object)Attributes.field_233825_h_, (Object)new AttributeModifier(ItemCrystalSword.field_185050_h, "Tool modifier", this.getAttackSpeed(), AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
}
