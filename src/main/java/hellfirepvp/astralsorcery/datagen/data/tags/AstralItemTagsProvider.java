package hellfirepvp.astralsorcery.datagen.data.tags;

import net.minecraft.data.TagsProvider;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.common.Tags;
import hellfirepvp.astralsorcery.common.lib.TagsAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

public class AstralItemTagsProvider extends ItemTagsProvider
{
    public AstralItemTagsProvider(final DataGenerator dataGenerator, final BlockTagsProvider blockTagsProvider, final ExistingFileHelper fileHelper) {
        super(dataGenerator, blockTagsProvider, "astralsorcery", fileHelper);
    }
    
    protected void func_200432_c() {
        this.tag((ITag.INamedTag<Item>)ItemTags.field_226160_P_).func_240534_a_((Object[])new Item[] { ItemsAS.TOME });
        this.tag(TagsAS.Items.CURIOS_NECKLACE).func_240534_a_((Object[])new Item[] { ItemsAS.ENCHANTMENT_AMULET });
        this.tag(TagsAS.Items.FORGE_GEM_AQUAMARINE).func_240534_a_((Object[])new Item[] { ItemsAS.AQUAMARINE });
        this.tag(TagsAS.Items.COLORED_LENS).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_BREAK }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_DAMAGE }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_FIRE }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_GROWTH }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_PUSH }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_REGENERATION }).func_240534_a_((Object[])new Item[] { ItemsAS.COLORED_LENS_SPECTRAL });
        this.tag(TagsAS.Items.DUSTS_STARDUST).func_240534_a_((Object[])new Item[] { ItemsAS.STARDUST });
        this.tag(TagsAS.Items.INGOTS_STARMETAL).func_240534_a_((Object[])new Item[] { ItemsAS.STARMETAL_INGOT });
        this.tag((ITag.INamedTag<Item>)Tags.Items.ORES).func_240534_a_((Object[])new Item[] { BlocksAS.STARMETAL_ORE.func_199767_j() }).func_240534_a_((Object[])new Item[] { BlocksAS.AQUAMARINE_SAND_ORE.func_199767_j() }).func_240534_a_((Object[])new Item[] { BlocksAS.ROCK_CRYSTAL_ORE.func_199767_j() });
    }
    
    private TagsProvider.Builder<Item> tag(final ITag.INamedTag<Item> tag) {
        return (TagsProvider.Builder<Item>)this.func_240522_a_((ITag.INamedTag)tag);
    }
}
