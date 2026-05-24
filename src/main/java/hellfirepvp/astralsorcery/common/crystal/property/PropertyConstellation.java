package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.crystal.source.Ritual;
import hellfirepvp.astralsorcery.common.crystal.source.Crystal;
import hellfirepvp.astralsorcery.common.crystal.source.AttunedSourceInstance;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyConstellation extends CrystalProperty
{
    private final IWeakConstellation cst;
    
    public PropertyConstellation(final IWeakConstellation cst) {
        super(AstralSorcery.key("constellation." + cst.getSimpleName()));
        this.cst = cst;
        this.setRequiredResearch(ResearchProgression.ATTUNEMENT);
        this.addUsage(ctx -> (ctx.isSource(CrystalPropertiesAS.Sources.SOURCE_COLLECTOR_CRYSTAL) || ctx.isSource(CrystalPropertiesAS.Sources.SOURCE_TILE_COLLECTOR_CRYSTAL)) && cst.equals(ctx.getSource().getAttunedConstellation()));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.isSource(CrystalPropertiesAS.Sources.SOURCE_COLLECTOR_CRYSTAL) || context.isSource(CrystalPropertiesAS.Sources.SOURCE_TILE_COLLECTOR_CRYSTAL)) {
                final Crystal crystal = context.getSource();
                if (crystal != null && cst.equals(crystal.getAttunedConstellation())) {
                    return value * (1.0 + 0.3 * propertyLevel);
                }
            }
            return value;
        });
        this.addUsage(ctx -> (ctx.isSource(CrystalPropertiesAS.Sources.SOURCE_RITUAL_PEDESTAL) || ctx.isSource(CrystalPropertiesAS.Sources.SOURCE_TILE_RITUAL_PEDESTAL)) && cst.equals(ctx.getSource().getAttunedConstellation()));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.isSource(CrystalPropertiesAS.Sources.SOURCE_RITUAL_PEDESTAL) || context.isSource(CrystalPropertiesAS.Sources.SOURCE_TILE_RITUAL_PEDESTAL)) {
                final Ritual ritual = context.getSource();
                if (ritual != null && cst.equals(ritual.getAttunedConstellation())) {
                    return value * (1.0 + 0.35 * propertyLevel);
                }
            }
            return value;
        });
    }
    
    public IWeakConstellation getConstellation() {
        return this.cst;
    }
    
    @Override
    public int getMaxTier() {
        return 2;
    }
    
    @Override
    public boolean canSee(final PlayerProgress progress) {
        return super.canSee(progress) && progress.hasConstellationDiscovered(this.cst);
    }
}
