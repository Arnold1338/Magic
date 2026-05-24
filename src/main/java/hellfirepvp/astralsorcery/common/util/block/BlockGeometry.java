package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.core.Direction;

public class BlockGeometry
{
    public static List<BlockPos> getPlane(final Direction direction, final int radius) {
        return getPlane(direction.func_176740_k(), radius);
    }
    
    public static List<BlockPos> getPlane(final Direction.Axis axis, final int radius) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final int xRadius = (axis == Direction.Axis.X) ? 0 : radius;
        final int yRadius = (axis == Direction.Axis.Y) ? 0 : radius;
        final int zRadius = (axis == Direction.Axis.Z) ? 0 : radius;
        for (int xx = -xRadius; xx <= xRadius; ++xx) {
            for (int yy = -yRadius; yy <= yRadius; ++yy) {
                for (int zz = -zRadius; zz <= zRadius; ++zz) {
                    out.add(new BlockPos(xx, yy, zz));
                }
            }
        }
        return out;
    }
    
    public static List<BlockPos> getSphere(final double radius) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final Vector3 vFrom = new Vector3(0.5, 0.5, 0.5);
        final double dst = radius * radius;
        final int toX = Mth.func_76143_f(radius);
        final int toY = Mth.func_76143_f(radius);
        final int toZ = Mth.func_76143_f(radius);
        for (int y = Mth.func_76128_c(-radius); y <= toY; ++y) {
            for (int x = Mth.func_76128_c(-radius); x <= toX; ++x) {
                for (int z = Mth.func_76128_c(-radius); z <= toZ; ++z) {
                    final Vector3 result = new Vector3(x, y, z).add(0.5, 0.5, 0.5);
                    if (result.distanceSquared(vFrom) <= dst) {
                        out.add(result.toBlockPos());
                    }
                }
            }
        }
        return out;
    }
    
    public static List<BlockPos> getHollowSphere(final double outerRadius, final double innerRadius) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final Vector3 vFrom = new Vector3(0.5, 0.5, 0.5);
        final double dstOuter = outerRadius * outerRadius;
        final double dstInner = innerRadius * innerRadius;
        final int toX = Mth.func_76143_f(outerRadius);
        final int toY = Mth.func_76143_f(outerRadius);
        final int toZ = Mth.func_76143_f(outerRadius);
        for (int x = Mth.func_76128_c(-outerRadius); x <= toX; ++x) {
            for (int y = Mth.func_76128_c(-outerRadius); y <= toY; ++y) {
                for (int z = Mth.func_76128_c(-outerRadius); z <= toZ; ++z) {
                    final Vector3 result = new Vector3(x, y, z).add(0.5, 0.5, 0.5);
                    final double dst = result.distanceSquared(vFrom);
                    if (dst > dstInner && dst <= dstOuter) {
                        out.add(result.toBlockPos());
                    }
                }
            }
        }
        return out;
    }
    
    public static List<BlockPos> getHollowDome(final double outerRadius, final double innerRadius) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final Vector3 vFrom = new Vector3(0.5, 0.5, 0.5);
        final double dstOuter = outerRadius * outerRadius;
        final double dstInner = innerRadius * innerRadius;
        final int toX = Mth.func_76143_f(outerRadius);
        final int toY = Mth.func_76143_f(outerRadius);
        final int toZ = Mth.func_76143_f(outerRadius);
        for (int x = Mth.func_76128_c(-outerRadius); x <= toX; ++x) {
            for (int z = Mth.func_76128_c(-outerRadius); z <= toZ; ++z) {
                for (int y = 0; y <= toY; ++y) {
                    final Vector3 result = new Vector3(x, y, z).add(0.5, 0.5, 0.5);
                    final double dst = result.distanceSquared(vFrom);
                    if (dst > dstInner && dst <= dstOuter) {
                        out.add(result.toBlockPos());
                    }
                }
            }
        }
        return out;
    }
    
    public static List<BlockPos> getVerticalCone(final BlockPos offset, final int flatRadius) {
        final List<BlockPos> out = new ArrayList<BlockPos>();
        final int lX = offset.getX() - flatRadius;
        final int hX = offset.getX() + flatRadius;
        final int lZ = offset.getZ() - flatRadius;
        final int hZ = offset.getZ() + flatRadius;
        for (int yy = offset.getY(); yy > 0; --yy) {
            for (int xx = lX; xx <= hX; ++xx) {
                for (int zz = lZ; zz <= hZ; ++zz) {
                    final float perc = yy / (float)offset.getY();
                    final float dstAllowed = flatRadius * perc;
                    final double dX = offset.getX() - xx;
                    final double dZ = offset.getZ() - zz;
                    final double dstCur = Math.sqrt(dX * dX + dZ * dZ);
                    if (dstCur <= dstAllowed) {
                        out.add(new BlockPos(xx, yy, zz));
                    }
                }
            }
        }
        return out;
    }
}
