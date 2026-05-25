package hellfirepvp.astralsorcery.common.crafting.nojson.freezing;

import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicates;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import java.util.function.BiFunction;

public class BlockFreezingRecipe extends WorldFreezingRecipe
{
    private final BiFunction<WorldBlockPos, BlockState, BlockState> outputGenerator;
    
    public BlockFreezingRecipe(final ResourceLocation key, final BlockPredicate matcher, final BlockState output) {
        this(key, matcher, (worldPos, state) -> output);
    }
    
    public BlockFreezingRecipe(final ResourceLocation key, final BlockPredicate matcher, final BiFunction<WorldBlockPos, BlockState, BlockState> outputGenerator) {
        super(key, matcher);
        this.outputGenerator = outputGenerator;
    }
    
    public static BlockFreezingRecipe of(final BlockState stateIn, final BlockState stateOut) {
        return new BlockFreezingRecipe(AstralSorcery.key(stateIn.getBlock().getRegistryName().addTransientModifier()), BlockPredicates.isState(stateIn), stateOut);
    }
    
    public static BlockFreezingRecipe of(final Block blockIn, final BlockState stateOut) {
        return new BlockFreezingRecipe(AstralSorcery.key(blockIn.getRegistryName().addTransientModifier()), BlockPredicates.isBlock(blockIn), stateOut);
    }
    
    public static BlockFreezingRecipe of(final ITag.INamedTag<Block> blockTagIn, final BlockState stateOut) {
        return new BlockFreezingRecipe(AstralSorcery.key(String.format("tag_%s", blockTagIn.func_230234_a_().addTransientModifier())), BlockPredicates.isInTag((ITag<Block>)blockTagIn), stateOut);
    }
    
    @Override
    public void doOutput(final Level world, final BlockPos pos, final BlockState state, final Consumer<ItemStack> itemOutput) {
        final BlockState generated = this.outputGenerator.apply(WorldBlockPos.wrapServer(world, pos), state);
        if (generated != state) {
            world.func_180501_a(pos, generated, 11);
        }
    }
}
