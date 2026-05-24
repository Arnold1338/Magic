package hellfirepvp.astralsorcery.common.item.gem;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import net.minecraft.util.Mth;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.PerkAttributeEntry;
import hellfirepvp.astralsorcery.common.data.config.registry.WeightedPerkAttributeRegistry;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.perk.DynamicModifierHelper;
import net.minecraft.world.level.item.ItemStack;
import java.util.Random;

public class GemAttributeHelper
{
    private static final Random rand;
    private static float chance3Modifiers;
    private static float chance4Modifiers;
    private static boolean allowDuplicateTypes;
    private static float incModifierLower;
    private static float incModifierHigher;
    private static boolean allowNegativeModifiers;
    private static float chanceNegative;
    private static float decModifierLower;
    private static float decModifierHigher;
    private static boolean allowMoreLessModifiers;
    private static float chanceMultiplicative;
    private static float moreModifierLower;
    private static float moreModifierHigher;
    private static float lessModifierLower;
    private static float lessModifierHigher;
    
    public static boolean rollGem(final ItemStack gem) {
        return rollGem(gem, GemAttributeHelper.rand);
    }
    
    public static boolean rollGem(final ItemStack gem, final Random random) {
        if (!DynamicModifierHelper.getStaticModifiers(gem).isEmpty()) {
            return false;
        }
        final GemType gemType = ItemPerkGem.getGemType(gem);
        if (gemType == null) {
            return false;
        }
        final int rolls = getPotentialMods(random, gemType.countModifier);
        final List<DynamicAttributeModifier> mods = new ArrayList<DynamicAttributeModifier>();
        final List<PerkAttributeEntry> configuredModifiers = WeightedPerkAttributeRegistry.INSTANCE.getConfiguredValues();
        for (int i = 0; i < rolls; ++i) {
            PerkAttributeEntry entry = null;
            if (GemAttributeHelper.allowDuplicateTypes) {
                entry = MiscUtils.getWeightedRandomEntry(configuredModifiers, random, PerkAttributeEntry::getWeight);
            }
            else {
                final List<PerkAttributeEntry> keys = new ArrayList<PerkAttributeEntry>(configuredModifiers);
                while (!keys.isEmpty() && entry == null) {
                    final PerkAttributeEntry item = getWeightedResultAndRemove(keys, random);
                    if (item != null) {
                        boolean foundType = false;
                        for (final DynamicAttributeModifier m : mods) {
                            if (m.getAttributeType().equals(item.getType())) {
                                foundType = true;
                                break;
                            }
                        }
                        if (foundType) {
                            continue;
                        }
                        entry = item;
                    }
                }
            }
            if (entry != null) {
                final boolean isNegative = GemAttributeHelper.allowNegativeModifiers && random.nextFloat() < GemAttributeHelper.chanceNegative;
                final boolean isMultiplicative = GemAttributeHelper.allowMoreLessModifiers && random.nextFloat() < GemAttributeHelper.chanceMultiplicative;
                final float lower = isNegative ? (isMultiplicative ? GemAttributeHelper.lessModifierLower : GemAttributeHelper.decModifierLower) : (isMultiplicative ? GemAttributeHelper.moreModifierLower : GemAttributeHelper.incModifierLower);
                final float higher = isNegative ? (isMultiplicative ? GemAttributeHelper.lessModifierHigher : GemAttributeHelper.decModifierHigher) : (isMultiplicative ? GemAttributeHelper.moreModifierHigher : GemAttributeHelper.incModifierHigher);
                float value;
                if (lower > higher) {
                    value = lower;
                }
                else {
                    final float exp = 1.0f / gemType.amplifierModifier;
                    final float multiplierScale = (float)Math.pow(random.nextFloat(), exp);
                    value = lower + Mth.func_76131_a(multiplierScale, 0.0f, 1.0f) * (higher - lower);
                }
                final ModifierType mode = isMultiplicative ? ModifierType.STACKING_MULTIPLY : ModifierType.ADDED_MULTIPLY;
                final float rValue = isMultiplicative ? (1.0f + value) : value;
                final PerkAttributeType type = entry.getType();
                if (GemAttributeHelper.allowDuplicateTypes) {
                    final DynamicAttributeModifier existing = MiscUtils.iterativeSearch(mods, mod -> mod.getAttributeType().equals(type) && mod.getMode().equals(mode));
                    if (existing != null) {
                        mods.remove(existing);
                        float combinedValue;
                        if (isMultiplicative) {
                            combinedValue = existing.getRawValue() - 1.0f + (rValue - 1.0f);
                        }
                        else {
                            combinedValue = existing.getRawValue() + rValue;
                        }
                        if (combinedValue != 0.0f) {
                            mods.add(new DynamicAttributeModifier(UUID.randomUUID(), type, mode, isMultiplicative ? (combinedValue + 1.0f) : combinedValue));
                        }
                    }
                    else {
                        mods.add(new DynamicAttributeModifier(UUID.randomUUID(), type, mode, rValue));
                    }
                }
                else {
                    mods.add(new DynamicAttributeModifier(UUID.randomUUID(), type, mode, rValue));
                }
            }
        }
        DynamicModifierHelper.addModifiers(gem, mods);
        return true;
    }
    
    @Nullable
    private static PerkAttributeEntry getWeightedResultAndRemove(final List<PerkAttributeEntry> list, final Random random) {
        if (list.isEmpty()) {
            return null;
        }
        final PerkAttributeEntry result = MiscUtils.getWeightedRandomEntry(list, random, PerkAttributeEntry::getWeight);
        if (result != null) {
            list.remove(result);
        }
        return result;
    }
    
    private static int getPotentialMods(final Random random, final float countModifier) {
        int mods = 2;
        if (random.nextFloat() < GemAttributeHelper.chance3Modifiers + countModifier) {
            ++mods;
            if (random.nextFloat() < GemAttributeHelper.chance4Modifiers + countModifier) {
                ++mods;
            }
        }
        return mods;
    }
    
    static {
        rand = new Random();
        GemAttributeHelper.chance3Modifiers = 0.4f;
        GemAttributeHelper.chance4Modifiers = 0.15f;
        GemAttributeHelper.allowDuplicateTypes = false;
        GemAttributeHelper.incModifierLower = 0.05f;
        GemAttributeHelper.incModifierHigher = 0.08f;
        GemAttributeHelper.allowNegativeModifiers = false;
        GemAttributeHelper.chanceNegative = 0.25f;
        GemAttributeHelper.decModifierLower = -0.05f;
        GemAttributeHelper.decModifierHigher = -0.08f;
        GemAttributeHelper.allowMoreLessModifiers = false;
        GemAttributeHelper.chanceMultiplicative = 0.1f;
        GemAttributeHelper.moreModifierLower = 0.05f;
        GemAttributeHelper.moreModifierHigher = 0.08f;
        GemAttributeHelper.lessModifierLower = -0.05f;
        GemAttributeHelper.lessModifierHigher = -0.08f;
    }
}
