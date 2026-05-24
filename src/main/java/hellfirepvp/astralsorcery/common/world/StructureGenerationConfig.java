package hellfirepvp.astralsorcery.common.world;

import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class StructureGenerationConfig extends FeatureGenerationConfig
{
    private final int defaultSpacing;
    private final int defaultSeparation;
    private ForgeConfigSpec.IntValue spacing;
    private ForgeConfigSpec.IntValue separation;
    
    public StructureGenerationConfig(final ResourceLocation featureName, final int spacing, final int separation) {
        this(featureName.func_110623_a(), spacing, separation);
    }
    
    public StructureGenerationConfig(final String featureName, final int spacing, final int separation) {
        super(featureName);
        this.defaultSpacing = spacing;
        this.defaultSeparation = separation;
    }
    
    @Override
    public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
        super.createEntries(cfgBuilder);
        this.spacing = cfgBuilder.comment("Defines the structure spacing for worldgen").translation(this.translationKey("spacing")).defineInRange("spacing", this.defaultSpacing, 1, 512);
        this.separation = cfgBuilder.comment("Defines the structure separation for worldgen").translation(this.translationKey("separation")).defineInRange("separation", this.defaultSeparation, 1, 512);
    }
    
    public StructureSeparationSettings createSettings() {
        return new StructureSeparationSettings((int)this.spacing.get(), (int)this.separation.get(), Math.abs(this.getFullPath().hashCode()));
    }
}
