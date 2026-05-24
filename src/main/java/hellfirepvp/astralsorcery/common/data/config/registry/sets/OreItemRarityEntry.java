package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nonnull;
import net.minecraft.tags.ItemTags;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import java.util.List;
import java.util.Collection;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class OreItemRarityEntry implements ConfigDataSet
{
    private final ITag<Item> itemTag;
    private final ResourceLocation key;
    private final int weight;
    
    public OreItemRarityEntry(final ITag<Item> itemTag, final ResourceLocation key, final int weight) {
        this.itemTag = itemTag;
        this.key = key;
        this.weight = weight;
    }
    
    public OreItemRarityEntry(final ITag.INamedTag<Item> itemTag, final int weight) {
        this((ITag<Item>)itemTag, itemTag.func_230234_a_(), weight);
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    @Nullable
    public Item getRandomItem(final Random rand) {
        return MiscUtils.getRandomEntry((Collection<Item>)this.itemTag.func_230236_b_().stream().filter(item -> !((List)GeneralConfig.CONFIG.modidOreBlacklist.get()).contains(item.getRegistryName().func_110624_b())).collect(Collectors.toList()), rand);
    }
    
    @Nullable
    public static OreItemRarityEntry deserialize(final String str) throws IllegalArgumentException {
        final String[] split = str.split(";");
        if (split.length != 2) {
            return null;
        }
        final ResourceLocation keyItemTag = new ResourceLocation(split[0]);
        final ITag<Item> itemTag = (ITag<Item>)ItemTags.func_199903_a().func_199910_a(keyItemTag);
        if (itemTag == null) {
            return null;
        }
        final String strWeight = split[1];
        int weight;
        try {
            weight = Integer.parseInt(strWeight);
        }
        catch (final NumberFormatException exc) {
            return null;
        }
        return new OreItemRarityEntry(itemTag, keyItemTag, weight);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return String.format("%s;%s", this.key.toString(), this.weight);
    }
}
