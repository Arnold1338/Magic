package hellfirepvp.astralsorcery.common.util.block;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.data.BiDiPair;
import net.minecraft.core.Vec3i;
import java.util.HashSet;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class BlockSymmetryHelper
{
    public SymmetryResult getDotSymmetry(final IBlockReader world, final BlockPos center, final int radiusLayer, final boolean allowMirrorSymmetry, final Predicate<BlockState> applicableStateFilter) {
        final List<BlockPos> layerPositions = BlockGeometry.getHollowSphere(radiusLayer + 1, radiusLayer);
        final SymmetryResult result = new SymmetryResult(layerPositions.size());
        final Set<BlockPos> visitedBlocks = new HashSet<BlockPos>();
        for (final BlockPos offset : layerPositions) {
            final BlockPos at = center.func_177971_a((Vector3i)offset);
            if (visitedBlocks.contains(at)) {
                continue;
            }
            visitedBlocks.add(at);
            final BlockState state = world.getBlockState(at);
            if (offset.getX() == 0 || offset.getY() == 0 || offset.getZ() == 0) {
                if (state.isAir(world, at)) {
                    continue;
                }
                result.fillerBlocks.add(at);
            }
            else if (applicableStateFilter.test(state)) {
                final BlockPos dotSym = center.func_177973_b((Vector3i)offset);
                final BlockState dotState = world.getBlockState(dotSym);
                if (applicableStateFilter.test(dotState)) {
                    result.symmetryPairs.add(new BiDiPair(at, dotSym));
                    if (!allowMirrorSymmetry) {
                        checkMirrorSymmetry(world, new Vector3i(-offset.getX(), offset.getY(), offset.getZ()), center, result, visitedBlocks);
                        checkMirrorSymmetry(world, new Vector3i(offset.getX(), -offset.getY(), offset.getZ()), center, result, visitedBlocks);
                        checkMirrorSymmetry(world, new Vector3i(offset.getX(), offset.getY(), -offset.getZ()), center, result, visitedBlocks);
                    }
                }
                else if (!dotState.isAir(world, dotSym)) {
                    result.fillerBlocks.add(at);
                    result.fillerBlocks.add(dotSym);
                }
                visitedBlocks.add(dotSym);
            }
            else {
                if (state.isAir(world, at)) {
                    continue;
                }
                result.fillerBlocks.add(at);
            }
        }
        result.density = (result.fillerBlocks.size() + result.symmetryPairs.size() * 2.0f) / result.totalCount;
        return result;
    }
    
    private static void checkMirrorSymmetry(final IBlockReader world, final Vector3i offset, final BlockPos center, final SymmetryResult result, final Set<BlockPos> visitedBlocks) {
        final BlockPos at = center.func_177971_a(offset);
        final BlockState state = world.getBlockState(at);
        visitedBlocks.add(at);
        if (!state.isAir(world, at)) {
            result.fillerBlocks.add(at);
        }
        final BlockPos dotSym = center.func_177973_b(offset);
        final BlockState dotState = world.getBlockState(dotSym);
        visitedBlocks.add(dotSym);
        if (!dotState.isAir(world, dotSym)) {
            result.fillerBlocks.add(at);
        }
    }
    
    public static class SymmetryResult
    {
        private final int totalCount;
        private float density;
        private final Set<BiDiPair<BlockPos, BlockPos>> symmetryPairs;
        private final Set<BlockPos> fillerBlocks;
        
        private SymmetryResult(final int totalCount) {
            this.density = 0.0f;
            this.symmetryPairs = new HashSet<BiDiPair<BlockPos, BlockPos>>();
            this.fillerBlocks = new HashSet<BlockPos>();
            this.totalCount = totalCount;
        }
        
        public float getDensity() {
            return this.density;
        }
        
        public Set<BiDiPair<BlockPos, BlockPos>> getSymmetryPairs() {
            return this.symmetryPairs;
        }
        
        public Set<BlockPos> getFillerBlocks() {
            return this.fillerBlocks;
        }
    }
}
