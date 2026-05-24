package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Iterator;
import net.minecraft.world.level.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.AmuletEnchantmentEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class AmuletEnchantmentRegistry extends ConfigDataAdapter<AmuletEnchantmentEntry>
{
    private static final Random rand;
    public static final AmuletEnchantmentRegistry INSTANCE;
    
    private AmuletEnchantmentRegistry() {
    }
    
    @Override
    public List<AmuletEnchantmentEntry> getDefaultValues() {
        final List<AmuletEnchantmentEntry> enchantments = new LinkedList<AmuletEnchantmentEntry>();
        for (final Enchantment e : ForgeRegistries.ENCHANTMENTS.getValues()) {
            if (!e.func_190936_d()) {
                enchantments.add(new AmuletEnchantmentEntry(e, e.func_77324_c().func_185270_a()));
            }
        }
        return enchantments;
    }
    
    @Nullable
    public static Enchantment getRandomEnchant() {
        final List<AmuletEnchantmentEntry> cfgValues = AmuletEnchantmentRegistry.INSTANCE.getConfiguredValues();
        if (cfgValues.isEmpty()) {
            return null;
        }
        final AmuletEnchantmentEntry entry = MiscUtils.getWeightedRandomEntry(cfgValues, AmuletEnchantmentRegistry.rand, AmuletEnchantmentEntry::getWeight);
        if (entry == null) {
            return null;
        }
        return entry.getEnchantment();
    }
    
    public static boolean canBeInfluenced(final Enchantment ench) {
        for (final AmuletEnchantmentEntry e : AmuletEnchantmentRegistry.INSTANCE.getConfiguredValues()) {
            if (e.getEnchantment().equals(ench)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getSectionName() {
        return "amulet_enchantments";
    }
    
    @Override
    public String getCommentDescription() {
        return "Defines a whitelist of which enchantments can be rolled and buffed by the enchantment-amulet. The higher the weight, the more likely that roll is selected.Format: <enchantment-registry-name>;<weight>";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String && AmuletEnchantmentEntry.deserialize((String)obj) != null;
    }
    
    @Nullable
    @Override
    public AmuletEnchantmentEntry deserialize(final String string) throws IllegalArgumentException {
        return AmuletEnchantmentEntry.deserialize(string);
    }
    
    static {
        rand = new Random();
        INSTANCE = new AmuletEnchantmentRegistry();
    }
}
