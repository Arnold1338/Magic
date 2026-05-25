package hellfirepvp.astralsorcery.common.item.block;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItemUseContext;
import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.block.tile.BlockCelestialCrystalCluster;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
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
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeGenItem;

public class ItemBlockCelestialCrystalCluster extends ItemBlockCustom implements CrystalAttributeGenItem
{
    public ItemBlockCelestialCrystalCluster(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties.func_208103_a(CommonProxy.RARITY_CELESTIAL));
    }
    
    public void func_77663_a(final ItemStack stack, final Level world, final Entity entity, final int slot, final boolean isSelected) {
        if (!world.level()) {
            CrystalAttributes attributes = this.getAttributes(stack);
            if (attributes == null && stack.getItem() instanceof CrystalAttributeGenItem) {
                attributes = CrystalGenerator.generateNewAttributes(stack);
                attributes.store(stack);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        super.func_77624_a(stack, worldIn, (List)tooltip, flagIn);
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            attr.addTooltip(tooltip);
        }
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            for (final int stage : BlockCelestialCrystalCluster.STAGE.func_177700_c()) {
                final ItemStack cluster = new ItemStack((ItemLike)this);
                this.setDamage(cluster, stage);
                items.add((Object)cluster);
            }
        }
    }
    
    @Nullable
    protected BlockState func_195945_b(final BlockItemUseContext context) {
        final BlockState toPlace = super.func_195945_b(context);
        if (toPlace != null) {
            return (BlockState)toPlace.setValue((Property)BlockCelestialCrystalCluster.STAGE, (Comparable)this.getDamage(context.func_195996_i()));
        }
        return null;
    }
    
    @Override
    public int getGeneratedPropertyTiers() {
        return ItemsAS.CELESTIAL_CRYSTAL.getGeneratedPropertyTiers();
    }
    
    @Override
    public int getMaxPropertyTiers() {
        return ItemsAS.CELESTIAL_CRYSTAL.getMaxPropertyTiers();
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
}
