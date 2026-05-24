package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCustom;
import net.minecraft.world.item.BlockItem;

public interface CustomItemBlock
{
    default Class<? extends BlockItem> getItemBlockClass() {
        return ItemBlockCustom.class;
    }
    
    default BlockItem createItemBlock(final Item.Properties properties) {
        final Class<?> itemBlockClass = this.getItemBlockClass();
        try {
            return (BlockItem)itemBlockClass.getConstructor(Block.class, Item.Properties.class).newInstance(this, properties);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("Cannot instantiate ItemBlock!", e);
        }
    }
}
