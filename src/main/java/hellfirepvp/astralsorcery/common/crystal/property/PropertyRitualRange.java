package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyRitualRange extends CrystalProperty
{
    public PropertyRitualRange() {
        super(AstralSorcery.key("ritual.range"));
        this.setRequiredResearch(ResearchProgression.ATTUNEMENT);
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_RITUAL_RANGE));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_RITUAL_RANGE)) {
                return value * (1.0 + 0.1 * propertyLevel);
            }
            else {
                return value;
            }
        });
    }
    
    @Override
    public int getMaxTier() {
        return 2;
    }
}
