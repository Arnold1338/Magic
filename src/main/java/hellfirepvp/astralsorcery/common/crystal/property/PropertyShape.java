package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyShape extends CrystalProperty
{
    public PropertyShape() {
        super(AstralSorcery.key("shape"));
        this.setRequiredResearch(ResearchProgression.BASIC_CRAFT);
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS)) {
                return value * (1.0 + 0.1f * Math.min(propertyLevel, 6));
            }
            else {
                return value;
            }
        });
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL));
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL) || context.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT)) {
                return value * (1.0 + 0.25f * propertyLevel);
            }
            else {
                return value;
            }
        });
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_LENS_EFFECT));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_LENS_EFFECT)) {
                return value * (1.0 + 0.2f * Math.min(propertyLevel, this.getMaxTier()));
            }
            else {
                return value;
            }
        });
    }
}
