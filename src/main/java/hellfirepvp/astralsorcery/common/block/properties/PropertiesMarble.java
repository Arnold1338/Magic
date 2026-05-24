package hellfirepvp.astralsorcery.common.block.properties;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.lib.MaterialsAS;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PropertiesMarble
{
    public static AbstractBlock.Properties defaultMarble() {
        return AbstractBlock.Properties.func_200945_a(MaterialsAS.MARBLE).func_200948_a(3.0f, 5.0f).harvestLevel(0).func_235861_h_().harvestTool(ToolType.PICKAXE).func_200947_a(SoundType.field_185851_d);
    }
    
    public static AbstractBlock.Properties defaultBlackMarble() {
        return AbstractBlock.Properties.func_200945_a(MaterialsAS.MARBLE).func_200948_a(3.0f, 5.0f).harvestLevel(0).func_235861_h_().harvestTool(ToolType.PICKAXE).func_200947_a(SoundType.field_185851_d);
    }
}
