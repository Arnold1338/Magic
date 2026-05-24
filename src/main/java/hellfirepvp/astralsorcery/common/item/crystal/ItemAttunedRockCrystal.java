package hellfirepvp.astralsorcery.common.item.crystal;

import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemAttunedRockCrystal extends ItemAttunedCrystalBase
{
    public ItemAttunedRockCrystal() {
        super(new Item.Properties().func_200916_a(CommonProxy.ITEM_GROUP_AS_CRYSTALS));
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
    public ItemAttunedCrystalBase getTunedItemVariant() {
        return ItemsAS.ATTUNED_ROCK_CRYSTAL;
    }
    
    @Override
    public ItemCrystalBase getInertDuplicateItem() {
        return ItemsAS.ROCK_CRYSTAL;
    }
}
