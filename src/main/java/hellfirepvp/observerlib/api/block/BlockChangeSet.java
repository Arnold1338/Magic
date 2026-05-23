package hellfirepvp.observerlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface BlockChangeSet {
    boolean hasChange(BlockPos pos);

    boolean isEmpty();

    @Nonnull
    Collection<StateChange> getChanges();

    interface StateChange {
        @Nonnull
        BlockPos getAbsolutePosition();

        @Nonnull
        BlockPos getRelativePosition();

        @Nonnull
        BlockState getOldState();

        @Nonnull
        BlockState getNewState();
    }
}
