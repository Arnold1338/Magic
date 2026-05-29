package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravingEffect;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class RegistryRegistries {
    private RegistryRegistries() {}

    @SubscribeEvent
    public static void buildRegistries(final NewRegistryEvent event) {
        RegistriesAS.REGISTRY_CONSTELLATIONS = event.create(
            new RegistryBuilder<IConstellation>()
                .setName(RegistriesAS.REGISTRY_NAME_CONSTELLATIONS)
                .setType(IConstellation.class)
                .addCallback((owner, stage, id, key, obj, oldObj) -> ConstellationRegistry.addConstellation(obj))
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_CONSTELLATION_EFFECT = event.create(
            new RegistryBuilder<ConstellationEffectProvider>()
                .setName(RegistriesAS.REGISTRY_NAME_CONSTELLATION_EFFECTS)
                .setType(ConstellationEffectProvider.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_MANTLE_EFFECT = event.create(
            new RegistryBuilder<MantleEffect>()
                .setName(RegistriesAS.REGISTRY_NAME_MANTLE_EFFECTS)
                .setType(MantleEffect.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_ENGRAVING_EFFECT = event.create(
            new RegistryBuilder<EngravingEffect>()
                .setName(RegistriesAS.REGISTRY_NAME_ENGRAVING_EFFECT)
                .setType(EngravingEffect.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_CONVERTERS = event.create(
            new RegistryBuilder<PerkConverter>()
                .setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_CONVERTERS)
                .setType(PerkConverter.class)
                .disableSaving().disableOverrides().allowModification()
        );
        RegistriesAS.REGISTRY_PERK_CUSTOM_MODIFIERS = event.create(
            new RegistryBuilder<PerkAttributeModifier>()
                .setName(RegistriesAS.REGISTRY_NAME_PERK_CUSTOM_MODIFIERS)
                .setType(PerkAttributeModifier.class)
                .disableSaving().disableOverrides().allowModification()
        );
        RegistriesAS.REGISTRY_STRUCTURE_TYPES = event.create(
            new RegistryBuilder<StructureType>()
                .setName(RegistriesAS.REGISTRY_NAME_STRUCTURE_TYPES)
                .setType(StructureType.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES = event.create(
            new RegistryBuilder<PerkAttributeType>()
                .setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_TYPES)
                .setType(PerkAttributeType.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_READERS = event.create(
            new RegistryBuilder<PerkAttributeReader>()
                .setName(RegistriesAS.REGISTRY_NAME_PERK_ATTRIBUTE_READERS)
                .setType(PerkAttributeReader.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES = event.create(
            new RegistryBuilder<CrystalProperty>()
                .setName(RegistriesAS.REGISTRY_NAME_CRYSTAL_PROPERTIES)
                .setType(CrystalProperty.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_CRYSTAL_USAGES = event.create(
            new RegistryBuilder<PropertyUsage>()
                .setName(RegistriesAS.REGISTRY_NAME_CRYSTAL_USAGES)
                .setType(PropertyUsage.class)
                .disableSaving().disableOverrides()
        );
        RegistriesAS.REGISTRY_ALTAR_EFFECTS = event.create(
            new RegistryBuilder<AltarRecipeEffect>()
                .setName(RegistriesAS.REGISTRY_NAME_ALTAR_EFFECTS)
                .setType(AltarRecipeEffect.class)
                .disableSaving().disableOverrides()
        );
    }
}
