package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemGlassLens extends Item
{
    public ItemGlassLens() {
        super(new Item.Properties().hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
}
