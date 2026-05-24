package hellfirepvp.astralsorcery.common.crystal.property;

import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;

public class PropertyRitualEffect extends CrystalProperty
{
    public PropertyRitualEffect() {
        super(AstralSorcery.key("ritual.effect"));
        this.setRequiredResearch(ResearchProgression.ATTUNEMENT);
        this.addUsage(ctx -> ctx.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT));
        this.addModifier((value, originalValue, propertyLevel, context) -> {
            if (context.uses(CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT)) {
                return value * (1.0 + 0.3 * propertyLevel);
            }
            else {
                return value;
            }
        });
    }
}
