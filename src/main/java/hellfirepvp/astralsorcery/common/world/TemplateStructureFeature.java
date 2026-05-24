package hellfirepvp.astralsorcery.common.world;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.level.levelgen.structure.Structure;

public abstract class TemplateStructureFeature extends Structure<NoFeatureConfig>
{
    public TemplateStructureFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }
    
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }
    
    public String func_143025_a() {
        return this.getRegistryName().toString();
    }
}
