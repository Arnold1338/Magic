package hellfirepvp.observerlib.api.structure;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MatchableStructure extends Structure {
    ResourceLocation getRegistryName();

    default boolean matches(@Nonnull BlockGetter reader, @Nonnull BlockPos center) {
        for (Map.Entry<BlockPos, ? extends MatchableState> entry : this.getContents().entrySet()) {
            if (!this.matchesSingleBlock(reader, center, entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    default boolean matchesSlice(@Nonnull BlockGetter reader, @Nonnull BlockPos center, int yOffset) {
        if (this.getMinimumOffset().getY() > yOffset || this.getMaximumOffset().getY() < yOffset) {
            return true;
        }
        List<BlockPos> posInSlice = this.getContents().keySet().stream()
            .filter(pos -> pos.getY() == yOffset)
            .collect(Collectors.toList());
        for (BlockPos pos : posInSlice) {
            if (!this.matchesSingleBlock(reader, center, pos)) {
                return false;
            }
        }
        return true;
    }

    default boolean matchesSingleBlock(@Nonnull BlockGetter reader, @Nonnull BlockPos center, @Nonnull BlockPos centerOffset) {
        BlockPos absolute = center.offset(centerOffset);
        return matchesSingleBlock(reader, center, centerOffset, reader.getBlockState(absolute), reader.getBlockEntity(absolute));
    }

    default boolean matchesSingleBlock(@Nullable BlockGetter reader, @Nonnull BlockPos center, @Nonnull BlockPos centerOffset,
                                        @Nonnull BlockState comparing, @Nullable BlockEntity blockEntity) {
        if (!this.hasBlockAt(centerOffset)) {
            return false;
        }
        MatchableState state = this.getBlockStateAt(centerOffset);
        MatchableTile<?> tileMatch = this.getTileEntityAt(centerOffset);
        BlockPos absolute = center.offset(centerOffset);
        return state.matches(reader, absolute, comparing)
            && (blockEntity == null || tileMatch == null || ((MatchableTile) tileMatch).matches(reader, absolute, blockEntity));
    }
}
