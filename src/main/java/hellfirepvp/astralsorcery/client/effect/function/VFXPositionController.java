package hellfirepvp.astralsorcery.client.effect.function;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXPositionController<T extends EntityVisualFX>
{
    public static final VFXPositionController<?> CONSTANT = (fx, position, motionToBeMoved) -> position.add(motionToBeMoved);
    
    @Nonnull
    Vector3 updatePosition(@Nonnull final T p0, @Nonnull final Vector3 p1, @Nonnull final Vector3 p2);
}
