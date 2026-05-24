package hellfirepvp.astralsorcery.common.world;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;

public abstract class TemplateStructureFeature extends Structure<NoneFeatureConfiguration>
{
    public TemplateStructureFeature() {
        super(NoneFeatureConfiguration.field_236558_a_);
    }
    
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }
    
    public String func_143025_a() {
        return this.getRegistryName().toString();
    }
}
