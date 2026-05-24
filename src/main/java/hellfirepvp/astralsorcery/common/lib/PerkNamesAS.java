package hellfirepvp.astralsorcery.common.lib;

public class PerkNamesAS
{
    public static final String NAME_INC_ALL_RES;
    public static final String NAME_INC_DODGE;
    public static final String NAME_INC_REACH;
    public static final String NAME_INC_ARMOR;
    public static final String NAME_INC_ARMOR_TOUGHNESS;
    public static final String NAME_INC_LIFE;
    public static final String NAME_INC_LIFE_RECOVERY;
    public static final String NAME_INC_LIFE_LEECH;
    public static final String NAME_INC_PROJ_SPEED;
    public static final String NAME_INC_PROJ_DAMAGE;
    public static final String NAME_INC_ATTACK_SPEED;
    public static final String NAME_INC_MELEE_DAMAGE;
    public static final String NAME_INC_MINING_SPEED;
    public static final String NAME_INC_MINING_SIZE;
    public static final String NAME_INC_CRIT_CHANCE;
    public static final String NAME_INC_CRIT_MULTIPLIER;
    public static final String NAME_INC_MOVESPEED;
    public static final String NAME_INC_SWIMSPEED;
    public static final String NAME_INC_PERK_EFFECT;
    public static final String NAME_INC_PERK_EXP;
    public static final String NAME_INC_POTION_DURATION;
    public static final String NAME_INC_CHARGE_MAX;
    public static final String NAME_INC_CHARGE_REGEN;
    public static final String NAME_INC_THORNS;
    public static final String NAME_INC_BLEED_DURATION;
    public static final String NAME_INC_BLEED_CHANCE;
    public static final String NAME_INC_BLEED_STACKS;
    public static final String NAME_INC_RAMPAGE_DURATION;
    public static final String NAME_INC_ENCH_EFFECT;
    public static final String NAME_INC_COOLDOWN_RECOVERY;
    public static final String NAME_INC_LUCK;
    public static final String NAME_ADD_ALL_RES;
    public static final String NAME_ADD_DODGE;
    public static final String NAME_ADD_REACH;
    public static final String NAME_ADD_ARMOR;
    public static final String NAME_ADD_ARMOR_TOUGHNESS;
    public static final String NAME_ADD_LIFE;
    public static final String NAME_ADD_LIFE_RECOVERY;
    public static final String NAME_ADD_LIFE_LEECH;
    public static final String NAME_ADD_PROJ_SPEED;
    public static final String NAME_ADD_PROJ_DAMAGE;
    public static final String NAME_ADD_ATTACK_SPEED;
    public static final String NAME_ADD_MELEE_DAMAGE;
    public static final String NAME_ADD_MINING_SPEED;
    public static final String NAME_ADD_MINING_SIZE;
    public static final String NAME_ADD_CRIT_CHANCE;
    public static final String NAME_ADD_CRIT_MULTIPLIER;
    public static final String NAME_ADD_MOVESPEED;
    public static final String NAME_ADD_SWIMSPEED;
    public static final String NAME_ADD_PERK_EFFECT;
    public static final String NAME_ADD_PERK_EXP;
    public static final String NAME_ADD_POTION_DURATION;
    public static final String NAME_ADD_CHARGE_MAX;
    public static final String NAME_ADD_CHARGE_REGEN;
    public static final String NAME_ADD_THORNS;
    public static final String NAME_ADD_BLEED_DURATION;
    public static final String NAME_ADD_BLEED_CHANCE;
    public static final String NAME_ADD_BLEED_STACKS;
    public static final String NAME_ADD_RAMPAGE_DURATION;
    public static final String NAME_ADD_LUCK;
    
    private PerkNamesAS() {
    }
    
    public static String name(final String namespacedName) {
        return String.format("perk.astralsorcery.%s", namespacedName);
    }
    
    static {
        NAME_INC_ALL_RES = name("generic.inc.allres");
        NAME_INC_DODGE = name("generic.inc.dodge");
        NAME_INC_REACH = name("generic.inc.reach");
        NAME_INC_ARMOR = name("generic.inc.armor");
        NAME_INC_ARMOR_TOUGHNESS = name("generic.inc.armor_toughness");
        NAME_INC_LIFE = name("generic.inc.life");
        NAME_INC_LIFE_RECOVERY = name("generic.inc.life_recovery");
        NAME_INC_LIFE_LEECH = name("generic.inc.life_leech");
        NAME_INC_PROJ_SPEED = name("generic.inc.proj_speed");
        NAME_INC_PROJ_DAMAGE = name("generic.inc.damage_proj");
        NAME_INC_ATTACK_SPEED = name("generic.inc.attack_speed");
        NAME_INC_MELEE_DAMAGE = name("generic.inc.damage_melee");
        NAME_INC_MINING_SPEED = name("generic.inc.mining_speed");
        NAME_INC_MINING_SIZE = name("generic.inc.mining_size");
        NAME_INC_CRIT_CHANCE = name("generic.inc.crit_chance");
        NAME_INC_CRIT_MULTIPLIER = name("generic.inc.crit_multiplier");
        NAME_INC_MOVESPEED = name("generic.inc.move_speed");
        NAME_INC_SWIMSPEED = name("generic.inc.swim_speed");
        NAME_INC_PERK_EFFECT = name("generic.inc.perk_effect");
        NAME_INC_PERK_EXP = name("generic.inc.perk_exp");
        NAME_INC_POTION_DURATION = name("generic.inc.potion_duration");
        NAME_INC_CHARGE_MAX = name("generic.inc.starlight_charge_max");
        NAME_INC_CHARGE_REGEN = name("generic.inc.starlight_charge_regen");
        NAME_INC_THORNS = name("generic.inc.thorns");
        NAME_INC_BLEED_DURATION = name("generic.inc.bleed_duration");
        NAME_INC_BLEED_CHANCE = name("generic.inc.bleed_chance");
        NAME_INC_BLEED_STACKS = name("generic.inc.bleed_stacks");
        NAME_INC_RAMPAGE_DURATION = name("generic.inc.rampage_duration");
        NAME_INC_ENCH_EFFECT = name("generic.inc.ench_effect");
        NAME_INC_COOLDOWN_RECOVERY = name("generic.inc.cooldown_reduction");
        NAME_INC_LUCK = name("generic.inc.luck");
        NAME_ADD_ALL_RES = name("generic.add.allres");
        NAME_ADD_DODGE = name("generic.add.dodge");
        NAME_ADD_REACH = name("generic.add.reach");
        NAME_ADD_ARMOR = name("generic.add.armor");
        NAME_ADD_ARMOR_TOUGHNESS = name("generic.add.armor_toughness");
        NAME_ADD_LIFE = name("generic.add.life");
        NAME_ADD_LIFE_RECOVERY = name("generic.add.life_recovery");
        NAME_ADD_LIFE_LEECH = name("generic.add.life_leech");
        NAME_ADD_PROJ_SPEED = name("generic.add.proj_speed");
        NAME_ADD_PROJ_DAMAGE = name("generic.add.damage_proj");
        NAME_ADD_ATTACK_SPEED = name("generic.add.attack_speed");
        NAME_ADD_MELEE_DAMAGE = name("generic.add.damage_melee");
        NAME_ADD_MINING_SPEED = name("generic.add.mining_speed");
        NAME_ADD_MINING_SIZE = name("generic.add.mining_size");
        NAME_ADD_CRIT_CHANCE = name("generic.add.crit_chance");
        NAME_ADD_CRIT_MULTIPLIER = name("generic.add.crit_multiplier");
        NAME_ADD_MOVESPEED = name("generic.add.move_speed");
        NAME_ADD_SWIMSPEED = name("generic.add.swim_speed");
        NAME_ADD_PERK_EFFECT = name("generic.add.perk_effect");
        NAME_ADD_PERK_EXP = name("generic.add.perk_exp");
        NAME_ADD_POTION_DURATION = name("generic.add.potion_duration");
        NAME_ADD_CHARGE_MAX = name("generic.add.starlight_charge_max");
        NAME_ADD_CHARGE_REGEN = name("generic.add.starlight_charge_regen");
        NAME_ADD_THORNS = name("generic.add.thorns");
        NAME_ADD_BLEED_DURATION = name("generic.add.bleed_duration");
        NAME_ADD_BLEED_CHANCE = name("generic.add.bleed_chance");
        NAME_ADD_BLEED_STACKS = name("generic.add.bleed_stacks");
        NAME_ADD_RAMPAGE_DURATION = name("generic.add.rampage_duration");
        NAME_ADD_LUCK = name("generic.add.luck");
    }
}
