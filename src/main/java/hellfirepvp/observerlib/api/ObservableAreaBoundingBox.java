package hellfirepvp.observerlib.api;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

import java.util.Collection;

public class ObservableAreaBoundingBox implements ObservableArea {
    private final AABB box;

    public ObservableAreaBoundingBox(Vec3i min, Vec3i max) {
        this(new AABB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ()));
    }

    public ObservableAreaBoundingBox(AABB boundingBox) {
        this.box = boundingBox;
    }

    @Override
    public Collection<ChunkPos> getAffectedChunks(Vec3i offset) {
        return this.calculateAffectedChunks(this.box, offset);
    }

    @Override
    public boolean observes(Vec3i relativePos) {
        int x = relativePos.getX();
        int y = relativePos.getY();
        int z = relativePos.getZ();
        return x >= this.box.minX && x <= this.box.maxX
            && y >= this.box.minY && y <= this.box.maxY
            && z >= this.box.minZ && z <= this.box.maxZ;
    }
}
