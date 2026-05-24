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
import net.minecraft.world.level.item.Item;
import java.util.Random;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.OreItemRarityEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class OreItemRarityRegistry extends ConfigDataAdapter<OreItemRarityEntry>
{
    public static final OreItemRarityRegistry VOID_TRASH_REWARD;
    private final String fileName;
    
    private OreItemRarityRegistry(final String fileName) {
        this.fileName = fileName;
    }
    
    @Nullable
    public Item getRandomItem(final Random rand) {
        final List<OreItemRarityEntry> entries = this.getConfiguredValues();
        final Set<OreItemRarityEntry> visitedEntires = new HashSet<OreItemRarityEntry>();
        while (visitedEntires.size() < entries.size()) {
            final OreItemRarityEntry entry = MiscUtils.getWeightedRandomEntry((Collection<OreItemRarityEntry>)entries.stream().filter(value -> !visitedEntires.contains(value)).collect((Collector<? super Object, ?, Collection<T>>)Collectors.toList()), rand, OreItemRarityEntry::getWeight);
            if (entry == null) {
                return null;
            }
            visitedEntires.add(entry);
            final Item i = entry.getRandomItem(rand);
            if (i != null) {
                return i;
            }
        }
        return null;
    }
    
    @Override
    public List<OreItemRarityEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new OreItemRarityEntry[] { new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_COAL, 5200), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_IRON, 2500), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_GOLD, 550), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_LAPIS, 360), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_REDSTONE, 700), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_DIAMOND, 120), new OreItemRarityEntry((ITag.INamedTag<Item>)Tags.Items.ORES_EMERALD, 100) });
    }
    
    @Override
    public String getSectionName() {
        return this.fileName;
    }
    
    @Override
    public String getCommentDescription() {
        return "Format: '<tagName>;<integerWeight>' Defines random-weighted ore-selection data. Define item-tags to select from here with associated weight. Specific mods can be blacklisted in the general AstralSorcery config in 'modidOreBlacklist'.";
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
    public OreItemRarityEntry deserialize(final String string) throws IllegalArgumentException {
        return OreItemRarityEntry.deserialize(string);
    }
    
    static {
        VOID_TRASH_REWARD = new OreItemRarityRegistry("perk_void_trash_ore");
    }
}
