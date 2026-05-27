package hellfirepvp.astralsorcery.common.util.collision;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.function.Consumer;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CollisionHelper
{
    public static boolean onCollision(final VoxelShape iterator, final Consumer<? super VoxelShape> action) {
        if (!CollisionManager.needsCustomCollision(iterator.field_234868_a_)) {
            return false;
        }
        final AABB box = CollisionManager.getIteratorBoundingBoxes(iterator, iterator.field_234868_a_);
        if (box == null) {
            return false;
        }
        final VoxelShape floor = VoxelShapes.func_197881_a(box);
        if (VoxelShapes.func_197879_c(floor, VoxelShapes.func_197881_a(iterator.field_234869_b_.func_186662_g(1.0E-7)), BooleanOp.field_223238_i_)) {
            action.accept(floor);
            return true;
        }
        return false;
    }
    
    @Nullable
    public static Vec3 onEntityCollision(Vec3 allowedMovement, final Entity entity) {
        if (!CollisionManager.needsCustomCollision(entity)) {
            return null;
        }
        final List<AABB> additionalBoxes = CollisionManager.getAdditionalBoundingBoxes(entity);
        final AABB entityBox = entity.func_174813_aQ().func_186662_g(1.0E-7);
        for (final AABB box : additionalBoxes) {
            final double newYMovement = VoxelShapes.func_197881_a(box).func_212430_a(Direction.Axis.Y, entityBox, allowedMovement.field_72448_b);
            allowedMovement = new Vec3(allowedMovement.field_72450_a, newYMovement, allowedMovement.field_72449_c);
        }
        return allowedMovement;
    }
}
