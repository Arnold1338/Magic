package hellfirepvp.astralsorcery.common.crafting.recipe.altar;

import java.util.HashMap;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.NBTCopyRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationCopyStatsRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseNBTCopyRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.CrystalCountRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationItemRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseItemRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseMergeStatsRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationBaseAverageStatsRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.TraitUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.AttunementUpgradeRecipe;

public class AltarRecipeTypeHandler
{
    public static final Type<AttunementUpgradeRecipe> ALTAR_UPGRADE_ATTUNEMENT;
    public static final Type<ConstellationUpgradeRecipe> ALTAR_UPGRADE_CONSTELLATION;
    public static final Type<TraitUpgradeRecipe> ALTAR_UPGRADE_TRAIT;
    public static final Type<ConstellationBaseAverageStatsRecipe> CONSTELLATION_CRYSTAL_AVERAGE;
    public static final Type<ConstellationBaseMergeStatsRecipe> CONSTELLATION_CRYSTAL_MERGE;
    public static final Type<ConstellationBaseItemRecipe> CONSTELLATION_ITEM_BASE;
    public static final Type<ConstellationItemRecipe> CONSTELLATION_ITEM;
    public static final Type<CrystalCountRecipe> CRYSTAL_SET_COUNT;
    public static final Type<ConstellationBaseNBTCopyRecipe> CONSTELLATION_BASE_NBT_COPY;
    public static final Type<ConstellationCopyStatsRecipe> CONSTELLATION_COPY_CRYSTAL;
    public static final Type<NBTCopyRecipe> NBT_COPY;
    public static final Type<SimpleAltarRecipe> DEFAULT;
    private static final Map<ResourceLocation, Type<?>> CONVERTER_MAP;
    
    public static <T extends SimpleAltarRecipe> Type<T> registerConverter(final ResourceLocation name, final Function<SimpleAltarRecipe, T> converter) {
        final Type<T> type = new Type<T>(name, (Function)converter);
        AltarRecipeTypeHandler.CONVERTER_MAP.put(name, type);
        return type;
    }
    
    private static <T extends SimpleAltarRecipe> void registerInternal(final Type<T> type, final Function<SimpleAltarRecipe, T> converter) {
        ((Type<SimpleAltarRecipe>)type).converter = (Function<SimpleAltarRecipe, SimpleAltarRecipe>)converter;
        AltarRecipeTypeHandler.CONVERTER_MAP.put(((Type<SimpleAltarRecipe>)type).key, type);
    }
    
    public static <T extends SimpleAltarRecipe> T convert(final SimpleAltarRecipe recipe, final ResourceLocation alternativeBase) {
        return (T)AltarRecipeTypeHandler.CONVERTER_MAP.getOrDefault(alternativeBase, AltarRecipeTypeHandler.DEFAULT).convert(recipe);
    }
    
    public static void init() {
        registerInternal(AltarRecipeTypeHandler.ALTAR_UPGRADE_ATTUNEMENT, AttunementUpgradeRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.ALTAR_UPGRADE_CONSTELLATION, ConstellationUpgradeRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.ALTAR_UPGRADE_TRAIT, TraitUpgradeRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_CRYSTAL_AVERAGE, ConstellationBaseAverageStatsRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_CRYSTAL_MERGE, ConstellationBaseMergeStatsRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_ITEM_BASE, ConstellationBaseItemRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_ITEM, ConstellationItemRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CRYSTAL_SET_COUNT, CrystalCountRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_BASE_NBT_COPY, ConstellationBaseNBTCopyRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.CONSTELLATION_COPY_CRYSTAL, ConstellationCopyStatsRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.NBT_COPY, NBTCopyRecipe::convertToThis);
        registerInternal(AltarRecipeTypeHandler.DEFAULT, Function.identity());
    }
    
    static {
        ALTAR_UPGRADE_ATTUNEMENT = new Type<AttunementUpgradeRecipe>(AstralSorcery.key("attunement_upgrade"));
        ALTAR_UPGRADE_CONSTELLATION = new Type<ConstellationUpgradeRecipe>(AstralSorcery.key("constellation_upgrade"));
        ALTAR_UPGRADE_TRAIT = new Type<TraitUpgradeRecipe>(AstralSorcery.key("trait_upgrade"));
        CONSTELLATION_CRYSTAL_AVERAGE = new Type<ConstellationBaseAverageStatsRecipe>(AstralSorcery.key("constellation_base_average"));
        CONSTELLATION_CRYSTAL_MERGE = new Type<ConstellationBaseMergeStatsRecipe>(AstralSorcery.key("constellation_base_merge"));
        CONSTELLATION_ITEM_BASE = new Type<ConstellationBaseItemRecipe>(AstralSorcery.key("constellation_base"));
        CONSTELLATION_ITEM = new Type<ConstellationItemRecipe>(AstralSorcery.key("constellation_item"));
        CRYSTAL_SET_COUNT = new Type<CrystalCountRecipe>(AstralSorcery.key("crystal_count"));
        CONSTELLATION_BASE_NBT_COPY = new Type<ConstellationBaseNBTCopyRecipe>(AstralSorcery.key("constellation_base_nbt_copy"));
        CONSTELLATION_COPY_CRYSTAL = new Type<ConstellationCopyStatsRecipe>(AstralSorcery.key("constellation_copy_stats"));
        NBT_COPY = new Type<NBTCopyRecipe>(AstralSorcery.key("nbt_copy"));
        DEFAULT = new Type<SimpleAltarRecipe>(AstralSorcery.key("default"));
        CONVERTER_MAP = new HashMap<ResourceLocation, Type<?>>();
    }
    
    public static class Type<T extends SimpleAltarRecipe>
    {
        private final ResourceLocation key;
        private Function<SimpleAltarRecipe, T> converter;
        
        private Type(final ResourceLocation key) {
            this.key = key;
        }
        
        private Type(final ResourceLocation key, final Function<SimpleAltarRecipe, T> converter) {
            this.key = key;
            this.converter = converter;
        }
        
        public T convert(final SimpleAltarRecipe recipe) {
            return this.converter.apply(recipe);
        }
        
        public final ResourceLocation getKey() {
            return this.key;
        }
    }
}
