package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import net.minecraft.resources.ResourceLocation;

public class PerkAttributeTypesAS
{
    public static final ResourceLocation KEY_ATTR_TYPE_MELEE_DAMAGE;
    public static final ResourceLocation KEY_ATTR_TYPE_PROJ_DAMAGE;
    public static final ResourceLocation KEY_ATTR_TYPE_PROJ_SPEED;
    public static final ResourceLocation KEY_ATTR_TYPE_HEALTH;
    public static final ResourceLocation KEY_ATTR_TYPE_MOVESPEED;
    public static final ResourceLocation KEY_ATTR_TYPE_SWIMSPEED;
    public static final ResourceLocation KEY_ATTR_TYPE_ARMOR;
    public static final ResourceLocation KEY_ATTR_TYPE_ARMOR_TOUGHNESS;
    public static final ResourceLocation KEY_ATTR_TYPE_ATTACK_SPEED;
    public static final ResourceLocation KEY_ATTR_TYPE_REACH;
    public static final ResourceLocation KEY_ATTR_TYPE_LUCK;
    public static final ResourceLocation KEY_ATTR_TYPE_LIFE_RECOVERY;
    public static final ResourceLocation KEY_ATTR_TYPE_POTION_DURATION;
    public static final ResourceLocation KEY_ATTR_TYPE_BLEED_DURATION;
    public static final ResourceLocation KEY_ATTR_TYPE_BLEED_STACKS;
    public static final ResourceLocation KEY_ATTR_TYPE_BLEED_CHANCE;
    public static final ResourceLocation KEY_ATTR_TYPE_RAMPAGE_DURATION;
    public static final ResourceLocation KEY_ATTR_TYPE_ATTACK_LIFE_LEECH;
    public static final ResourceLocation KEY_ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION;
    public static final ResourceLocation KEY_ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM;
    public static final ResourceLocation KEY_ATTR_TYPE_MINING_SIZE;
    public static final ResourceLocation KEY_ATTR_TYPE_ARC_CHAINS;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_PERK_EFFECT;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_HARVEST_SPEED;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_CRIT_CHANCE;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_CRIT_MULTIPLIER;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_PERK_EXP;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_DODGE;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_THORNS;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_THORNS_RANGED;
    public static final ResourceLocation KEY_ATTR_TYPE_INC_ENCH_EFFECT;
    public static final ResourceLocation KEY_ATTR_TYPE_COOLDOWN_REDUCTION;
    public static PerkAttributeType ATTR_TYPE_MELEE_DAMAGE;
    public static PerkAttributeType ATTR_TYPE_PROJ_DAMAGE;
    public static PerkAttributeType ATTR_TYPE_PROJ_SPEED;
    public static PerkAttributeType ATTR_TYPE_HEALTH;
    public static PerkAttributeType ATTR_TYPE_MOVESPEED;
    public static PerkAttributeType ATTR_TYPE_SWIMSPEED;
    public static PerkAttributeType ATTR_TYPE_ARMOR;
    public static PerkAttributeType ATTR_TYPE_ARMOR_TOUGHNESS;
    public static PerkAttributeType ATTR_TYPE_ATTACK_SPEED;
    public static PerkAttributeType ATTR_TYPE_REACH;
    public static PerkAttributeType ATTR_TYPE_LUCK;
    public static PerkAttributeType ATTR_TYPE_LIFE_RECOVERY;
    public static PerkAttributeType ATTR_TYPE_POTION_DURATION;
    public static PerkAttributeType ATTR_TYPE_BLEED_DURATION;
    public static PerkAttributeType ATTR_TYPE_BLEED_STACKS;
    public static PerkAttributeType ATTR_TYPE_BLEED_CHANCE;
    public static PerkAttributeType ATTR_TYPE_RAMPAGE_DURATION;
    public static PerkAttributeType ATTR_TYPE_ATTACK_LIFE_LEECH;
    public static PerkAttributeType ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION;
    public static PerkAttributeType ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM;
    public static PerkAttributeType ATTR_TYPE_MINING_SIZE;
    public static PerkAttributeType ATTR_TYPE_ARC_CHAINS;
    public static PerkAttributeType ATTR_TYPE_INC_PERK_EFFECT;
    public static PerkAttributeType ATTR_TYPE_INC_HARVEST_SPEED;
    public static PerkAttributeType ATTR_TYPE_INC_CRIT_CHANCE;
    public static PerkAttributeType ATTR_TYPE_INC_CRIT_MULTIPLIER;
    public static PerkAttributeType ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST;
    public static PerkAttributeType ATTR_TYPE_INC_PERK_EXP;
    public static PerkAttributeType ATTR_TYPE_INC_DODGE;
    public static PerkAttributeType ATTR_TYPE_INC_THORNS;
    public static PerkAttributeType ATTR_TYPE_INC_THORNS_RANGED;
    public static PerkAttributeType ATTR_TYPE_INC_ENCH_EFFECT;
    public static PerkAttributeType ATTR_TYPE_COOLDOWN_REDUCTION;
    
    private PerkAttributeTypesAS() {
    }
    
    static {
        KEY_ATTR_TYPE_MELEE_DAMAGE = AstralSorcery.key("meleeattackdamage");
        KEY_ATTR_TYPE_PROJ_DAMAGE = AstralSorcery.key("projectileattackdamage");
        KEY_ATTR_TYPE_PROJ_SPEED = AstralSorcery.key("projectilespeed");
        KEY_ATTR_TYPE_HEALTH = AstralSorcery.key("maxhealth");
        KEY_ATTR_TYPE_MOVESPEED = AstralSorcery.key("movespeed");
        KEY_ATTR_TYPE_SWIMSPEED = AstralSorcery.key("swimspeed");
        KEY_ATTR_TYPE_ARMOR = AstralSorcery.key("armor");
        KEY_ATTR_TYPE_ARMOR_TOUGHNESS = AstralSorcery.key("armortoughness");
        KEY_ATTR_TYPE_ATTACK_SPEED = AstralSorcery.key("attackspeed");
        KEY_ATTR_TYPE_REACH = AstralSorcery.key("reach");
        KEY_ATTR_TYPE_LUCK = AstralSorcery.key("luck");
        KEY_ATTR_TYPE_LIFE_RECOVERY = AstralSorcery.key("liferecovery");
        KEY_ATTR_TYPE_POTION_DURATION = AstralSorcery.key("potionduration");
        KEY_ATTR_TYPE_BLEED_DURATION = AstralSorcery.key("bleedduration");
        KEY_ATTR_TYPE_BLEED_STACKS = AstralSorcery.key("bleedamount");
        KEY_ATTR_TYPE_BLEED_CHANCE = AstralSorcery.key("bleedchance");
        KEY_ATTR_TYPE_RAMPAGE_DURATION = AstralSorcery.key("rampageduration");
        KEY_ATTR_TYPE_ATTACK_LIFE_LEECH = AstralSorcery.key("lifeleech");
        KEY_ATTR_TYPE_ALIGNMENT_CHARGE_REGENERATION = AstralSorcery.key("chargeregeneration");
        KEY_ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM = AstralSorcery.key("chargecap");
        KEY_ATTR_TYPE_MINING_SIZE = AstralSorcery.key("miningsize");
        KEY_ATTR_TYPE_ARC_CHAINS = AstralSorcery.key("archops");
        KEY_ATTR_TYPE_INC_PERK_EFFECT = AstralSorcery.key("perkeffect");
        KEY_ATTR_TYPE_INC_HARVEST_SPEED = AstralSorcery.key("harvestspeed");
        KEY_ATTR_TYPE_INC_CRIT_CHANCE = AstralSorcery.key("critchance");
        KEY_ATTR_TYPE_INC_CRIT_MULTIPLIER = AstralSorcery.key("critmulti");
        KEY_ATTR_TYPE_INC_ALL_ELEMENTAL_RESIST = AstralSorcery.key("allres");
        KEY_ATTR_TYPE_INC_PERK_EXP = AstralSorcery.key("expgain");
        KEY_ATTR_TYPE_INC_DODGE = AstralSorcery.key("dodge");
        KEY_ATTR_TYPE_INC_THORNS = AstralSorcery.key("thorns");
        KEY_ATTR_TYPE_INC_THORNS_RANGED = AstralSorcery.key("rangedthorns");
        KEY_ATTR_TYPE_INC_ENCH_EFFECT = AstralSorcery.key("dynenchantmenteffect");
        KEY_ATTR_TYPE_COOLDOWN_REDUCTION = AstralSorcery.key("cooldown_reduction");
    }
}
