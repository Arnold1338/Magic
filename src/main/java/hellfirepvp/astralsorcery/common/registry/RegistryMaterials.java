package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.lib.MaterialsAS;
import hellfirepvp.astralsorcery.common.block.base.MaterialBuilderAS;
import net.minecraft.world.level.level.material.MapColor;

public class RegistryMaterials
{
    private RegistryMaterials() {
    }
    
    public static void init() {
        MaterialsAS.MARBLE = new MaterialBuilderAS(MaterialColor.field_193561_M).build();
        MaterialsAS.BLACK_MARBLE = new MaterialBuilderAS(MaterialColor.field_151646_E).build();
        MaterialsAS.INFUSED_WOOD = new MaterialBuilderAS(MaterialColor.field_151650_B).flammable().build();
    }
}
