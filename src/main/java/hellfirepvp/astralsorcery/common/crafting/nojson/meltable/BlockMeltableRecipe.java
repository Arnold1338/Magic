package hellfirepvp.astralsorcery.common.crafting.nojson.meltable;

import net.minecraft.world.level.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.level.block.Block;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicates;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import java.util.function.BiFunction;

public class BlockMeltableRecipe extends WorldMeltableRecipe
{
    private final BiFunction<WorldBlockPos, BlockState, BlockState> outputGenerator;
    
    public BlockMeltableRecipe(final ResourceLocation key, final BlockPredicate matcher, final BlockState output) {
        this(key, matcher, (worldPos, state) -> output);
    }
    
    public BlockMeltableRecipe(final ResourceLocation key, final BlockPredicate matcher, final BiFunction<WorldBlockPos, BlockState, BlockState> outputGenerator) {
        super(key, matcher);
        this.outputGenerator = outputGenerator;
    }
    
    public static BlockMeltableRecipe of(final BlockState stateIn, final BlockState stateOut) {
        return new BlockMeltableRecipe(AstralSorcery.key(stateIn.getBlock().getRegistryName().func_110623_a()), BlockPredicates.isState(stateIn), stateOut);
    }
    
    public static BlockMeltableRecipe of(final ITag.INamedTag<Block> blockTagIn, final BlockState stateOut) {
        return new BlockMeltableRecipe(AstralSorcery.key(String.format("tag_%s", blockTagIn.func_230234_a_().func_110623_a())), BlockPredicates.isInTag((ITag<Block>)blockTagIn), stateOut);
    }
    
    @Override
    public void doOutput(final World world, final BlockPos pos, final BlockState state, final Consumer<ItemStack> itemOutput) {
        final BlockState generated = this.outputGenerator.apply(WorldBlockPos.wrapServer(world, pos), state);
        if (generated != state) {
            world.func_180501_a(pos, generated, 3);
        }
    }
}
