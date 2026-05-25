package hellfirepvp.astralsorcery.common.crafting.nojson;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.crafting.nojson.meltable.FurnaceMeltableRecipe;
import net.minecraftforge.common.Tags;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.crafting.nojson.meltable.BlockMeltableRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import hellfirepvp.astralsorcery.common.crafting.nojson.meltable.WorldMeltableRecipe;

public class WorldMeltableRegistry extends CustomRecipeRegistry<WorldMeltableRecipe>
{
    public static final WorldMeltableRegistry INSTANCE;
    
    @Override
    public void init() {
        ((CustomRecipeRegistry<BlockMeltableRecipe>)this).register(BlockMeltableRecipe.of((ITag.INamedTag<Block>)BlockTags.field_205213_E, Blocks.field_150355_j.defaultBlockState()));
        ((CustomRecipeRegistry<BlockMeltableRecipe>)this).register(BlockMeltableRecipe.of((ITag.INamedTag<Block>)Tags.Blocks.STONE, Blocks.field_150353_l.defaultBlockState()));
        ((CustomRecipeRegistry<BlockMeltableRecipe>)this).register(BlockMeltableRecipe.of((ITag.INamedTag<Block>)Tags.Blocks.NETHERRACK, Blocks.field_150353_l.defaultBlockState()));
        ((CustomRecipeRegistry<BlockMeltableRecipe>)this).register(BlockMeltableRecipe.of((ITag.INamedTag<Block>)Tags.Blocks.OBSIDIAN, Blocks.field_150353_l.defaultBlockState()));
        ((CustomRecipeRegistry<BlockMeltableRecipe>)this).register(BlockMeltableRecipe.of(Blocks.field_196814_hQ.defaultBlockState(), Blocks.field_150353_l.defaultBlockState()));
        ((CustomRecipeRegistry<FurnaceMeltableRecipe>)this).register(new FurnaceMeltableRecipe());
    }
    
    @Nullable
    public WorldMeltableRecipe getRecipeFor(final Level world, final BlockPos pos) {
        return this.getRecipes().stream().filter(recipe -> recipe.canMelt(world, pos)).findFirst().orElse(null);
    }
    
    static {
        INSTANCE = new WorldMeltableRegistry();
    }
}
