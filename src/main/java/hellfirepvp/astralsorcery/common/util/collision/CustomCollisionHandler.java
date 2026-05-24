package hellfirepvp.astralsorcery.common.util.collision;

import java.util.List;
import net.minecraft.world.level.phys.AABB;
import net.minecraft.world.level.entity.Entity;

public interface CustomCollisionHandler
{
    boolean shouldAddCollisionFor(final Entity p0);
    
    void addCollision(final Entity p0, final AABB p1, final List<AABB> p2);
}
