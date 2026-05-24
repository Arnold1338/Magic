package hellfirepvp.astralsorcery.common.block.properties;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PropertiesGlass
{
    public static AbstractBlock.Properties coatedGlass() {
        return AbstractBlock.Properties.func_200945_a(Material.field_151592_s).func_200948_a(1.0f, 5.0f).func_200947_a(SoundType.field_185853_f);
    }
}
