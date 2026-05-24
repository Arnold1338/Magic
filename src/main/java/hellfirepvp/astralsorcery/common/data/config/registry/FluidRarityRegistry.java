package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Random;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.base.Mods;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.FluidRarityEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class FluidRarityRegistry extends ConfigDataAdapter<FluidRarityEntry>
{
    public static final FluidRarityRegistry INSTANCE;
    
    private FluidRarityRegistry() {
    }
    
    @Override
    public List<FluidRarityEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new FluidRarityEntry[] { new FluidRarityEntry(Mods.MINECRAFT.key("water"), 14000, Integer.MAX_VALUE, Integer.MAX_VALUE), new FluidRarityEntry(Mods.MINECRAFT.key("lava"), 7500, 4000000), new FluidRarityEntry(key("immersivepetrolium", "oil"), 5000, 2500000), new FluidRarityEntry(key("immersivepetroleum", "napalm"), 100, 750000), new FluidRarityEntry(key("industrialforegoing", "essence_fluid"), 300, 500000), new FluidRarityEntry(key("industrialforegoing", "sewage_fluid"), 250, 10000000), new FluidRarityEntry(key("industrialforegoing", "sludge_fluid"), 200, 3000000), new FluidRarityEntry(key("mekanism", "flowing_heavy_water"), 2500, 10000000), new FluidRarityEntry(key("mekanismgenerators", "flowing_fusion_fuel"), 100, 1000000), new FluidRarityEntry(key("bloodmagic", "life_essence_fluid_flowing"), 250, 5000000) });
    }
    
    @Nullable
    @Override
    public synchronized FluidRarityEntry getRandomValue(final Random rand) {
        return MiscUtils.getWeightedRandomEntry(this.getConfiguredValues(), rand, FluidRarityEntry::getRarity);
    }
    
    @Override
    public String getSectionName() {
        return "fluid_rarities";
    }
    
    @Override
    public String getCommentDescription() {
        return "Defines fluid-rarities and amounts for the evershifting fountain's neromantic prime. The lower the relative rarity, the more rare the fluid. Format: <FluidName>;<guaranteedMbAmount>;<additionalRandomMbAmount>;<rarity>";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Override
    public FluidRarityEntry deserialize(final String string) throws IllegalArgumentException {
        return FluidRarityEntry.deserialize(string);
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String;
    }
    
    private static ResourceLocation key(final String domain, final String path) {
        return new ResourceLocation(domain, path);
    }
    
    static {
        INSTANCE = new FluidRarityRegistry();
    }
}
