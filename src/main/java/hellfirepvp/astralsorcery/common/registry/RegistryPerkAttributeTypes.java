package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeLimiter;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeLuck;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeCooldown;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMiningSize;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeChargeMaximum;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeThorns;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeSwimSpeed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeProjectileAttackDamage;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypePotionDuration;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypePerkEffect;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMovementSpeed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMeleeAttackDamage;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMaxReach;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMaxHealth;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeLifeRecovery;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeLifeLeech;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeEnchantmentEffectiveness;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeDodge;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeCritMultiplier;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeCritChance;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeBreakSpeed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeAttackSpeed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeArrowSpeed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeArmorToughness;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeArmor;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeAllElementalResist;

public class RegistryPerkAttributeTypes
{
    private RegistryPerkAttributeTypes() {
    }
    
    public static void init() {
        PerkAttributeTypesAS.ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST = register(new AttributeTypeAllElementalResist());
        PerkAttributeTypesAS.ATTR_TYPE_ARMOR = register(new AttributeTypeArmor());
        PerkAttributeTypesAS.ATTR_TYPE_ARMOR_TOUGHNESS = register(new AttributeTypeArmorToughness());
        PerkAttributeTypesAS.ATTR_TYPE_PROJ_SPEED = register(new AttributeTypeArrowSpeed());
        PerkAttributeTypesAS.ATTR_TYPE_ATTACK_SPEED = register(new AttributeTypeAttackSpeed());
        PerkAttributeTypesAS.ATTR_TYPE_INC_HARVEST_SPEED = register(new AttributeTypeBreakSpeed());
        PerkAttributeTypesAS.ATTR_TYPE_INC_CRIT_CHANCE = register(new AttributeTypeCritChance());
        PerkAttributeTypesAS.ATTR_TYPE_INC_CRIT_MULTIPLIER = register(new AttributeTypeCritMultiplier());
        PerkAttributeTypesAS.ATTR_TYPE_INC_DODGE = register(new AttributeTypeDodge());
        PerkAttributeTypesAS.ATTR_TYPE_INC_ENCH_EFFECT = register(new AttributeTypeEnchantmentEffectiveness());
        PerkAttributeTypesAS.ATTR_TYPE_ATTACK_LIFE_LEECH = register(new AttributeTypeLifeLeech());
        PerkAttributeTypesAS.ATTR_TYPE_LIFE_RECOVERY = register(new AttributeTypeLifeRecovery());
        PerkAttributeTypesAS.ATTR_TYPE_HEALTH = register(new AttributeTypeMaxHealth());
        PerkAttributeTypesAS.ATTR_TYPE_REACH = register(new AttributeTypeMaxReach());
        PerkAttributeTypesAS.ATTR_TYPE_MELEE_DAMAGE = register(new AttributeTypeMeleeAttackDamage());
        PerkAttributeTypesAS.ATTR_TYPE_MOVESPEED = register(new AttributeTypeMovementSpeed());
        PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT = register(new AttributeTypePerkEffect());
        PerkAttributeTypesAS.ATTR_TYPE_POTION_DURATION = register(new AttributeTypePotionDuration());
        PerkAttributeTypesAS.ATTR_TYPE_PROJ_DAMAGE = register(new AttributeTypeProjectileAttackDamage());
        PerkAttributeTypesAS.ATTR_TYPE_SWIMSPEED = register(new AttributeTypeSwimSpeed());
        PerkAttributeTypesAS.ATTR_TYPE_INC_THORNS = register(new AttributeTypeThorns());
        PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM = register(new AttributeTypeChargeMaximum());
        PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE = register(new AttributeTypeMiningSize());
        PerkAttributeTypesAS.ATTR_TYPE_COOLDOWN_REDUCTION = register(new AttributeTypeCooldown());
        PerkAttributeTypesAS.ATTR_TYPE_LUCK = register(new AttributeTypeLuck());
        PerkAttributeTypesAS.ATTR_TYPE_BLEED_DURATION = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_BLEED_DURATION, false));
        PerkAttributeTypesAS.ATTR_TYPE_BLEED_CHANCE = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_BLEED_CHANCE, false));
        PerkAttributeTypesAS.ATTR_TYPE_BLEED_STACKS = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_BLEED_STACKS, false));
        PerkAttributeTypesAS.ATTR_TYPE_RAMPAGE_DURATION = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_RAMPAGE_DURATION, false));
        PerkAttributeTypesAS.ATTR_TYPE_INC_THORNS_RANGED = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_THORNS_RANGED, false));
        PerkAttributeTypesAS.ATTR_TYPE_ARC_CHAINS = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_ARC_CHAINS, false));
        PerkAttributeTypesAS.ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION, true));
        PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP = register(PerkAttributeType.makeDefault(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_PERK_EXP, true));
        PerkAttributeLimiter.limit(PerkAttributeTypesAS.ATTR_TYPE_INC_DODGE, () -> 0.0, () -> 0.75);
        PerkAttributeLimiter.limit(PerkAttributeTypesAS.ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST, () -> 0.0, () -> 0.6);
        PerkAttributeLimiter.limit(PerkAttributeTypesAS.ATTR_TYPE_ATTACK_LIFE_LEECH, () -> 0.0, () -> 0.2);
        PerkAttributeLimiter.limit(PerkAttributeTypesAS.ATTR_TYPE_COOLDOWN_REDUCTION, () -> 0.0, () -> 0.8);
    }
    
    private static <T extends PerkAttributeType> T register(final T type) {
        AstralSorcery.getProxy().getRegistryPrimer().register(type);
        return type;
    }
}
