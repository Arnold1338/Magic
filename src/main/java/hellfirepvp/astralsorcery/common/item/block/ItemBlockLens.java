package hellfirepvp.astralsorcery.common.item.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;

public class ItemBlockLens extends ItemBlockCustom implements CrystalAttributeItem
{
    public ItemBlockLens(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
        final CrystalAttributes attr = this.getAttributes(stack);
        if (attr != null) {
            attr.addTooltip(tooltip, CalculationContext.Builder.newBuilder().addUsage(CrystalPropertiesAS.Usages.USE_LENS_EFFECT).addUsage(CrystalPropertiesAS.Usages.USE_LENS_TRANSFER).build());
        }
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            final ItemStack lens = new ItemStack((ItemLike)this);
            this.setAttributes(lens, CrystalPropertiesAS.LENS_PRISM_CREATIVE_ATTRIBUTES);
            items.add((Object)lens);
        }
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
