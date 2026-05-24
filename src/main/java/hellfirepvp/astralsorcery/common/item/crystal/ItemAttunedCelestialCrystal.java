package hellfirepvp.astralsorcery.common.item.crystal;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.level.item.Item;

public class ItemAttunedCelestialCrystal extends ItemAttunedCrystalBase
{
    public ItemAttunedCelestialCrystal() {
        super(new Item.Properties().func_208103_a(CommonProxy.RARITY_CELESTIAL).func_200916_a(CommonProxy.ITEM_GROUP_AS_CRYSTALS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            for (final IWeakConstellation cst : ConstellationRegistry.getWeakConstellations()) {
                final ItemStack stack = new ItemStack((ItemLike)this);
                this.setAttunedConstellation(stack, cst);
                items.add((Object)stack);
            }
        }
    }
    
    @Override
    public int getGeneratedPropertyTiers() {
        return 8;
    }
    
    @Override
    public int getMaxPropertyTiers() {
        return 10;
    }
    
    @Override
    protected Color getItemEntityColor(final ItemStack stack) {
        return ColorsAS.CELESTIAL_CRYSTAL;
    }
    
    @Override
    public ItemAttunedCrystalBase getTunedItemVariant() {
        return ItemsAS.ATTUNED_CELESTIAL_CRYSTAL;
    }
    
    @Override
    public ItemCrystalBase getInertDuplicateItem() {
        return ItemsAS.CELESTIAL_CRYSTAL;
    }
}
