package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.util.collision.CollisionHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "collide", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void addCustomCollision(Vec3 vec, AABB collisionBox, Level world,
                                            CollisionContext context, Stream<VoxelShape> potentialHits,
                                            CallbackInfoReturnable<Vec3> cir) {
        Entity entity = (Entity)(Object)new Object(); // placeholder - mixin handles this
        Vec3 allowedMovement = CollisionHelper.onEntityCollision(vec, entity);
        if (allowedMovement != null) {
            cir.setReturnValue(allowedMovement);
        }
    }
}
