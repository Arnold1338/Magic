package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.util.Mth;
import java.util.Deque;
import java.util.LinkedList;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.level.chunk.LevelChunk;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import java.util.function.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.core.Vec3i;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;

public class BlockDiscoverer
{
    public static Set<BlockPos> discoverBlocksWithSameStateAroundChain(final World world, final BlockPos origin, final BlockState match, int length, @Nullable final Direction originalBreakDirection, final BlockPredicate addCheck) {
        final Set<BlockPos> out = new HashSet<BlockPos>();
        BlockPos offset = new BlockPos((Vector3i)origin);
    Label_0019:
        while (length > 0) {
            final List<Direction> faces = new ArrayList<Direction>();
            Collections.addAll(faces, Direction.values());
            if (originalBreakDirection != null && out.isEmpty()) {
                faces.remove(originalBreakDirection);
                faces.remove(originalBreakDirection.func_176734_d());
            }
            Collections.shuffle(faces);
            for (final Direction face : faces) {
                final BlockPos at = offset.func_177972_a(face);
                if (out.contains(at)) {
                    continue;
                }
                final BlockState test = world.getBlockState(at);
                if (BlockUtils.matchStateExact(match, test) && addCheck.test(world, at, test)) {
                    out.add(at);
                    --length;
                    offset = at;
                    continue Label_0019;
                }
            }
            break;
        }
        return out;
    }
    
    public static Set<BlockPos> searchForTileEntitiesAround(final World world, final BlockPos origin, final int distance, final Predicate<BlockEntity> match) {
        final Set<BlockPos> out = new HashSet<BlockPos>();
        final int minChX = origin.getX() - distance >> 4;
        final int minChZ = origin.getZ() - distance >> 4;
        final int maxChX = origin.getX() + distance >> 4;
        final int maxChZ = origin.getZ() + distance >> 4;
        for (int chX = minChX; chX <= maxChX; ++chX) {
            for (int chZ = minChZ; chZ <= maxChZ; ++chZ) {
                final Chunk ch = world.func_212866_a_(chX, chZ);
                if (ch != null) {
                    out.addAll(ch.func_177434_r().values().stream().filter(tile -> tile.func_174877_v().func_218141_a((Vector3i)origin, (double)distance)).filter((Predicate<? super Object>)match).map((Function<? super Object, ?>)BlockEntity::func_174877_v).collect((Collector<? super Object, ?, Collection<? extends BlockPos>>)Collectors.toList()));
                }
            }
        }
        return out;
    }
    
    public static List<BlockPos> searchForBlocksAround(final World world, final BlockPos origin, final int cubeSize, final BlockPredicate match) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final BlockPos.Mutable offset = new BlockPos.Mutable();
        for (int xx = -cubeSize; xx <= cubeSize; ++xx) {
            for (int zz = -cubeSize; zz <= cubeSize; ++zz) {
                for (int yy = -cubeSize; yy <= cubeSize; ++yy) {
                    offset.func_181079_c(origin.getX() + xx, origin.getY() + yy, origin.getZ() + zz);
                    MiscUtils.executeWithChunk((IWorldReader)world, (BlockPos)offset, () -> {
                        final BlockState atState = world.getBlockState((BlockPos)offset);
                        if (match.test(world, (BlockPos)offset, atState)) {
                            out.add(new BlockPos((Vector3i)offset));
                        }
                        return;
                    });
                }
            }
        }
        return out;
    }
    
    @Nullable
    public static BlockPos searchAreaForFirst(final World world, final BlockPos center, final int radius, @Nullable final Vector3 offsetFrom, final BlockPredicate acceptor) {
        for (int r = 0; r <= radius; ++r) {
            final Set<BlockPos> posList = new HashSet<BlockPos>();
            for (int xx = -r; xx <= r; ++xx) {
                for (int yy = -r; yy <= r; ++yy) {
                    for (int zz = -r; zz <= r; ++zz) {
                        final BlockPos pos = center.offset(xx, yy, zz);
                        MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                            final BlockState state = world.getBlockState(pos);
                            if (acceptor.test(world, pos, state)) {
                                posList.add(pos);
                            }
                            return;
                        });
                    }
                }
            }
            if (!posList.isEmpty()) {
                Vector3 offset = new Vector3((Vector3i)center).add(0.5, 0.5, 0.5);
                if (offsetFrom != null) {
                    offset = offsetFrom;
                }
                BlockPos closest = null;
                double prevDst = 0.0;
                for (final BlockPos pos2 : posList) {
                    if (closest == null || offset.distance((Vector3i)pos2) < prevDst) {
                        closest = pos2;
                        prevDst = offset.distance((Vector3i)pos2);
                    }
                }
                return closest;
            }
        }
        return null;
    }
    
    public static List<BlockPos> discoverBlocksWithSameStateAround(final World world, final BlockPos origin, final boolean onlyExposed, final int cubeSize, final int limit, final boolean searchCorners) {
        return MiscUtils.executeWithChunk((IWorldReader)world, origin, () -> {
            final BlockState state = world.getBlockState(origin);
            return discoverBlocksWithSameStateAround(BlockPredicates.isState(state), world, origin, onlyExposed, cubeSize, limit, searchCorners);
        }, Lists.newArrayList());
    }
    
    public static List<BlockPos> discoverBlocksWithSameStateAround(final BlockPredicate match, final World world, final BlockPos origin, final boolean onlyExposed, final int cubeSize, final int limit, final boolean searchCorners) {
        final List<BlockPos> foundResult = new ArrayList<BlockPos>();
        foundResult.add(origin);
        final List<BlockPos> visited = new LinkedList<BlockPos>();
        Deque<BlockPos> searchNext = new LinkedList<BlockPos>();
        searchNext.addFirst(origin);
        while (!searchNext.isEmpty()) {
            final Deque<BlockPos> currentSearch = searchNext;
            searchNext = new LinkedList<BlockPos>();
            for (final BlockPos offsetPos : currentSearch) {
                if (searchCorners) {
                    for (int xx = -1; xx <= 1; ++xx) {
                        for (int yy = -1; yy <= 1; ++yy) {
                            for (int zz = -1; zz <= 1; ++zz) {
                                final BlockPos search = offsetPos.offset(xx, yy, zz);
                                if (!visited.contains(search)) {
                                    if (getCubeDistance(search, origin) <= cubeSize) {
                                        if (limit == -1 || foundResult.size() + 1 <= limit) {
                                            visited.add(search);
                                            if (!onlyExposed || isExposedToAir(world, search)) {
                                                MiscUtils.executeWithChunk((IWorldReader)world, search, searchNext, searchQueue -> {
                                                    if (match.test(world, search, world.getBlockState(search))) {
                                                        foundResult.add(search);
                                                        searchQueue.add(search);
                                                    }
                                                    return;
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    for (final Direction face : Direction.values()) {
                        final BlockPos search2 = offsetPos.func_177972_a(face);
                        if (!visited.contains(search2)) {
                            if (getCubeDistance(search2, origin) <= cubeSize) {
                                if (limit == -1 || foundResult.size() + 1 <= limit) {
                                    visited.add(search2);
                                    if (!onlyExposed || isExposedToAir(world, search2)) {
                                        final BlockPos search;
                                        MiscUtils.executeWithChunk((IWorldReader)world, search2, searchNext, searchQueue -> {
                                            if (match.test(world, search, world.getBlockState(search))) {
                                                foundResult.add(search);
                                                searchQueue.add(search);
                                            }
                                            return;
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return foundResult;
    }
    
    private static int getCubeDistance(final BlockPos p1, final BlockPos p2) {
        return (int)Mth.func_76132_a(Mth.func_76132_a((double)(p1.getX() - p2.getX()), (double)(p1.getY() - p2.getY())), (double)(p1.getZ() - p2.getZ()));
    }
    
    private static boolean isExposedToAir(final World world, final BlockPos pos) {
        for (final Direction face : Direction.values()) {
            final BlockPos offset = pos.func_177972_a(face);
            if (MiscUtils.executeWithChunk((IWorldReader)world, offset, () -> BlockUtils.isReplaceable(world, offset), false)) {
                return true;
            }
        }
        return false;
    }
}
