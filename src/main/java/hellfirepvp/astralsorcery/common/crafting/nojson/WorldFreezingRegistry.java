package hellfirepvp.astralsorcery.common.crafting.nojson;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.crafting.nojson.freezing.FluidFreezingRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.freezing.BlockFreezingRecipe;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.crafting.nojson.freezing.WorldFreezingRecipe;

public class WorldFreezingRegistry extends CustomRecipeRegistry<WorldFreezingRecipe>
{
    public static final WorldFreezingRegistry INSTANCE;
    
    @Override
    public void init() {
        ((CustomRecipeRegistry<BlockFreezingRecipe>)this).register(BlockFreezingRecipe.of(Blocks.field_150480_ab, Blocks.field_150350_a.defaultBlockState()));
        ((CustomRecipeRegistry<BlockFreezingRecipe>)this).register(BlockFreezingRecipe.of(Blocks.field_150350_a.defaultBlockState(), Blocks.field_150432_aD.defaultBlockState()));
        ((CustomRecipeRegistry<BlockFreezingRecipe>)this).register(BlockFreezingRecipe.of(Blocks.field_201941_jj.defaultBlockState(), Blocks.field_150403_cj.defaultBlockState()));
        ((CustomRecipeRegistry<FluidFreezingRecipe>)this).register(new FluidFreezingRecipe());
    }
    
    @Nullable
    public WorldFreezingRecipe getRecipeFor(final World world, final BlockPos pos) {
        return this.getRecipes().stream().filter(recipe -> recipe.canFreeze(world, pos)).findFirst().orElse(null);
    }
    
    static {
        INSTANCE = new WorldFreezingRegistry();
    }
}
