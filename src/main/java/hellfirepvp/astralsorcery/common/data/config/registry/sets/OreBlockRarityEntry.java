package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nonnull;
import net.minecraft.tags.BlockTags;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import java.util.List;
import java.util.Collection;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class OreBlockRarityEntry implements ConfigDataSet
{
    private final ITag<Block> blockTag;
    private final ResourceLocation key;
    private final int weight;
    
    public OreBlockRarityEntry(final ITag<Block> blockTag, final ResourceLocation key, final int weight) {
        this.blockTag = blockTag;
        this.key = key;
        this.weight = weight;
    }
    
    public OreBlockRarityEntry(final ITag.INamedTag<Block> blockTag, final int weight) {
        this((ITag<Block>)blockTag, blockTag.func_230234_a_(), weight);
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    @Nullable
    public Block getRandomBlock(final Random rand) {
        return MiscUtils.getRandomEntry((Collection<Block>)this.blockTag.func_230236_b_().stream().filter(item -> !((List)GeneralConfig.CONFIG.modidOreBlacklist.get()).contains(item.getRegistryName().func_110624_b())).collect(Collectors.toList()), rand);
    }
    
    @Nullable
    public static OreBlockRarityEntry deserialize(final String str) throws IllegalArgumentException {
        final String[] split = str.split(";");
        if (split.length != 2) {
            return null;
        }
        final ResourceLocation keyBlockTag = new ResourceLocation(split[0]);
        final ITag<Block> blockTag = (ITag<Block>)BlockTags.func_199896_a().func_199910_a(keyBlockTag);
        if (blockTag == null) {
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
        return new OreBlockRarityEntry(blockTag, keyBlockTag, weight);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return String.format("%s;%s", this.key.toString(), this.weight);
    }
}
