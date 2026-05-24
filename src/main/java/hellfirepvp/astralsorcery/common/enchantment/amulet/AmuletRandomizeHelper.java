package hellfirepvp.astralsorcery.common.enchantment.amulet;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.LinkedList;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.item.enchantment.Enchantment;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentType;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.registry.AmuletEnchantmentRegistry;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.item.ItemEnchantmentAmulet;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Random;

public class AmuletRandomizeHelper
{
    public static final Config CONFIG;
    private static final Random rand;
    private static ForgeConfigSpec.DoubleValue chance2nd;
    private static ForgeConfigSpec.DoubleValue chance3rd;
    private static ForgeConfigSpec.DoubleValue chance2Level;
    private static ForgeConfigSpec.DoubleValue chanceToAll;
    private static ForgeConfigSpec.DoubleValue chanceToNonExisting;
    
    public static void rollAmulet(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemEnchantmentAmulet)) {
            return;
        }
        final List<AmuletEnchantment> ench = new ArrayList<AmuletEnchantment>();
        while (mayGetAdditionalRoll(ench)) {
            final DynamicEnchantmentType type = getRollType(ench);
            if (type != null) {
                final int lvl = getRollLevel();
                if (type.isEnchantmentSpecific()) {
                    final Enchantment e = AmuletEnchantmentRegistry.getRandomEnchant();
                    if (e == null) {
                        continue;
                    }
                    ench.add(new AmuletEnchantment(type, e, lvl));
                }
                else {
                    ench.add(new AmuletEnchantment(type, lvl));
                }
            }
        }
        ItemEnchantmentAmulet.setAmuletEnchantments(stack, collapseEnchantments(ench));
    }
    
    @Nullable
    private static DynamicEnchantmentType getRollType(final List<AmuletEnchantment> existing) {
        final int exAll = getAdditionAll(existing);
        switch (existing.size()) {
            case 0:
            case 1: {
                if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chanceToAll.get()) {
                    return DynamicEnchantmentType.ADD_TO_EXISTING_ALL;
                }
                if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chanceToNonExisting.get()) {
                    return DynamicEnchantmentType.ADD_TO_SPECIFIC;
                }
                return DynamicEnchantmentType.ADD_TO_EXISTING_SPECIFIC;
            }
            case 2: {
                if (exAll > 1) {
                    return null;
                }
                if (exAll == 1) {
                    if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chanceToNonExisting.get()) {
                        return DynamicEnchantmentType.ADD_TO_SPECIFIC;
                    }
                    return DynamicEnchantmentType.ADD_TO_EXISTING_SPECIFIC;
                }
                else {
                    if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chanceToAll.get()) {
                        return DynamicEnchantmentType.ADD_TO_EXISTING_ALL;
                    }
                    if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chanceToNonExisting.get()) {
                        return DynamicEnchantmentType.ADD_TO_SPECIFIC;
                    }
                    return DynamicEnchantmentType.ADD_TO_EXISTING_SPECIFIC;
                }
                break;
            }
            default: {
                return null;
            }
        }
    }
    
    private static int getRollLevel() {
        if (AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chance2Level.get()) {
            return 2;
        }
        return 1;
    }
    
    private static boolean mayGetAdditionalRoll(final List<AmuletEnchantment> existing) {
        if (existing.isEmpty()) {
            return true;
        }
        switch (existing.size()) {
            case 1: {
                return AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chance2nd.get();
            }
            case 2: {
                return getAdditionAll(existing) < 2 && AmuletRandomizeHelper.rand.nextFloat() < (double)AmuletRandomizeHelper.chance3rd.get();
            }
            default: {
                return false;
            }
        }
    }
    
    private static int getAdditionAll(final List<AmuletEnchantment> ench) {
        int i = 0;
        for (final AmuletEnchantment e : ench) {
            if (e.getType().equals(DynamicEnchantmentType.ADD_TO_EXISTING_ALL)) {
                ++i;
            }
        }
        return i;
    }
    
    private static List<AmuletEnchantment> collapseEnchantments(final List<AmuletEnchantment> ench) {
        final List<AmuletEnchantment> enchantments = new LinkedList<AmuletEnchantment>();
        for (final AmuletEnchantment e : ench) {
            boolean found = false;
            for (final AmuletEnchantment ex : enchantments) {
                if (ex.canMerge(e)) {
                    ex.merge(e);
                    found = true;
                    break;
                }
            }
            if (!found) {
                enchantments.add(e);
            }
        }
        return enchantments;
    }
    
    static {
        CONFIG = new Config();
        rand = new Random();
    }
    
    public static class Config extends ConfigEntry
    {
        public Config() {
            super("enchantment_amulet");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            AmuletRandomizeHelper.chance2nd = cfgBuilder.comment("Defines the chance to roll a 2nd-enchantment-manipulating roll on the amulet. Value defines a percent chance from 0% to 100%. Setting this to 0 also prevents a 3rd roll").translation(this.translationKey("chance2nd")).defineInRange("chance2nd", 0.8, 0.0, 1.0);
            AmuletRandomizeHelper.chance3rd = cfgBuilder.comment("Defines the chance to roll a 3rd-enchantment-manipulation roll on the amulet. Value defines a percent chance from 0% to 100%.").translation(this.translationKey("chance3rd")).defineInRange("chance3rd", 0.25, 0.0, 1.0);
            AmuletRandomizeHelper.chance2Level = cfgBuilder.comment("Defines the chance the roll will be +2 instead of +1 to existing enchantment/to enchantment/to all enchantments.").translation(this.translationKey("chance2Level")).defineInRange("chance2Level", 0.15, 0.0, 1.0);
            AmuletRandomizeHelper.chanceToAll = cfgBuilder.comment("Defines the chance the amulet-roll 'to all existing enchantments' will appear.").translation(this.translationKey("chanceToAll")).defineInRange("chanceToAll", 0.02, 0.0, 1.0);
            AmuletRandomizeHelper.chanceToNonExisting = cfgBuilder.comment("Defines the chance the amulet roll 'to <encahntment>' will appear. (Don't mistake this for 'to exsting <enchantment>'!)").translation(this.translationKey("chanceToNonExisting")).defineInRange("chanceToNonExisting", 0.35, 0.0, 1.0);
        }
    }
}
