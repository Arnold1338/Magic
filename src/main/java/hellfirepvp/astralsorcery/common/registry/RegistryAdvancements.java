package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.advancement.PerkLevelTrigger;
import hellfirepvp.astralsorcery.common.advancement.AltarCraftTrigger;
import hellfirepvp.astralsorcery.common.advancement.AttuneCrystalTrigger;
import hellfirepvp.astralsorcery.common.advancement.AttuneSelfTrigger;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import hellfirepvp.astralsorcery.common.lib.AdvancementsAS;
import hellfirepvp.astralsorcery.common.advancement.DiscoverConstellationTrigger;

public class RegistryAdvancements
{
    public static void init() {
        CriteriaTriggers.func_192118_a((ICriterionTrigger)(AdvancementsAS.DISCOVER_CONSTELLATION = new DiscoverConstellationTrigger()));
        CriteriaTriggers.func_192118_a((ICriterionTrigger)(AdvancementsAS.ATTUNE_SELF = new AttuneSelfTrigger()));
        CriteriaTriggers.func_192118_a((ICriterionTrigger)(AdvancementsAS.ATTUNE_CRYSTAL = new AttuneCrystalTrigger()));
        CriteriaTriggers.func_192118_a((ICriterionTrigger)(AdvancementsAS.ALTAR_CRAFT = new AltarCraftTrigger()));
        CriteriaTriggers.func_192118_a((ICriterionTrigger)(AdvancementsAS.PERK_LEVEL = new PerkLevelTrigger()));
    }
}
