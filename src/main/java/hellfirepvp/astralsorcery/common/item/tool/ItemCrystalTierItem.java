package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.HashMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import javax.annotation.Nullable;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.material.Material;
import java.util.Set;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import net.minecraft.world.item.Item;

public abstract class ItemCrystalTierItem extends Item implements CrystalAttributeItem
{
    private final Set<Material> effectiveMaterials;
    private final ToolType toolType;
    
    protected ItemCrystalTierItem(@Nullable final ToolType toolType, final Item.Properties prop, final Set<Material> effectiveMaterials) {
        super(withTool(toolType, prop.func_200918_c(CrystalToolTier.getInstance().func_200926_a()).setNoRepair().hasModifier(CommonProxy.ITEM_GROUP_AS)));
        this.effectiveMaterials = effectiveMaterials;
        this.toolType = toolType;
    }
    
    private static Item.Properties withTool(@Nullable final ToolType tool, final Item.Properties prop) {
        if (tool != null) {
            prop.addToolType(tool, CrystalToolTier.getInstance().func_200925_d());
        }
        return prop;
    }
    
    abstract double getAttackDamage();
    
    abstract double getAttackSpeed();
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            attr.addTooltip(tooltip, CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_TOOL_DURABILITY).addUsage(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS).build());
        }
        super.appendHoverText(stack, world, (List)tooltip, flag);
    }
    
    public int getMaxDamage(final ItemStack stack) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            return CrystalCalculations.getToolDurability(super.getMaxDamage(stack), stack);
        }
        return super.getMaxDamage(stack);
    }
    
    public boolean canHarvestBlock(final ItemStack stack, final BlockState state) {
        final int i = CrystalToolTier.getInstance().func_200925_d();
        if (this.toolType != null && state.getHarvestTool() == this.toolType) {
            return i >= state.getHarvestLevel();
        }
        return this.effectiveMaterials.contains(state.func_185904_a());
    }
    
    public float func_150893_a(final ItemStack stack, final BlockState state) {
        float str = super.func_150893_a(stack, state);
        if (this.getToolTypes(stack).stream().noneMatch(state::isToolEffective) && !this.isToolEfficientAgainst(state) && !this.effectiveMaterials.contains(state.func_185904_a())) {
            return str;
        }
        str *= CrystalToolTier.getInstance().func_200928_b();
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            return CrystalCalculations.getToolEfficiency(str, stack);
        }
        return str;
    }
    
    protected boolean isToolEfficientAgainst(final BlockState state) {
        return false;
    }
    
    public boolean func_77644_a(final ItemStack stack, final LivingEntity target, final LivingEntity attacker) {
        stack.func_222118_a(1, attacker, entity -> entity.func_213361_c(EquipmentSlot.MAINHAND));
        return true;
    }
    
    public boolean func_179218_a(final ItemStack stack, final Level worldIn, final BlockState state, final BlockPos pos, final LivingEntity entityLiving) {
        if (!worldIn.isClientSide && state.func_185887_b((IBlockReader)worldIn, pos) != 0.0f) {
            stack.func_222118_a(1, entityLiving, p_220038_0_ -> p_220038_0_.func_213361_c(EquipmentSlot.MAINHAND));
        }
        return true;
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
    
    public boolean func_82789_a(final ItemStack p_82789_1_, final ItemStack p_82789_2_) {
        return false;
    }
    
    public int getItemEnchantability(final ItemStack stack) {
        return CrystalToolTier.getInstance().func_200927_e();
    }
    
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlot slot, final ItemStack stack) {
        final Multimap<Attribute, AttributeModifier> multimap = (Multimap<Attribute, AttributeModifier>)HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND) {
            multimap.put((Object)Attributes.field_233823_f_, (Object)new AttributeModifier(ItemCrystalTierItem.field_111210_e, "Tool modifier", this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            multimap.put((Object)Attributes.field_233825_h_, (Object)new AttributeModifier(ItemCrystalTierItem.field_185050_h, "Tool modifier", this.getAttackSpeed(), AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
}
