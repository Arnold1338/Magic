package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemParchment extends Item
{
    public ItemParchment() {
        super(new Item.Properties().hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
}
