package hellfirepvp.astralsorcery.common.perk.data;

import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.util.TriFunction;
import hellfirepvp.astralsorcery.common.perk.ProgressGatedPerk;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeConverterPerk;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierPerk;
import hellfirepvp.astralsorcery.common.perk.node.socket.GemSocketMajorPerk;
import hellfirepvp.astralsorcery.common.perk.node.MajorPerk;
import hellfirepvp.astralsorcery.common.perk.node.ConstellationPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyTreeConnector;
import hellfirepvp.astralsorcery.common.perk.node.root.RootVicio;
import hellfirepvp.astralsorcery.common.perk.node.root.RootEvorsio;
import hellfirepvp.astralsorcery.common.perk.node.root.RootDiscidia;
import hellfirepvp.astralsorcery.common.perk.node.root.RootArmara;
import hellfirepvp.astralsorcery.common.perk.node.root.RootAevitas;
import hellfirepvp.astralsorcery.common.perk.node.focus.KeyVorux;
import hellfirepvp.astralsorcery.common.perk.node.focus.KeyUlteria;
import hellfirepvp.astralsorcery.common.perk.node.focus.KeyGelu;
import hellfirepvp.astralsorcery.common.perk.node.focus.KeyAlcara;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyVoidTrash;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyStoneEnrichment;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyStepAssist;
import hellfirepvp.astralsorcery.common.perk.node.key.KeySpawnLights;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyReducedFood;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyRampage;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyProjectileProximity;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyProjectileDistance;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyNoKnockback;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyNoArmor;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMending;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMantleFlight;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMagnetDrops;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyLightningArc;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyLastBreath;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyGrowables;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyEntityReach;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDisarm;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDigTypes;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDamageEffects;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDamageArmor;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyCullingAttack;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyCleanseBadPotions;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyCheatDeath;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyChargeBalancing;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyBleed;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyAreaOfEffect;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyAddEnchantment;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class PerkTypeHandler
{
    private static final Map<ResourceLocation, Type<? extends AbstractPerk>> CONVERTER_MAP;
    public static final Type<AbstractPerk> DEFAULT;
    public static final Type<KeyAddEnchantment> KEY_ADD_ENCHANTMENT;
    public static final Type<KeyAreaOfEffect> KEY_AOE;
    public static final Type<KeyBleed> KEY_BLEED;
    public static final Type<KeyChargeBalancing> KEY_CHARGE_BALANCING;
    public static final Type<KeyCheatDeath> KEY_CHEATDEATH;
    public static final Type<KeyCleanseBadPotions> KEY_REMOVE_BAD_POTIONS;
    public static final Type<KeyCullingAttack> KEY_CULLING;
    public static final Type<KeyDamageArmor> KEY_DAMAGE_ARMOR;
    public static final Type<KeyDamageEffects> KEY_ON_HIT_POTIONS;
    public static final Type<KeyDigTypes> KEY_DIG_TYPES;
    public static final Type<KeyDisarm> KEY_DISARM;
    public static final Type<KeyEntityReach> KEY_ENTITY_REACH;
    public static final Type<KeyGrowables> KEY_GROWABLES;
    public static final Type<KeyLastBreath> KEY_LOW_LIFE;
    public static final Type<KeyLightningArc> KEY_LIGHTNING_ARC;
    public static final Type<KeyMagnetDrops> KEY_MAGNET_DROPS;
    public static final Type<KeyMantleFlight> KEY_MANTLE_CREATIVE_FLIGHT;
    public static final Type<KeyMending> KEY_ARMOR_MENDING;
    public static final Type<KeyNoArmor> KEY_NO_ARMOR;
    public static final Type<KeyNoKnockback> KEY_NO_KNOCKBACK;
    public static final Type<KeyProjectileDistance> KEY_RPOJ_DISTANCE;
    public static final Type<KeyProjectileProximity> KEY_PROJ_PROXIMITY;
    public static final Type<KeyRampage> KEY_RAMPAGE;
    public static final Type<KeyReducedFood> KEY_REDUCED_FOOD;
    public static final Type<KeySpawnLights> KEY_SPAWN_LIGHTS;
    public static final Type<KeyStepAssist> KEY_STEP_ASSIST;
    public static final Type<KeyStoneEnrichment> KEY_STONE_ENRICHMENT;
    public static final Type<KeyVoidTrash> KEY_VOID_TRASH;
    public static final Type<KeyAlcara> FOCUS_ALCARA;
    public static final Type<KeyGelu> FOCUS_GELU;
    public static final Type<KeyUlteria> FOCUS_ULTERIA;
    public static final Type<KeyVorux> FOCUS_VORUX;
    public static final Type<RootAevitas> ROOT_AEVITAS;
    public static final Type<RootArmara> ROOT_ARMARA;
    public static final Type<RootDiscidia> ROOT_DISCIDIA;
    public static final Type<RootEvorsio> ROOT_EVORSIO;
    public static final Type<RootVicio> ROOT_VICIO;
    public static final Type<KeyTreeConnector> EPIPHANY_PERK;
    public static final Type<KeyPerk> KEY_PERK;
    public static final Type<ConstellationPerk> CONSTELLATION_PERK;
    public static final Type<MajorPerk> MAJOR_PERK;
    public static final Type<GemSocketMajorPerk> GEM_SLOT_PERK;
    public static final Type<AttributeModifierPerk> MODIFIER_PERK;
    public static final Type<AttributeConverterPerk> CONVERTER_PERK;
    public static final Type<ProgressGatedPerk> GATED_PERK;
    
    public static <T extends AbstractPerk> Type<T> registerConverter(final ResourceLocation name, final TriFunction<ResourceLocation, Float, Float, T> converter) {
        final Type<T> type = new Type<T>(name, (TriFunction)converter);
        PerkTypeHandler.CONVERTER_MAP.put(name, type);
        return type;
    }
    
    public static <T extends AbstractPerk> T convert(final ResourceLocation perkKey, final float x, final float y, final ResourceLocation alternativeBase) {
        return (T)PerkTypeHandler.CONVERTER_MAP.getOrDefault(alternativeBase, PerkTypeHandler.DEFAULT).convert(perkKey, x, y);
    }
    
    public static boolean hasCustomType(final ResourceLocation key) {
        return PerkTypeHandler.CONVERTER_MAP.containsKey(key);
    }
    
    public static void init() {
        registerConverter(PerkTypeHandler.DEFAULT.getKey(), AbstractPerk::new);
    }
    
    static {
        CONVERTER_MAP = new HashMap<ResourceLocation, Type<? extends AbstractPerk>>();
        DEFAULT = new Type<AbstractPerk>(AstralSorcery.key("default"), (TriFunction)AbstractPerk::new);
        KEY_ADD_ENCHANTMENT = registerConverter(AstralSorcery.key("key_add_enchantment"), KeyAddEnchantment::new);
        KEY_AOE = registerConverter(AstralSorcery.key("key_area_of_effect"), KeyAreaOfEffect::new);
        KEY_BLEED = registerConverter(AstralSorcery.key("key_bleed"), KeyBleed::new);
        KEY_CHARGE_BALANCING = registerConverter(AstralSorcery.key("key_charge_balancing"), KeyChargeBalancing::new);
        KEY_CHEATDEATH = registerConverter(AstralSorcery.key("key_cheatdeath"), KeyCheatDeath::new);
        KEY_REMOVE_BAD_POTIONS = registerConverter(AstralSorcery.key("key_remove_bad_potions"), KeyCleanseBadPotions::new);
        KEY_CULLING = registerConverter(AstralSorcery.key("key_culling"), KeyCullingAttack::new);
        KEY_DAMAGE_ARMOR = registerConverter(AstralSorcery.key("key_damage_armor"), KeyDamageArmor::new);
        KEY_ON_HIT_POTIONS = registerConverter(AstralSorcery.key("key_on_hit_potions"), KeyDamageEffects::new);
        KEY_DIG_TYPES = registerConverter(AstralSorcery.key("key_dig_types"), KeyDigTypes::new);
        KEY_DISARM = registerConverter(AstralSorcery.key("key_disarm"), KeyDisarm::new);
        KEY_ENTITY_REACH = registerConverter(AstralSorcery.key("key_entity_reach"), KeyEntityReach::new);
        KEY_GROWABLES = registerConverter(AstralSorcery.key("key_growables"), KeyGrowables::new);
        KEY_LOW_LIFE = registerConverter(AstralSorcery.key("key_low_life"), KeyLastBreath::new);
        KEY_LIGHTNING_ARC = registerConverter(AstralSorcery.key("key_lightning_arc"), KeyLightningArc::new);
        KEY_MAGNET_DROPS = registerConverter(AstralSorcery.key("key_magnet_drops"), KeyMagnetDrops::new);
        KEY_MANTLE_CREATIVE_FLIGHT = registerConverter(AstralSorcery.key("key_vicio_mantle_creative_flight"), KeyMantleFlight::new);
        KEY_ARMOR_MENDING = registerConverter(AstralSorcery.key("key_armor_mending"), KeyMending::new);
        KEY_NO_ARMOR = registerConverter(AstralSorcery.key("key_no_armor"), KeyNoArmor::new);
        KEY_NO_KNOCKBACK = registerConverter(AstralSorcery.key("key_no_knockback"), KeyNoKnockback::new);
        KEY_RPOJ_DISTANCE = registerConverter(AstralSorcery.key("key_proj_distance"), KeyProjectileDistance::new);
        KEY_PROJ_PROXIMITY = registerConverter(AstralSorcery.key("key_proj_proximity"), KeyProjectileProximity::new);
        KEY_RAMPAGE = registerConverter(AstralSorcery.key("key_rampage"), KeyRampage::new);
        KEY_REDUCED_FOOD = registerConverter(AstralSorcery.key("key_reduced_food_need"), KeyReducedFood::new);
        KEY_SPAWN_LIGHTS = registerConverter(AstralSorcery.key("key_spawn_lights"), KeySpawnLights::new);
        KEY_STEP_ASSIST = registerConverter(AstralSorcery.key("key_step_assist"), KeyStepAssist::new);
        KEY_STONE_ENRICHMENT = registerConverter(AstralSorcery.key("key_stone_enrichment"), KeyStoneEnrichment::new);
        KEY_VOID_TRASH = registerConverter(AstralSorcery.key("key_void_trash"), KeyVoidTrash::new);
        FOCUS_ALCARA = registerConverter(AstralSorcery.key("focus_alcara"), KeyAlcara::new);
        FOCUS_GELU = registerConverter(AstralSorcery.key("focus_gelu"), KeyGelu::new);
        FOCUS_ULTERIA = registerConverter(AstralSorcery.key("focus_ulteria"), KeyUlteria::new);
        FOCUS_VORUX = registerConverter(AstralSorcery.key("focus_vorux"), KeyVorux::new);
        ROOT_AEVITAS = registerConverter(AstralSorcery.key("root_aevitas"), RootAevitas::new);
        ROOT_ARMARA = registerConverter(AstralSorcery.key("root_armara"), RootArmara::new);
        ROOT_DISCIDIA = registerConverter(AstralSorcery.key("root_discidia"), RootDiscidia::new);
        ROOT_EVORSIO = registerConverter(AstralSorcery.key("root_evorsio"), RootEvorsio::new);
        ROOT_VICIO = registerConverter(AstralSorcery.key("root_vicio"), RootVicio::new);
        EPIPHANY_PERK = registerConverter(AstralSorcery.key("epiphany_perk"), KeyTreeConnector::new);
        KEY_PERK = registerConverter(AstralSorcery.key("key_perk"), KeyPerk::new);
        CONSTELLATION_PERK = registerConverter(AstralSorcery.key("constellation_perk"), ConstellationPerk::convertToThis);
        MAJOR_PERK = registerConverter(AstralSorcery.key("major_perk"), MajorPerk::new);
        GEM_SLOT_PERK = registerConverter(AstralSorcery.key("gem_slot_perk"), GemSocketMajorPerk::new);
        MODIFIER_PERK = registerConverter(AstralSorcery.key("modifier_perk"), AttributeModifierPerk::new);
        CONVERTER_PERK = registerConverter(AstralSorcery.key("converter_perk"), AttributeConverterPerk::new);
        GATED_PERK = registerConverter(AstralSorcery.key("gated_perk"), ProgressGatedPerk::new);
    }
    
    public static class Type<T extends AbstractPerk>
    {
        private final ResourceLocation key;
        private final TriFunction<ResourceLocation, Float, Float, T> converter;
        
        private Type(final ResourceLocation key, final TriFunction<ResourceLocation, Float, Float, T> converter) {
            this.key = key;
            this.converter = converter;
        }
        
        public T convert(final ResourceLocation perkKey, final float x, final float y) {
            return this.converter.apply(perkKey, x, y);
        }
        
        public final ResourceLocation getKey() {
            return this.key;
        }
    }
}
