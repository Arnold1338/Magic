package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;

public class RegistryCrystalPropertyUsages
{
    private RegistryCrystalPropertyUsages() {
    }
    
    public static void init() {
        CrystalPropertiesAS.Usages.USE_RITUAL_EFFECT = createUsage("ritual.effect");
        CrystalPropertiesAS.Usages.USE_RITUAL_RANGE = createUsage("ritual.range");
        CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL = createUsage("collector");
        CrystalPropertiesAS.Usages.USE_LENS_TRANSFER = createUsage("lens.transfer");
        CrystalPropertiesAS.Usages.USE_LENS_EFFECT = createUsage("lens.effect");
        CrystalPropertiesAS.Usages.USE_TOOL_DURABILITY = createUsage("tool.durability");
        CrystalPropertiesAS.Usages.USE_TOOL_EFFECTIVENESS = createUsage("tool.effectiveness");
    }
    
    private static <T extends PropertyUsage> T createUsage(final String plainName) {
        return registerUsage(new PropertyUsage(AstralSorcery.key(plainName)));
    }
    
    private static <T extends PropertyUsage> T registerUsage(final T usage) {
        AstralSorcery.getProxy().getRegistryPrimer().register(usage);
        return usage;
    }
}
