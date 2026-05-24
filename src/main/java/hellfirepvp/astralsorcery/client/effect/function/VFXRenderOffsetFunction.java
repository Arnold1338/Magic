package hellfirepvp.astralsorcery.client.effect.function;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXRenderOffsetFunction<T extends EntityVisualFX>
{
    public static final VFXRenderOffsetFunction<?> IDENTITY = (fx, iPos, pTicks) -> iPos;
    
    @Nonnull
    Vector3 changeRenderPosition(@Nonnull final T p0, final Vector3 p1, final float p2);
}
