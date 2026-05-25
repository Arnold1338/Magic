package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.Item;

public class ItemAquamarine extends Item
{
    public ItemAquamarine() {
        super(new Item.Properties().hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
}
