package hellfirepvp.astralsorcery.common.block.ore;

import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.level.block.Block;

public class BlockStarmetalOre extends Block implements CustomItemBlock
{
    public BlockStarmetalOre() {
        super(PropertiesMisc.defaultRock().harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }
}
