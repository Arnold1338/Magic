package hellfirepvp.observerlib.api.util;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.observerlib.api.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StructureUtil {
    private StructureUtil() {}

    public static boolean isStructureLoaded(Structure structure, LevelAccessor world, BlockPos offset) {
        ChunkPos min = new ChunkPos(offset.offset(structure.getMinimumOffset().getX(), structure.getMinimumOffset().getY(), structure.getMinimumOffset().getZ()));
        ChunkPos max = new ChunkPos(offset.offset(structure.getMaximumOffset().getX(), structure.getMaximumOffset().getY(), structure.getMaximumOffset().getZ()));
        for (int xx = min.x; xx <= max.x; xx++) {
            for (int zz = min.z; zz <= max.z; zz++) {
                if (!world.getChunkSource().hasChunk(xx, zz)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Optional<Integer> getLowestMismatchingSlice(MatchableStructure structure, BlockGetter world, BlockPos offset) {
        int minY = structure.getMinimumOffset().getY();
        int maxY = structure.getMaximumOffset().getY();
        for (int y = minY; y <= maxY; y++) {
            if (!structure.matchesSlice(world, offset, y)) {
                return Optional.of(y);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Set<BlockPos> getMismatches(MatchableStructure structure, BlockGetter world, BlockPos offset) {
        Set<BlockPos> result = new HashSet<>();
        structure.getContents().forEach((key, value) -> {
            if (!structure.matchesSingleBlock(world, offset, key)) {
                result.add(key);
            }
        });
        return result;
    }
}
