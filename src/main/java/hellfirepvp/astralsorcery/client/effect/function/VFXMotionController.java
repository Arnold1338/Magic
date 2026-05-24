package hellfirepvp.astralsorcery.client.effect.function;

import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXMotionController<T extends EntityVisualFX>
{
    public static final VFXMotionController<?> IDENTITY = (fx, motion) -> motion;
    
    default <T extends EntityVisualFX> VectorTarget<T> target(final Supplier<Vector3> targetSupplier) {
        return target(targetSupplier, 0.1f);
    }
    
    default <T extends EntityVisualFX> VectorTarget<T> target(final Supplier<Vector3> targetSupplier, final float velocityMultiplier) {
        return new VectorTarget<T>(targetSupplier, velocityMultiplier);
    }
    
    default <T extends EntityVisualFX> VFXMotionController<T> accelerate(final Supplier<Vector3> originalMotion) {
        return new VFXMotionController<T>() {
            @Nonnull
            @Override
            public Vector3 updateMotion(@Nonnull final T fx, @Nonnull final Vector3 motion) {
                final float perc = fx.getAge() / (float)fx.getMaxAge();
                return originalMotion.get().clone().multiply(perc);
            }
        };
    }
    
    default <T extends EntityVisualFX> VFXMotionController<T> decelerate(final Supplier<Vector3> originalMotion) {
        return new VFXMotionController<T>() {
            @Nonnull
            @Override
            public Vector3 updateMotion(@Nonnull final T fx, @Nonnull final Vector3 motion) {
                final float perc = 1.0f - fx.getAge() / (float)fx.getMaxAge();
                return originalMotion.get().clone().multiply(perc);
            }
        };
    }
    
    @Nonnull
    Vector3 updateMotion(@Nonnull final T p0, @Nonnull final Vector3 p1);
    
    public static class VectorTarget<T extends EntityVisualFX> implements VFXMotionController<T>
    {
        private final Supplier<Vector3> positionSupplier;
        private final float velocityMultiplier;
        
        protected VectorTarget(final Supplier<Vector3> positionSupplier, final float velocityMultiplier) {
            this.positionSupplier = positionSupplier;
            this.velocityMultiplier = velocityMultiplier;
        }
        
        @Nonnull
        @Override
        public Vector3 updateMotion(@Nonnull final T fx, @Nonnull final Vector3 motion) {
            final Vector3 target = this.positionSupplier.get();
            if (target == null) {
                return motion;
            }
            EntityUtils.applyVortexMotion(fx::getPosition, motion::add, target, 256.0, this.velocityMultiplier);
            return motion.multiply(0.9);
        }
    }
}
