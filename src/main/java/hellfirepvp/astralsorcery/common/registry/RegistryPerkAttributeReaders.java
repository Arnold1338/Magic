package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.perk.reader.ReaderAddedPercentage;
import hellfirepvp.astralsorcery.common.perk.reader.ReaderPercentageAttribute;
import hellfirepvp.astralsorcery.common.perk.reader.PerkAttributeReader;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.reader.ReaderBreakSpeed;
import hellfirepvp.astralsorcery.common.perk.reader.ReaderFlatAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.common.ForgeMod;
import hellfirepvp.astralsorcery.common.perk.reader.ReaderVanillaAttribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class RegistryPerkAttributeReaders
{
    private RegistryPerkAttributeReaders() {
    }
    
    public static void init() {
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_MELEE_DAMAGE, ref("generic.attack_damage")).formatAsDecimal());
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_HEALTH, ref("generic.max_health"));
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_MOVESPEED, ref("generic.movement_speed")).formatAsDecimal());
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_SWIMSPEED, (RegistryObject<Attribute>)ForgeMod.SWIM_SPEED).formatAsDecimal());
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_ARMOR, ref("generic.armor"));
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_ARMOR_TOUGHNESS, ref("generic.armor_toughness"));
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_ATTACK_SPEED, ref("generic.attack_speed")).formatAsDecimal());
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_REACH, (RegistryObject<Attribute>)ForgeMod.REACH_DISTANCE).formatAsDecimal());
        register(new ReaderVanillaAttribute(PerkAttributeTypesAS.ATTR_TYPE_LUCK, ref("generic.luck")).formatAsDecimal());
        register(new ReaderFlatAttribute(PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM, 1000.0)).formatAsDecimal();
        register(new ReaderFlatAttribute(PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE, 0.0));
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_CRIT_MULTIPLIER);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_PROJ_DAMAGE);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_PROJ_SPEED);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_LIFE_RECOVERY);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_POTION_DURATION);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_ENCH_EFFECT);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_DODGE);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_CRIT_CHANCE);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_ATTACK_LIFE_LEECH);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_INC_THORNS);
        registerDefaultReader(PerkAttributeTypesAS.ATTR_TYPE_COOLDOWN_REDUCTION);
        register(new ReaderBreakSpeed(PerkAttributeTypesAS.ATTR_TYPE_INC_HARVEST_SPEED));
    }
    
    private static RegistryObject<Attribute> ref(final String key) {
        return (RegistryObject<Attribute>)RegistryObject.of(new ResourceLocation(key), ForgeRegistries.ATTRIBUTES);
    }
    
    private static PerkAttributeReader registerDefaultReader(final PerkAttributeType type) {
        if (type.isMultiplicative()) {
            return register(new ReaderPercentageAttribute(type));
        }
        return register(new ReaderAddedPercentage(type));
    }
    
    private static <T extends PerkAttributeReader> T register(final T reader) {
        AstralSorcery.getProxy().getRegistryPrimer().register(reader);
        return reader;
    }
}
