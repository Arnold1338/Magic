package hellfirepvp.astralsorcery.client.effect.function;

import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXScaleFunction<T extends EntityVisualFX>
{
    public static final VFXScaleFunction<EntityVisualFX> IDENTITY = (fx, scaleIn, pTicks) -> scaleIn;
    public static final VFXScaleFunction<EntityVisualFX> SHRINK = (fx, scaleIn, pTicks) -> {
        final float prevAge = Math.max(0.0f, fx.getAge() - 1.0f) / fx.getMaxAge();
        final float currAge = Math.max(0.0f, (float)fx.getAge()) / fx.getMaxAge();
        return scaleIn * (1.0f - RenderingVectorUtils.interpolate(prevAge, currAge, pTicks));
    };
    public static final VFXScaleFunction<EntityVisualFX> SHRINK_EXP = (fx, scaleIn, pTicks) -> MathHelper.func_76129_c(VFXScaleFunction.SHRINK.getScale(fx, scaleIn, pTicks));
    
    float getScale(@Nonnull final T p0, final float p1, final float p2);
    
    default VFXScaleFunction<T> andThen(final VFXScaleFunction<T> multiplied) {
        final VFXScaleFunction<T> existing = this;
        return (fx, scaleIn, pTicks) -> multiplied.getScale(fx, existing.getScale(fx, scaleIn, pTicks), pTicks);
    }
}
