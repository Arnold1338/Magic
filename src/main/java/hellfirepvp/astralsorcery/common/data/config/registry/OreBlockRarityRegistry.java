package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import java.util.function.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.world.level.block.Block;
import java.util.Random;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.OreBlockRarityEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class OreBlockRarityRegistry extends ConfigDataAdapter<OreBlockRarityEntry>
{
    public static final OreBlockRarityRegistry STONE_ENRICHMENT;
    public static final OreBlockRarityRegistry MINERALIS_RITUAL;
    private final String fileName;
    
    private OreBlockRarityRegistry(final String fileName) {
        this.fileName = fileName;
    }
    
    @Nullable
    public Block getRandomBlock(final Random rand) {
        final List<OreBlockRarityEntry> entries = this.getConfiguredValues();
        final Set<OreBlockRarityEntry> visitedEntires = new HashSet<OreBlockRarityEntry>();
        while (visitedEntires.size() < entries.size()) {
            final OreBlockRarityEntry entry = MiscUtils.getWeightedRandomEntry((Collection<OreBlockRarityEntry>)entries.stream().filter(e -> !visitedEntires.contains(e)).collect((Collector<? super Object, ?, Collection<T>>)Collectors.toList()), rand, OreBlockRarityEntry::getWeight);
            if (entry == null) {
                return null;
            }
            visitedEntires.add(entry);
            final Block b = entry.getRandomBlock(rand);
            if (b != null) {
                return b;
            }
        }
        return null;
    }
    
    @Override
    public List<OreBlockRarityEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new OreBlockRarityEntry[] { new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_COAL, 5200), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_IRON, 2500), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_GOLD, 440), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_LAPIS, 200), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_REDSTONE, 600), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_EMERALD, 60), new OreBlockRarityEntry((ITag.INamedTag<Block>)Tags.Blocks.ORES_DIAMOND, 40) });
    }
    
    @Override
    public String getSectionName() {
        return this.fileName;
    }
    
    @Override
    public String getCommentDescription() {
        return "Format: '<tagName>;<integerWeight>' Defines random-weighted ore-selection data. Define block-tags to select from here with associated weight. Specific mods can be blacklisted in the general AstralSorcery config in 'modidOreBlacklist'.";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String;
    }
    
    @Nullable
    @Override
    public OreBlockRarityEntry deserialize(final String string) throws IllegalArgumentException {
        return OreBlockRarityEntry.deserialize(string);
    }
    
    static {
        STONE_ENRICHMENT = new OreBlockRarityRegistry("perk_stone_enrichment_ore");
        MINERALIS_RITUAL = new OreBlockRarityRegistry("mineralis_ritual_ore");
    }
}
