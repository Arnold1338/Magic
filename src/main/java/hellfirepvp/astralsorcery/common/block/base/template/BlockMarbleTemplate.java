package hellfirepvp.astralsorcery.common.block.base.template;

import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.level.block.Block;

public class BlockMarbleTemplate extends Block implements CustomItemBlock
{
    public BlockMarbleTemplate() {
        super(PropertiesMarble.defaultMarble());
    }
}
