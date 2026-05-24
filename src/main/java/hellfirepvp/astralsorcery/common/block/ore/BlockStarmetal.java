package hellfirepvp.astralsorcery.common.block.ore;

import hellfirepvp.astralsorcery.common.block.properties.PropertiesMisc;
import net.minecraft.world.level.material.MapColor;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import net.minecraft.world.level.block.Block;

public class BlockStarmetal extends Block implements CustomItemBlock
{
    public BlockStarmetal() {
        super(PropertiesMisc.defaultMetal(MaterialColor.field_151649_A));
    }
}
