package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.util.BlockArray;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.lib.StructuresAS;

public class RegistryStructureTypes
{
    private RegistryStructureTypes() {
    }
    
    public static void init() {
        StructureTypesAS.EMPTY = registerAS("empty", () -> StructuresAS.EMPTY);
        StructureTypesAS.PTYPE_ALTAR_ATTUNEMENT = registerAS("pattern_altar_attunement", () -> StructuresAS.STRUCT_ALTAR_ATTUNEMENT);
        StructureTypesAS.PTYPE_ALTAR_CONSTELLATION = registerAS("pattern_altar_constellation", () -> StructuresAS.STRUCT_ALTAR_CONSTELLATION);
        StructureTypesAS.PTYPE_ALTAR_TRAIT = registerAS("pattern_altar_trait", () -> StructuresAS.STRUCT_ALTAR_TRAIT);
        StructureTypesAS.PTYPE_RITUAL_PEDESTAL = registerAS("pattern_ritual_pedestal", () -> StructuresAS.STRUCT_RITUAL_PEDESTAL);
        StructureTypesAS.PTYPE_INFUSER = registerAS("pattern_infuser", () -> StructuresAS.STRUCT_INFUSER);
        StructureTypesAS.PTYPE_ENHANCED_COLLECTOR_CRYSTAL = registerAS("pattern_enhanced_collector_crystal", () -> StructuresAS.STRUCT_ENHANCED_COLLECTOR_CRYSTAL);
        StructureTypesAS.PTYPE_SPECTRAL_RELAY = registerAS("pattern_spectral_relay", () -> StructuresAS.STRUCT_SPECTRAL_RELAY);
        StructureTypesAS.PTYPE_ATTUNEMENT_ALTAR = registerAS("pattern_attunement_altar", () -> StructuresAS.STRUCT_ATTUNEMENT_ALTAR);
        StructureTypesAS.PTYPE_CELESTIAL_GATEWAY = registerAS("pattern_celestial_gateway", () -> StructuresAS.STRUCT_CELESTIAL_GATEWAY);
        StructureTypesAS.PTYPE_SINGULARITY = registerAS("pattern_singularity", () -> StructuresAS.STRUCT_SINGULARITY);
        StructureTypesAS.PTYPE_FOUNTAIN = registerAS("pattern_fountain", () -> StructuresAS.STRUCT_FOUNTAIN);
    }
    
    private static StructureType registerAS(final String name, final Supplier<BlockArray> structureSupplier) {
        return register(AstralSorcery.key(name), structureSupplier);
    }
    
    private static StructureType register(final ResourceLocation name, final Supplier<BlockArray> structureSupplier) {
        final StructureType type = new StructureType(name, structureSupplier);
        AstralSorcery.getProxy().getRegistryPrimer().register(type);
        return type;
    }
}
