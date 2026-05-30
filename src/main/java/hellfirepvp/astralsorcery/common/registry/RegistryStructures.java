package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.observerlib.common.change.ObserverProviderStructure;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.astralsorcery.common.structure.PatternFountain;
import hellfirepvp.astralsorcery.common.structure.PatternSingularity;
import hellfirepvp.astralsorcery.common.structure.PatternCelestialGateway;
import hellfirepvp.astralsorcery.common.structure.PatternAttunementAltar;
import hellfirepvp.astralsorcery.common.structure.PatternSpectralRelay;
import hellfirepvp.astralsorcery.common.structure.PatternEnhancedCollectorCrystal;
import hellfirepvp.astralsorcery.common.structure.PatternInfuser;
import hellfirepvp.astralsorcery.common.structure.PatternRitualPedestal;
import hellfirepvp.astralsorcery.common.structure.PatternAltarTrait;
import hellfirepvp.astralsorcery.common.structure.PatternAltarConstellation;
import hellfirepvp.astralsorcery.common.structure.PatternAltarAttunement;
import hellfirepvp.astralsorcery.common.lib.StructuresAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class RegistryStructures
{
    private RegistryStructures() {
    }
    
    public static void init() {
        StructuresAS.EMPTY = register(new PatternBlockArray(AstralSorcery.key("empty"));
        StructuresAS.STRUCT_ALTAR_ATTUNEMENT = register(new PatternAltarAttunement());
        StructuresAS.STRUCT_ALTAR_CONSTELLATION = register(new PatternAltarConstellation());
        StructuresAS.STRUCT_ALTAR_TRAIT = register(new PatternAltarTrait());
        StructuresAS.STRUCT_RITUAL_PEDESTAL = register(new PatternRitualPedestal());
        StructuresAS.STRUCT_INFUSER = register(new PatternInfuser());
        StructuresAS.STRUCT_ENHANCED_COLLECTOR_CRYSTAL = register(new PatternEnhancedCollectorCrystal());
        StructuresAS.STRUCT_SPECTRAL_RELAY = register(new PatternSpectralRelay());
        StructuresAS.STRUCT_ATTUNEMENT_ALTAR = register(new PatternAttunementAltar());
        StructuresAS.STRUCT_CELESTIAL_GATEWAY = register(new PatternCelestialGateway());
        StructuresAS.STRUCT_SINGULARITY = register(new PatternSingularity());
        StructuresAS.STRUCT_FOUNTAIN = register(new PatternFountain());
    }
    
    private static <T extends MatchableStructure> T register(final T struct) {
        AstralSorcery.getProxy().getRegistryPrimer().register(struct);
        final ObserverProviderStructure structureProvider = new ObserverProviderStructure(struct.getRegistryName());
        AstralSorcery.getProxy().getRegistryPrimer().register(structureProvider);
        return struct;
    }
}
