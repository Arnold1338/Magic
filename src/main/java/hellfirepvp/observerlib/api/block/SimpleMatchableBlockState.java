package hellfirepvp.observerlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SimpleMatchableBlockState implements MatchableState {
    private static final int CYCLE_STATES = 20;
    private final List<BlockState> matchingStates;

    public SimpleMatchableBlockState(BlockState... matchingStates) {
        this(Arrays.asList(matchingStates));
    }

    public SimpleMatchableBlockState(List<BlockState> matchingStates) {
        this.matchingStates = matchingStates;
    }

    @Nonnull
    @Override
    public BlockState getDescriptiveState(long tick) {
        if (this.matchingStates.isEmpty()) return Blocks.AIR.defaultBlockState();
        int cycleState = Math.max(2, 20 / this.matchingStates.size());
        int part = (int)(tick % (cycleState * this.matchingStates.size()));
        return this.matchingStates.get(part / cycleState);
    }

    @Override
    public boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos absolutePosition, @Nonnull BlockState state) {
        return this.matchingStates.contains(state);
    }
}
