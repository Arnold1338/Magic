package hellfirepvp.astralsorcery.common.item.crystal;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.entity.item.EntityCrystal;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalGenerator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeGenItem;
import net.minecraft.world.item.Item;

public abstract class ItemCrystalBase extends Item implements CrystalAttributeGenItem
{
    public ItemCrystalBase(final Item.Properties prop) {
        super(prop.func_200918_c(0));
    }
    
    public void func_77663_a(final ItemStack stack, final World world, final Entity entity, final int slot, final boolean isSelected) {
        if (!world.func_201670_d()) {
            CrystalAttributes attributes = this.getAttributes(stack);
            if (attributes == null && stack.getItem() instanceof CrystalAttributeGenItem) {
                attributes = CrystalGenerator.generateNewAttributes(stack);
                attributes.store(stack);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final World world, final List<Component> toolTip, final TooltipFlag flag) {
        this.addCrystalPropertyToolTip(stack, toolTip);
    }
    
    @OnlyIn(Dist.CLIENT)
    protected CrystalAttributes.TooltipResult addCrystalPropertyToolTip(final ItemStack stack, final List<Component> tooltip) {
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            return attr.addTooltip(tooltip);
        }
        return CrystalAttributes.TooltipResult.ALL_MISSING;
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
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return true;
    }
    
    @Nullable
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        final EntityCrystal res = new EntityCrystal(EntityTypesAS.ITEM_CRYSTAL, world, location.func_226277_ct_(), location.func_226278_cu_(), location.func_226281_cx_(), itemstack);
        res.func_70020_e(location.func_189511_e(new CompoundTag()));
        res.applyColor(this.getItemEntityColor(itemstack));
        if (location instanceof ItemEntity) {
            res.setReplacedEntity((ItemEntity)location);
        }
        return (Entity)res;
    }
    
    public int getGeneratedPropertyTiers() {
        return 5;
    }
    
    public int getMaxPropertyTiers() {
        return 7;
    }
    
    protected Color getItemEntityColor(final ItemStack stack) {
        return ColorsAS.ROCK_CRYSTAL;
    }
    
    public abstract ItemAttunedCrystalBase getTunedItemVariant();
    
    public abstract ItemCrystalBase getInertDuplicateItem();
}
