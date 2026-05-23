package hellfirepvp.observerlib.api.structure;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import hellfirepvp.observerlib.api.util.ContentSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Structure extends ContentSerializable {
    @Nonnull
    Map<BlockPos, ? extends MatchableState> getContents();

    @Nonnull
    Map<BlockPos, ? extends MatchableTile<? extends BlockEntity>> getTileEntities();

    Vec3i getMaximumOffset();

    Vec3i getMinimumOffset();

    default boolean hasBlockAt(BlockPos offset) {
        return this.getContents().containsKey(offset);
    }

    @Nonnull
    default MatchableState getBlockStateAt(BlockPos offset) {
        if (!this.hasBlockAt(offset)) {
            return MatchableState.AIR;
        }
        MatchableState state = this.getContents().get(offset);
        return state != null ? state : MatchableState.AIR;
    }

    default boolean hasTileAt(BlockPos offset) {
        return this.getTileEntities().containsKey(offset);
    }

    @Nullable
    default MatchableTile<? extends BlockEntity> getTileEntityAt(BlockPos offset) {
        if (!this.hasTileAt(offset)) {
            return null;
        }
        return this.getTileEntities().get(offset);
    }

    default List<Tuple<BlockPos, ? extends MatchableState>> getStructureSlice(int yOffset) {
        return this.getContents().entrySet().stream()
            .filter(e -> e.getKey().getY() == yOffset)
            .map(e -> new Tuple<>(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }
}
