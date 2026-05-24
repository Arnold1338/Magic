package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyPurity extends CrystalProperty
{
    public PropertyPurity() {
        super(AstralSorcery.key("purity"));
        this.setRequiredResearch(ResearchProgression.BASIC_CRAFT);
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_LENS_TRANSFER));
        this.addModifier((value, originalValue, propertyLevel, context) -> context.withUse(CrystalPropertiesAS.Usages.USE_LENS_TRANSFER, value, () -> value * (1.0 + 0.16666666666666666 * propertyLevel)));
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL));
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT));
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_RITUAL_RANGE));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL) || context.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT) || context.uses(CrystalPropertiesAS.Usages.USE_RITUAL_RANGE)) {
                return value * (1.0 + 0.4 * propertyLevel);
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
