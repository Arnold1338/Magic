package hellfirepvp.observerlib.api.structure;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface PlaceableStructure extends Structure {
    default Map<BlockPos, BlockState> placeInWorld(LevelAccessor world, BlockPos center, Predicate<BlockPos> posFilter) {
        Map<BlockPos, BlockState> result = new HashMap<>();
        for (Map.Entry<BlockPos, ? extends MatchableState> entry : this.getContents().entrySet()) {
            MatchableState match = entry.getValue();
            BlockPos at = center.offset(entry.getKey());
            if (!posFilter.test(at)) continue;
            BlockState state = match.getDescriptiveState(0L);
            if (!world.setBlock(at, state, 3)) continue;
            result.put(at, state);
            BlockEntity placed = world.getBlockEntity(at);
            if (placed == null || !this.hasTileAt(entry.getKey())) continue;
            MatchableTile<?> matchTile = this.getTileEntityAt(entry.getKey());
            if (matchTile == null) continue;
            ((MatchableTile) matchTile).postPlacement(placed, world, entry.getKey());
        }
        return result;
    }

    default Map<BlockPos, BlockState> placeInWorld(LevelAccessor world, BlockPos center,
                                                    Predicate<BlockPos> posFilter, PastPlaceProcessor processor) {
        Map<BlockPos, BlockState> result = this.placeInWorld(world, center, posFilter);
        if (processor != null) {
            for (Map.Entry<BlockPos, BlockState> entry : result.entrySet()) {
                if (posFilter.test(entry.getKey())) {
                    processor.process(world, entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    interface PastPlaceProcessor {
        void process(LevelAccessor world, BlockPos pos, BlockState state);
    }
}
