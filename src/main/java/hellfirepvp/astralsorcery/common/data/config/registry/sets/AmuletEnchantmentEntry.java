package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nullable;
import net.minecraftforge.registries.ForgeRegistries;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nonnull;
import net.minecraft.world.item.enchantment.Enchantment;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class AmuletEnchantmentEntry implements ConfigDataSet, Comparable<AmuletEnchantmentEntry>
{
    private final Enchantment enchantment;
    private final int weight;
    
    public AmuletEnchantmentEntry(final Enchantment ench, final int weight) {
        this.enchantment = ench;
        this.weight = weight;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public Enchantment getEnchantment() {
        return this.enchantment;
    }
    
    @Override
    public int compareTo(final AmuletEnchantmentEntry o) {
        return Integer.compare(this.weight, o.weight);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return this.enchantment.getRegistryName().toString() + ";" + this.weight;
    }
    
    @Nullable
    public static AmuletEnchantmentEntry deserialize(final String str) {
        final String[] spl = str.split(";");
        if (spl.length < 2) {
            return null;
        }
        final String enchantmentKey = spl[0];
        final String weight = spl[1];
        final ResourceLocation registryName = new ResourceLocation(enchantmentKey);
        if (registryName.toString().equalsIgnoreCase("cofhcore:holding")) {
            AstralSorcery.log.info("Auto-ignoring amulet enchantment 'cofhcore:holding' as it's prone to cause issues.");
            return null;
        }
        final Enchantment ench = (Enchantment)ForgeRegistries.ENCHANTMENTS.getValue(registryName);
        if (ench == null) {
            AstralSorcery.log.info("Ignoring whitelist entry " + str + " for amulet enchantments - Enchantment does not exist!");
            return null;
        }
        int w;
        try {
            w = Integer.parseInt(weight);
        }
        catch (final NumberFormatException exc) {
            AstralSorcery.log.info("Ignoring whitelist entry " + str + " for amulet enchantments - last :-separated argument is not a number!");
            return null;
        }
        return new AmuletEnchantmentEntry(ench, w);
    }
}
