package hellfirepvp.astralsorcery.common.block.base.template;

import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.level.block.Block;

public class BlockBlackMarbleTemplate extends Block implements CustomItemBlock
{
    public BlockBlackMarbleTemplate() {
        super(PropertiesMarble.defaultBlackMarble());
    }
}
