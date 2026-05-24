package hellfirepvp.astralsorcery.datagen.data.tags;

import net.minecraft.data.TagsProvider;
import hellfirepvp.astralsorcery.common.lib.TagsAS;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.BlockTagsProvider;

public class AstralBlockTagsProvider extends BlockTagsProvider
{
    public AstralBlockTagsProvider(final DataGenerator generatorIn, final ExistingFileHelper existingFileHelper) {
        super(generatorIn, "astralsorcery", existingFileHelper);
    }
    
    protected void func_200432_c() {
        this.tag((ITag.INamedTag<Block>)BlockTags.field_232875_ap_).func_240534_a_((Object[])new Block[] { BlocksAS.STARMETAL });
        this.tag(TagsAS.Blocks.MARBLE).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_RAW }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_ARCH }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_BRICKS }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_CHISELED }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_ENGRAVED }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_PILLAR }).func_240534_a_((Object[])new Block[] { BlocksAS.MARBLE_RUNED });
        this.tag(TagsAS.Blocks.ORES).func_240534_a_((Object[])new Block[] { BlocksAS.STARMETAL_ORE }).func_240534_a_((Object[])new Block[] { (Block)BlocksAS.AQUAMARINE_SAND_ORE }).func_240534_a_((Object[])new Block[] { BlocksAS.ROCK_CRYSTAL_ORE });
    }
    
    private TagsProvider.Builder<Block> tag(final ITag.INamedTag<Block> tag) {
        return (TagsProvider.Builder<Block>)this.func_240522_a_((ITag.INamedTag)tag);
    }
}
