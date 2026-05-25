package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import java.util.Stack;
import javax.annotation.Nonnull;
import hellfirepvp.observerlib.api.util.BlockArray;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class TreeDiscoverer
{
    @Nonnull
    public static BlockArray findTreeAt(final Level world, final BlockPos at, final boolean checkCorners) {
        return findTreeAt(world, at, checkCorners, -1);
    }
    
    @Nonnull
    public static BlockArray findTreeAt(final Level world, final BlockPos at, final boolean checkCorners, final int xzLimit) {
        final int xzLimitSq = (xzLimit == -1) ? -1 : (xzLimit * xzLimit);
        final BlockArray out = new BlockArray();
        findTree(world, at, xzLimitSq, checkCorners, out);
        return out;
    }
    
    private static void findTree(final Level world, final BlockPos at, final int xzLimitSq, final boolean checkCorners, final BlockArray out) {
        final TreeMatch foundTreeType = new TreeMatch();
        final Stack<BlockPos> discoverPositions = new Stack<BlockPos>();
        discoverPositions.push(at);
        while (!discoverPositions.isEmpty()) {
            final BlockPos offset = discoverPositions.pop();
            if (world.isEmptyBlock(offset)) {
                continue;
            }
            final BlockState foundState = world.getBlockState(offset);
            final Block foundBlock = foundState.getBlock();
            if (foundTreeType.matchLog == null) {
                if (!BlockTags.field_200031_h.func_230235_a_((Object)foundBlock)) {
                    return;
                }
                foundTreeType.matchLog = BlockPredicates.isBlock(foundBlock);
            }
            else if (foundTreeType.matchLeaf == null && BlockTags.field_206952_E.func_230235_a_((Object)foundBlock)) {
                foundTreeType.matchLeaf = BlockPredicates.isBlock(foundBlock);
            }
            boolean successful = false;
            if (foundTreeType.matchLog.test(world, offset, foundState)) {
                successful = true;
            }
            else if (foundTreeType.matchLeaf != null && foundTreeType.matchLeaf.test(world, offset, foundState)) {
                successful = true;
            }
            if (!successful) {
                continue;
            }
            out.addBlock(foundState, offset.getX(), offset.getY(), offset.getZ());
            if (checkCorners) {
                for (int xx = -1; xx <= 1; ++xx) {
                    for (int yy = -1; yy <= 1; ++yy) {
                        for (int zz = -1; zz <= 1; ++zz) {
                            final BlockPos newPos = offset.offset(xx, yy, zz);
                            if ((xzLimitSq == -1 || flatDistanceSq((Vector3i)newPos, (Vector3i)at) <= xzLimitSq) && !out.hasBlockAt(newPos)) {
                                discoverPositions.push(newPos);
                            }
                        }
                    }
                }
            }
            else {
                for (final Direction dir : Direction.values()) {
                    final BlockPos newPos2 = offset.func_177972_a(dir);
                    if ((xzLimitSq == -1 || flatDistanceSq((Vector3i)newPos2, (Vector3i)at) <= xzLimitSq) && !out.hasBlockAt(newPos2)) {
                        discoverPositions.push(newPos2);
                    }
                }
            }
        }
    }
    
    private static double flatDistanceSq(final Vector3i from, final Vector3i to) {
        final double xDiff = from.getX() - (double)to.getX();
        final double zDiff = from.getZ() - (double)to.getZ();
        return xDiff * xDiff + zDiff * zDiff;
    }
    
    private static class TreeMatch
    {
        private BlockPredicate matchLog;
        private BlockPredicate matchLeaf;
    }
}
