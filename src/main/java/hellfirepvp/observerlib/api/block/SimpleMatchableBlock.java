package hellfirepvp.observerlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleMatchableBlock implements MatchableState {
    private static final int CYCLE_STATES = 20;
    private final List<Block> matchingBlocks;
    private final List<BlockState> displayStates;

    public SimpleMatchableBlock(Block... matchingBlocks) {
        this(Arrays.asList(matchingBlocks));
    }

    public SimpleMatchableBlock(List<Block> matchingBlocks) {
        this.displayStates = new ArrayList<>();
        this.matchingBlocks = matchingBlocks;
        for (Block b : this.matchingBlocks) {
            this.displayStates.addAll(b.getStateDefinition().getPossibleStates());
        }
    }

    @Nonnull
    @Override
    public BlockState getDescriptiveState(long tick) {
        if (this.displayStates.isEmpty()) return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        int cycleState = Math.max(2, 20 / this.displayStates.size());
        int part = (int)(tick % (cycleState * this.displayStates.size()));
        return this.displayStates.get(part / cycleState);
    }

    @Override
    public boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos absolutePosition, @Nonnull BlockState state) {
        return this.matchingBlocks.contains(state.getBlock());
    }
}
