package hellfirepvp.astralsorcery.common.util.collision;

import java.util.HashMap;
import java.util.NoSuchElementException;
import net.minecraft.core.BlockPos;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas;
import net.minecraft.world.phys.AABB;
import java.util.Map;
import net.minecraft.util.math.shapes.VoxelShapeSpliterator;
import java.util.LinkedList;
import java.util.List;

public class CollisionManager
{
    private static final List<CustomCollisionHandler> customHandlers;
    private static final int maxCacheSize = 20;
    private static final LinkedList<VoxelShapeSpliterator> accessList;
    private static final Map<VoxelShapeSpliterator, List<AABB>> instanceFlags;
    
    public static void init() {
        register(new MantleEffectAevitas.PlayerWalkableAir());
    }
    
    public static void register(final CustomCollisionHandler handler) {
        CollisionManager.customHandlers.add(handler);
    }
    
    @Nullable
    public static AABB getIteratorBoundingBoxes(final VoxelShapeSpliterator iterator, @Nullable final Entity entity) {
        if (!CollisionManager.instanceFlags.containsKey(iterator)) {
            final List<AABB> additionalBoundingBoxes = getAdditionalBoundingBoxes(entity);
            if (additionalBoundingBoxes.isEmpty()) {
                return null;
            }
            removeOldestEntry();
            CollisionManager.instanceFlags.put(iterator, additionalBoundingBoxes);
            CollisionManager.accessList.addFirst(iterator);
        }
        final List<AABB> boxes = CollisionManager.instanceFlags.get(iterator);
        if (boxes == null || boxes.isEmpty()) {
            return null;
        }
        markActive(iterator);
        return boxes.remove(0);
    }
    
    public static boolean needsCustomCollision(@Nullable final Entity entity) {
        for (final CustomCollisionHandler handler : CollisionManager.customHandlers) {
            if (handler.shouldAddCollisionFor(entity)) {
                return true;
            }
        }
        return false;
    }
    
    public static List<AABB> getAdditionalBoundingBoxes(@Nullable final Entity entity) {
        final List<AABB> additionalCollision = new ArrayList<AABB>();
        final AABB entityBox = (entity != null) ? entity.func_174813_aQ() : new AABB(BlockPos.field_177992_a);
        CollisionManager.customHandlers.stream().filter(handler -> handler.shouldAddCollisionFor(entity)).forEach(handler -> handler.addCollision(entity, entityBox, additionalCollision));
        return additionalCollision;
    }
    
    private static void removeOldestEntry() {
        if (CollisionManager.accessList.size() >= 20) {
            VoxelShapeSpliterator oldest;
            try {
                oldest = CollisionManager.accessList.removeLast();
            }
            catch (final NoSuchElementException exc) {
                if (CollisionManager.accessList.isEmpty()) {
                    return;
                }
                try {
                    oldest = CollisionManager.accessList.get(CollisionManager.accessList.size() - 1);
                }
                catch (final Exception e) {
                    return;
                }
            }
            if (oldest != null) {
                CollisionManager.instanceFlags.remove(oldest);
            }
        }
    }
    
    private static void markActive(final VoxelShapeSpliterator it) {
        if (CollisionManager.accessList.remove(it)) {
            CollisionManager.accessList.addFirst(it);
        }
    }
    
    static {
        customHandlers = new ArrayList<CustomCollisionHandler>();
        accessList = new LinkedList<VoxelShapeSpliterator>();
        instanceFlags = new HashMap<VoxelShapeSpliterator, List<AABB>>();
    }
}
