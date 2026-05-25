package hellfirepvp.astralsorcery.common.item.crystal;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemCelestialCrystal extends ItemCrystalBase
{
    public ItemCelestialCrystal() {
        super(new Item.Properties().hasModifier(CommonProxy.ITEM_GROUP_AS).func_208103_a(CommonProxy.RARITY_CELESTIAL));
    }
    
    @Override
    protected Color getItemEntityColor(final ItemStack stack) {
        return ColorsAS.CELESTIAL_CRYSTAL;
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
    public ItemAttunedCrystalBase getTunedItemVariant() {
        return ItemsAS.ATTUNED_CELESTIAL_CRYSTAL;
    }
    
    @Override
    public ItemCrystalBase getInertDuplicateItem() {
        return ItemsAS.CELESTIAL_CRYSTAL;
    }
}
