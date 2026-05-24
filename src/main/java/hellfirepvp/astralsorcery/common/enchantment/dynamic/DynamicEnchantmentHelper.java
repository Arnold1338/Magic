package hellfirepvp.astralsorcery.common.enchantment.dynamic;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.event.DynamicEnchantmentEvent;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.enchantment.amulet.AmuletEnchantmentHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistryEntry;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraft.item.BookItem;
import hellfirepvp.astralsorcery.common.util.object.ObjectReference;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import java.util.HashSet;
import net.minecraft.nbt.ListTag;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.config.registry.AmuletEnchantmentRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.QuickChargeEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class DynamicEnchantmentHelper
{
    private static int getNewEnchantmentLevel(int current, final String enchStr, final ItemStack item, @Nullable final List<DynamicEnchantment> context) {
        final Enchantment enchantment = (Enchantment)ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchStr));
        if (enchantment != null) {
            current = getNewEnchantmentLevel(current, enchantment, item, context);
            if (enchantment instanceof QuickChargeEnchantment) {
                current = MathHelper.func_76125_a(current, 0, 5);
            }
        }
        return current;
    }
    
    public static int getNewEnchantmentLevel(int current, final Enchantment enchantment, final ItemStack item, @Nullable final List<DynamicEnchantment> context) {
        if (!canHaveDynamicEnchantment(item)) {
            return current;
        }
        if (enchantment == null || !AmuletEnchantmentRegistry.canBeInfluenced(enchantment)) {
            return current;
        }
        final List<DynamicEnchantment> modifiers = (context != null) ? context : fireEnchantmentGatheringEvent(item);
        for (final DynamicEnchantment mod : modifiers) {
            final Enchantment target = mod.getEnchantment();
            switch (mod.getType()) {
                case ADD_TO_SPECIFIC: {
                    if (enchantment.equals(target)) {
                        current += mod.getLevelAddition();
                        continue;
                    }
                    continue;
                }
                case ADD_TO_EXISTING_SPECIFIC: {
                    if (enchantment.equals(target) && current > 0) {
                        current += mod.getLevelAddition();
                        continue;
                    }
                    continue;
                }
                case ADD_TO_EXISTING_ALL: {
                    if (current > 0) {
                        current += mod.getLevelAddition();
                        continue;
                    }
                    continue;
                }
            }
        }
        if (enchantment instanceof QuickChargeEnchantment) {
            current = MathHelper.func_76125_a(current, 0, 5);
        }
        return current;
    }
    
    public static ListTag modifyEnchantmentTags(final ListTag existingEnchantments, final ItemStack stack) {
        if (!canHaveDynamicEnchantment(stack)) {
            return existingEnchantments;
        }
        final List<DynamicEnchantment> context = fireEnchantmentGatheringEvent(stack);
        if (context.isEmpty()) {
            return existingEnchantments;
        }
        final ListTag returnNew = new ListTag();
        final Set<String> enchantments = new HashSet<String>(existingEnchantments.size());
        for (int i = 0; i < existingEnchantments.size(); ++i) {
            final CompoundTag cmp = existingEnchantments.getCompound(i);
            final String enchKey = cmp.getString("id");
            final int lvl = cmp.getInt("lvl");
            final int newLvl = getNewEnchantmentLevel(lvl, enchKey, stack, context);
            final CompoundTag newEnchTag = new CompoundTag();
            newEnchTag.putString("id", enchKey);
            newEnchTag.putInt("lvl", newLvl);
            returnNew.add((Object)newEnchTag);
            enchantments.add(enchKey);
        }
        for (final DynamicEnchantment mod : context) {
            if (mod.getType() == DynamicEnchantmentType.ADD_TO_SPECIFIC) {
                final Enchantment ench = mod.getEnchantment();
                if (ench == null) {
                    continue;
                }
                if (!AmuletEnchantmentRegistry.canBeInfluenced(ench)) {
                    continue;
                }
                if (!stack.canApplyAtEnchantingTable(ench)) {
                    continue;
                }
                final String enchName = ench.getRegistryName().toString();
                if (enchantments.contains(enchName)) {
                    continue;
                }
                final CompoundTag newEnchTag2 = new CompoundTag();
                newEnchTag2.putString("id", enchName);
                newEnchTag2.putInt("lvl", getNewEnchantmentLevel(0, ench, stack, context));
                returnNew.add((Object)newEnchTag2);
            }
        }
        return returnNew;
    }
    
    public static Map<Enchantment, Integer> addNewLevels(final Map<Enchantment, Integer> enchantmentLevelMap, final ItemStack stack) {
        if (!canHaveDynamicEnchantment(stack)) {
            return enchantmentLevelMap;
        }
        final List<DynamicEnchantment> context = fireEnchantmentGatheringEvent(stack);
        if (context.isEmpty()) {
            return enchantmentLevelMap;
        }
        final Map<Enchantment, Integer> copyRet = Maps.newLinkedHashMap((Map)enchantmentLevelMap);
        enchantmentLevelMap.clear();
        for (final Map.Entry<Enchantment, Integer> enchant : copyRet.entrySet()) {
            enchantmentLevelMap.put(enchant.getKey(), getNewEnchantmentLevel(enchant.getValue(), enchant.getKey(), stack, context));
        }
        for (final DynamicEnchantment mod : context) {
            if (mod.getType() == DynamicEnchantmentType.ADD_TO_SPECIFIC) {
                final Enchantment ench = mod.getEnchantment();
                if (ench == null) {
                    continue;
                }
                if (!AmuletEnchantmentRegistry.canBeInfluenced(ench)) {
                    continue;
                }
                if (enchantmentLevelMap.containsKey(ench)) {
                    continue;
                }
                enchantmentLevelMap.put(ench, getNewEnchantmentLevel(0, ench, stack, context));
            }
        }
        return enchantmentLevelMap;
    }
    
    public static boolean canHaveDynamicEnchantment(final ItemStack stack) {
        if (!EventFlags.CAN_HAVE_DYN_ENCHANTMENTS.isSet()) {
            final ObjectReference<Boolean> mayHaveDynamicEnchantments = new ObjectReference<Boolean>(false);
            EventFlags.CAN_HAVE_DYN_ENCHANTMENTS.executeWithFlag(() -> {
                if (stack.isEmpty()) {
                    return;
                }
                else {
                    final Item i = stack.getItem();
                    if (i.getRegistryName() == null) {
                        return;
                    }
                    else {
                        try {
                            if (!i.func_77616_k(stack) || i instanceof BookItem) {
                                return;
                            }
                        }
                        catch (final NullPointerException exc) {
                            return;
                        }
                        if (Mods.DRACONIC_EVOLUTION.owns((IForgeRegistryEntry<?>)stack.getItem())) {
                            return;
                        }
                        else {
                            mayHaveDynamicEnchantments.set(true);
                            return;
                        }
                    }
                }
            });
            return mayHaveDynamicEnchantments.get();
        }
        return false;
    }
    
    private static List<DynamicEnchantment> fireEnchantmentGatheringEvent(final ItemStack tool) {
        final Player foundEntity = AmuletEnchantmentHelper.getPlayerHavingTool(tool);
        if (foundEntity == null) {
            return new ArrayList<DynamicEnchantment>();
        }
        final DynamicEnchantmentEvent.Add addEvent = new DynamicEnchantmentEvent.Add(tool, foundEntity);
        if (MinecraftForge.EVENT_BUS.post((Event)addEvent)) {
            return new ArrayList<DynamicEnchantment>();
        }
        final DynamicEnchantmentEvent.Modify modifyEvent = new DynamicEnchantmentEvent.Modify(tool, addEvent.getEnchantmentsToApply(), foundEntity);
        if (MinecraftForge.EVENT_BUS.post((Event)modifyEvent)) {
            return new ArrayList<DynamicEnchantment>();
        }
        return modifyEvent.getEnchantmentsToApply();
    }
}
