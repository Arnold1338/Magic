package hellfirepvp.astralsorcery.common.block.properties;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PropertiesMisc
{
    public static AbstractBlock.Properties defaultAir() {
        return AbstractBlock.Properties.func_200949_a(Material.field_151579_a, MaterialColor.field_151660_b).func_200942_a();
    }
    
    public static AbstractBlock.Properties defaultSand() {
        return AbstractBlock.Properties.func_200949_a(Material.field_151595_p, MaterialColor.field_151658_d).func_200943_b(0.5f).harvestTool(ToolType.SHOVEL).func_200947_a(SoundType.field_185855_h);
    }
    
    public static AbstractBlock.Properties defaultRock() {
        return AbstractBlock.Properties.func_200949_a(Material.field_151576_e, MaterialColor.field_151665_m).func_200948_a(1.5f, 6.0f).harvestTool(ToolType.PICKAXE).func_200947_a(SoundType.field_185851_d);
    }
    
    public static AbstractBlock.Properties defaultMetal(final MaterialColor color) {
        return AbstractBlock.Properties.func_200949_a(Material.field_151573_f, color).func_200948_a(1.5f, 6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1).func_200947_a(SoundType.field_185852_e);
    }
    
    public static AbstractBlock.Properties defaultPlant() {
        return AbstractBlock.Properties.func_200945_a(Material.field_151585_k).func_200942_a().func_200943_b(0.0f).func_200947_a(SoundType.field_185850_c);
    }
    
    public static AbstractBlock.Properties defaultTickingPlant() {
        return AbstractBlock.Properties.func_200945_a(Material.field_151585_k).func_200942_a().func_200944_c().func_200943_b(0.0f).func_200947_a(SoundType.field_185850_c);
    }
    
    public static AbstractBlock.Properties defaultGoldMachinery() {
        return AbstractBlock.Properties.func_200949_a(Material.field_151573_f, MaterialColor.field_151647_F).func_200948_a(1.0f, 4.0f).func_200947_a(SoundType.field_185851_d);
    }
}
