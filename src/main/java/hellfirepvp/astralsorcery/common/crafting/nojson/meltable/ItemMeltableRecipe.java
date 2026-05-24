package hellfirepvp.astralsorcery.common.crafting.nojson.meltable;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.TagKey;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicates;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import java.util.function.BiFunction;

public class ItemMeltableRecipe extends WorldMeltableRecipe
{
    private final BiFunction<WorldBlockPos, BlockState, ItemStack> outputGenerator;
    
    public ItemMeltableRecipe(final ResourceLocation key, final BlockPredicate matcher, final ItemStack output) {
        this(key, matcher, (worldPos, state) -> ItemUtils.copyStackWithSize(output, output.func_190916_E()));
    }
    
    public ItemMeltableRecipe(final ResourceLocation key, final BlockPredicate matcher, final BiFunction<WorldBlockPos, BlockState, ItemStack> outputGenerator) {
        super(key, matcher);
        this.outputGenerator = outputGenerator;
    }
    
    public static ItemMeltableRecipe of(final BlockState stateIn, final ItemStack itemOut) {
        return new ItemMeltableRecipe(AstralSorcery.key(stateIn.getBlock().getRegistryName().func_110623_a()), BlockPredicates.isState(stateIn), itemOut);
    }
    
    public static ItemMeltableRecipe of(final ITag.INamedTag<Block> blockTagIn, final ItemStack itemOut) {
        return new ItemMeltableRecipe(AstralSorcery.key(String.format("tag_%s", blockTagIn.func_230234_a_().func_110623_a())), BlockPredicates.isInTag((ITag<Block>)blockTagIn), itemOut);
    }
    
    @Override
    public void doOutput(final World world, final BlockPos pos, final BlockState state, final Consumer<ItemStack> itemOutput) {
        if (world.func_217377_a(pos, false)) {
            final ItemStack generated = this.outputGenerator.apply(WorldBlockPos.wrapServer(world, pos), state);
            if (!generated.isEmpty()) {
                itemOutput.accept(generated);
            }
        }
    }
}
