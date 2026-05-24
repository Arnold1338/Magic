package hellfirepvp.astralsorcery.common.lib;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;

public class TagsAS
{
    private TagsAS() {
    }
    
    private static ITag.INamedTag<Block> blockTagForge(final String name) {
        return blockTag(Mods.FORGE, name);
    }
    
    private static ITag.INamedTag<Block> blockTag(final Mods mod, final String name) {
        return (ITag.INamedTag<Block>)BlockTags.func_199894_a(mod.key(name).toString());
    }
    
    private static ITag.INamedTag<Item> itemTagForge(final String name) {
        return itemTag(Mods.FORGE, name);
    }
    
    private static ITag.INamedTag<Item> itemTag(final Mods mod, final String name) {
        return (ITag.INamedTag<Item>)ItemTags.func_199901_a(mod.key(name).toString());
    }
    
    public static class Blocks
    {
        public static final ITag.INamedTag<Block> MARBLE;
        public static final ITag.INamedTag<Block> ORES;
        
        static {
            MARBLE = blockTagForge("marble");
            ORES = blockTagForge("ores");
        }
    }
    
    public static class Items
    {
        public static final ITag.INamedTag<Item> CURIOS_NECKLACE;
        public static final ITag.INamedTag<Item> FORGE_GEM_AQUAMARINE;
        public static final ITag.INamedTag<Item> DUSTS_STARDUST;
        public static final ITag.INamedTag<Item> INGOTS_STARMETAL;
        public static final ITag.INamedTag<Item> COLORED_LENS;
        
        static {
            CURIOS_NECKLACE = itemTag(Mods.CURIOS, "necklace");
            FORGE_GEM_AQUAMARINE = itemTagForge("gems/aquamarine");
            DUSTS_STARDUST = itemTag(Mods.ASTRAL_SORCERY, "stardust");
            INGOTS_STARMETAL = itemTag(Mods.ASTRAL_SORCERY, "starmetal");
            COLORED_LENS = itemTag(Mods.ASTRAL_SORCERY, "colored_lens");
        }
    }
}
