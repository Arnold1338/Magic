package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.effect.AltarRecipeEffect;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravingEffect;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.resources.ResourceLocation;

public class RegistriesAS
{
    public static final ResourceLocation REGISTRY_NAME_CONSTELLATIONS;
    public static final ResourceLocation REGISTRY_NAME_CONSTELLATION_EFFECTS;
    public static final ResourceLocation REGISTRY_NAME_MANTLE_EFFECTS;
    public static final ResourceLocation REGISTRY_NAME_ENGRAVING_EFFECT;
    public static final ResourceLocation REGISTRY_NAME_STRUCTURE_TYPES;
    public static final ResourceLocation REGISTRY_NAME_PERK_ATTRIBUTE_TYPES;
    public static final ResourceLocation REGISTRY_NAME_PERK_ATTRIBUTE_CONVERTERS;
    public static final ResourceLocation REGISTRY_NAME_PERK_CUSTOM_MODIFIERS;
    public static final ResourceLocation REGISTRY_NAME_PERK_ATTRIBUTE_READERS;
    public static final ResourceLocation REGISTRY_NAME_CRYSTAL_PROPERTIES;
    public static final ResourceLocation REGISTRY_NAME_CRYSTAL_USAGES;
    public static final ResourceLocation REGISTRY_NAME_ALTAR_EFFECTS;
    public static IForgeRegistry<IConstellation> REGISTRY_CONSTELLATIONS;
    public static IForgeRegistry<ConstellationEffectProvider> REGISTRY_CONSTELLATION_EFFECT;
    public static IForgeRegistry<MantleEffect> REGISTRY_MANTLE_EFFECT;
    public static IForgeRegistry<EngravingEffect> REGISTRY_ENGRAVING_EFFECT;
    public static IForgeRegistry<StructureType> REGISTRY_STRUCTURE_TYPES;
    public static IForgeRegistry<PerkAttributeType> REGISTRY_PERK_ATTRIBUTE_TYPES;
    public static IForgeRegistry<PerkConverter> REGISTRY_PERK_ATTRIBUTE_CONVERTERS;
    public static IForgeRegistry<PerkAttributeModifier> REGISTRY_PERK_CUSTOM_MODIFIERS;
    public static IForgeRegistry<PerkAttributeReader> REGISTRY_PERK_ATTRIBUTE_READERS;
    public static IForgeRegistry<CrystalProperty> REGISTRY_CRYSTAL_PROPERTIES;
    public static IForgeRegistry<PropertyUsage> REGISTRY_CRYSTAL_USAGES;
    public static IForgeRegistry<AltarRecipeEffect> REGISTRY_ALTAR_EFFECTS;
    
    private RegistriesAS() {
    }
    
    static {
        REGISTRY_NAME_CONSTELLATIONS = AstralSorcery.key("constellations");
        REGISTRY_NAME_CONSTELLATION_EFFECTS = AstralSorcery.key("constellation_effect");
        REGISTRY_NAME_MANTLE_EFFECTS = AstralSorcery.key("mantle_effect");
        REGISTRY_NAME_ENGRAVING_EFFECT = AstralSorcery.key("engraving_effect");
        REGISTRY_NAME_STRUCTURE_TYPES = AstralSorcery.key("structure_types");
        REGISTRY_NAME_PERK_ATTRIBUTE_TYPES = AstralSorcery.key("perk_attribute_types");
        REGISTRY_NAME_PERK_ATTRIBUTE_CONVERTERS = AstralSorcery.key("perk_attribute_converters");
        REGISTRY_NAME_PERK_CUSTOM_MODIFIERS = AstralSorcery.key("perk_attribute_custom_modifiers");
        REGISTRY_NAME_PERK_ATTRIBUTE_READERS = AstralSorcery.key("perk_attribute_readers");
        REGISTRY_NAME_CRYSTAL_PROPERTIES = AstralSorcery.key("attribute_crystal_properties");
        REGISTRY_NAME_CRYSTAL_USAGES = AstralSorcery.key("attribute_crystal_usages");
        REGISTRY_NAME_ALTAR_EFFECTS = AstralSorcery.key("altar_recipe_effects");
    }
}
