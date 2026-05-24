package hellfirepvp.astralsorcery.common.block.properties;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.lib.MaterialsAS;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PropertiesWood
{
    public static AbstractBlock.Properties defaultInfusedWood() {
        return AbstractBlock.Properties.func_200945_a(MaterialsAS.INFUSED_WOOD).func_200948_a(2.5f, 7.0f).harvestTool(ToolType.AXE).func_200947_a(SoundType.field_185848_a);
    }
}
