package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Blocks;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.world.level.block.Block;
import java.util.List;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.level.block.state.BlockState;
import java.util.function.Predicate;

public class SimpleBlockPredicate implements BlockPredicate, Predicate<BlockState>
{
    final Either<List<BlockState>, Block> validMatch;
    
    public SimpleBlockPredicate(final Block block) {
        this.validMatch = (Either<List<BlockState>, Block>)Either.right((Object)block);
    }
    
    public SimpleBlockPredicate(final BlockState... states) {
        this.validMatch = (Either<List<BlockState>, Block>)Either.left((Object)Arrays.asList(states));
    }
    
    public List<String> getAsConfigList() {
        final List<String> out = new ArrayList<String>();
        this.validMatch.ifLeft(states -> states.stream().map(BlockStateHelper::serialize).forEach(out::add)).ifRight(block -> out.add(BlockStateHelper.serialize(block)));
        return out;
    }
    
    @Nullable
    public static SimpleBlockPredicate fromConfig(final String serialized) {
        if (BlockStateHelper.isMissingStateInformation(serialized)) {
            final Block b = BlockStateHelper.deserializeBlock(serialized);
            if (b != Blocks.field_150350_a) {
                return new SimpleBlockPredicate(b);
            }
        }
        else {
            final BlockState state = BlockStateHelper.deserialize(serialized);
            if (state.getBlock() != Blocks.field_150350_a) {
                return new SimpleBlockPredicate(new BlockState[] { state });
            }
        }
        return null;
    }
    
    @Override
    public boolean test(final BlockState state) {
        final Either<Boolean, Boolean> matchResult = (Either<Boolean, Boolean>)this.validMatch.mapBoth(states -> states.contains(state), block -> state.getBlock().equals(block));
        return matchResult.left().orElse(matchResult.right().orElse(false));
    }
    
    @Override
    public boolean test(final World world, final BlockPos pos, final BlockState state) {
        return this.test(state);
    }
}
