package hellfirepvp.astralsorcery.common.registry;

import net.minecraftforge.registries.IForgeRegistry;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.IForgeRegistryInternal;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravingEffect;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.event.RegistryEvent;

public class RegistryRegistries
{
    private RegistryRegistries() {
    }
    
    public static void buildRegistries(final RegistryEvent.NewRegistry event) {
        RegistriesAS.REGISTRY_CONSTELLATIONS = (IForgeRegistry<IConstellation>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_CONSTELLATIONS).setType((Class)IConstellation.class).add((owner, stage, id, obj, oldObj) -> ConstellationRegistry.addConstellation(obj)).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_CONSTELLATION_EFFECT = (IForgeRegistry<ConstellationEffectProvider>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_CONSTELLATION_EFFECTS).setType((Class)ConstellationEffectProvider.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_MANTLE_EFFECT = (IForgeRegistry<MantleEffect>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_MANTLE_EFFECTS).setType((Class)MantleEffect.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_ENGRAVING_EFFECT = (IForgeRegistry<EngravingEffect>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_ENGRAVING_EFFECT).setType((Class)EngravingEffect.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_CONVERTERS = (IForgeRegistry<PerkConverter>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_CONVERTERS).setType((Class)PerkConverter.class).disableSaving().disableOverrides().allowModification().create();
        RegistriesAS.REGISTRY_PERK_CUSTOM_MODIFIERS = (IForgeRegistry<PerkAttributeModifier>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_PERK_CUSTOM_MODIFIERS).setType((Class)PerkAttributeModifier.class).disableSaving().disableOverrides().allowModification().create();
        RegistriesAS.REGISTRY_STRUCTURE_TYPES = (IForgeRegistry<StructureType>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_STRUCTURE_TYPES).setType((Class)StructureType.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES = (IForgeRegistry<PerkAttributeType>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_TYPES).setType((Class)PerkAttributeType.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_READERS = (IForgeRegistry<PerkAttributeReader>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_READERS).setType((Class)PerkAttributeReader.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES = (IForgeRegistry<CrystalProperty>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_CRYSTAL_PROPERTIES).setType((Class)CrystalProperty.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_CRYSTAL_USAGES = (IForgeRegistry<PropertyUsage>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_CRYSTAL_USAGES).setType((Class)PropertyUsage.class).disableSaving().disableOverrides().create();
        RegistriesAS.REGISTRY_ALTAR_EFFECTS = (IForgeRegistry<AltarRecipeEffect>)new RegistryBuilder().setName(RegistriesAS.REGISTRY_NAME_ALTAR_EFFECTS).setType((Class)AltarRecipeEffect.class).disableSaving().disableOverrides().create();
    }
}
