package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyToolEfficiency extends CrystalProperty
{
    public PropertyToolEfficiency() {
        super(AstralSorcery.key("tool.efficiency"));
        this.setRequiredResearch(ResearchProgression.BASIC_CRAFT);
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS));
        this.addModifier((value, originalValue, propertyLevel, context) -> context.withUse(CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS, value, () -> value * (1.0 + 0.15 * Math.min(propertyLevel, 4))));
    }
}
