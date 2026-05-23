package hellfirepvp.observerlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockStructureObserver {
    default boolean removeWithNewState(Level world, BlockPos pos, BlockState oldState, BlockState newState) {
        return oldState != newState;
    }
}
