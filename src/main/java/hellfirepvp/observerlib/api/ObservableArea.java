package hellfirepvp.observerlib.api;

import com.google.common.collect.Lists;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

public interface ObservableArea {
    Collection<ChunkPos> getAffectedChunks(Vec3i offset);

    boolean observes(Vec3i relativePos);

    default Collection<ChunkPos> calculateAffectedChunks(AABB box, Vec3i offset) {
        return this.calculateAffectedChunks(
            new Vec3i((int) Math.round(box.minX + offset.getX()), (int) Math.round(box.minY + offset.getY()), (int) Math.round(box.minZ + offset.getZ())),
            new Vec3i((int) Math.round(box.maxX + offset.getX()), (int) Math.round(box.maxY + offset.getY()), (int) Math.round(box.maxZ + offset.getZ()))
        );
    }

    default Collection<ChunkPos> calculateAffectedChunks(Vec3i min, Vec3i max) {
        List<ChunkPos> affected = Lists.newArrayList();
        int maxX = max.getX() >> 4;
        int maxZ = max.getZ() >> 4;
        for (int chX = min.getX() >> 4; chX <= maxX; chX++) {
            for (int chZ = min.getZ() >> 4; chZ <= maxZ; chZ++) {
                affected.add(new ChunkPos(chX, chZ));
            }
        }
        return affected;
    }
}
