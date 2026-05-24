package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.advancement.PerkLevelTrigger;
import hellfirepvp.astralsorcery.common.advancement.AltarCraftTrigger;
import hellfirepvp.astralsorcery.common.advancement.AttuneCrystalTrigger;
import hellfirepvp.astralsorcery.common.advancement.AttuneSelfTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import hellfirepvp.astralsorcery.common.lib.AdvancementsAS;
import hellfirepvp.astralsorcery.common.advancement.DiscoverConstellationTrigger;

public class RegistryAdvancements
{
    public static void init() {
        CriteriaTriggers.func_192118_a((CriterionTrigger)(AdvancementsAS.DISCOVER_CONSTELLATION = new DiscoverConstellationTrigger()));
        CriteriaTriggers.func_192118_a((CriterionTrigger)(AdvancementsAS.ATTUNE_SELF = new AttuneSelfTrigger()));
        CriteriaTriggers.func_192118_a((CriterionTrigger)(AdvancementsAS.ATTUNE_CRYSTAL = new AttuneCrystalTrigger()));
        CriteriaTriggers.func_192118_a((CriterionTrigger)(AdvancementsAS.ALTAR_CRAFT = new AltarCraftTrigger()));
        CriteriaTriggers.func_192118_a((CriterionTrigger)(AdvancementsAS.PERK_LEVEL = new PerkLevelTrigger()));
    }
}
