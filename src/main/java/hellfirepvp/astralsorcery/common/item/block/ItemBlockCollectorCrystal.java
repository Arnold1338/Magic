package hellfirepvp.astralsorcery.common.item.block;

import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crystal.CrystalPropertyRegistry;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;

public abstract class ItemBlockCollectorCrystal extends ItemBlockCustom implements CrystalAttributeItem, ConstellationItem
{
    public ItemBlockCollectorCrystal(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties.func_200916_a(CommonProxy.ITEM_GROUP_AS_CRYSTALS).func_200917_a(1));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> stacks) {
        if (this.func_194125_a(group)) {
            for (final IWeakConstellation cst : ConstellationRegistry.getWeakConstellations()) {
                final ItemStack stack = new ItemStack((ItemLike)this);
                this.setAttunedConstellation(stack, cst);
                final CrystalProperty prop = CrystalPropertyRegistry.INSTANCE.getConstellationProperty(cst);
                CrystalAttributes attr = this.getCreativeTemplateAttributes();
                if (prop != null) {
                    attr = attr.modifyLevel(prop, prop.getMaxTier());
                }
                attr.store(stack);
                stacks.add((Object)stack);
            }
        }
    }
    
    public Component func_200295_i(final ItemStack stack) {
        final IWeakConstellation cst = this.getAttunedConstellation(stack);
        if (cst != null) {
            return (Component)new Component(super.func_77667_c(stack) + ".typed", new Object[] { cst.getConstellationName() });
        }
        return super.func_200295_i(stack);
    }
    
    public abstract CollectorCrystalType getCollectorType();
    
    protected abstract CrystalAttributes getCreativeTemplateAttributes();
    
    @Nullable
    @Override
    public IWeakConstellation getAttunedConstellation(final ItemStack stack) {
        return (IWeakConstellation)IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), "constellation");
    }
    
    @Override
    public boolean setAttunedConstellation(final ItemStack stack, @Nullable final IWeakConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), "constellation");
        }
        else {
            NBTHelper.getPersistentData(stack).func_82580_o("constellation");
        }
        return true;
    }
    
    @Nullable
    @Override
    public IMinorConstellation getTraitConstellation(final ItemStack stack) {
        return (IMinorConstellation)IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), "trait");
    }
    
    @Override
    public boolean setTraitConstellation(final ItemStack stack, @Nullable final IMinorConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), "trait");
        }
        else {
            NBTHelper.getPersistentData(stack).func_82580_o("trait");
        }
        return true;
    }
    
    @Nullable
    @Override
    public CrystalAttributes getAttributes(final ItemStack stack) {
        return CrystalAttributes.getCrystalAttributes(stack);
    }
    
    @Override
    public void setAttributes(final ItemStack stack, @Nullable final CrystalAttributes attributes) {
        if (attributes != null) {
            attributes.store(stack);
        }
        else {
            CrystalAttributes.storeNull(stack);
        }
    }
}
