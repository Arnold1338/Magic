package hellfirepvp.astralsorcery.client.effect.function;

import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXAlphaFunction<T extends EntityVisualFX>
{
    public static final VFXAlphaFunction CONSTANT = (fx, alphaIn, pTicks) -> alphaIn;
    public static final VFXAlphaFunction FADE_OUT = (fx, alphaIn, pTicks) -> (1.0f - fx.getAge() / (float)fx.getMaxAge()) * alphaIn;
    public static final VFXAlphaFunction PYRAMID = (fx, alphaIn, pTicks) -> {
        final float halfAge = fx.getMaxAge() / 2.0f;
        return (1.0f - Math.abs(halfAge - fx.getAge()) / halfAge) * alphaIn;
    };
    
    default <T extends EntityVisualFX> VFXAlphaFunction<T> proximity(final Supplier<Vector3> targetSupplier, final float distance) {
        return (fx, alpha, pTicks) -> alpha * Mth.func_76131_a((float)fx.getRenderPosition(pTicks).distance(targetSupplier.get()) / distance, 0.0f, 1.0f);
    }
    
    float getAlpha(final T p0, final float p1, final float p2);
    
    default VFXAlphaFunction<T> andThen(final VFXAlphaFunction<T> multiplied) {
        final VFXAlphaFunction<T> existing = this;
        return (fx, alphaIn, pTicks) -> multiplied.getAlpha(fx, existing.getAlpha(fx, alphaIn, pTicks), pTicks);
    }
    
    default <T extends EntityVisualFX> VFXAlphaFunction<T> fadeIn(final float fadeInTicks) {
        return (fx, alphaIn, pTicks) -> {
            if (fx.getAgeRefreshCount() > 0) {
                return alphaIn;
            }
            else {
                final float mul = Mth.func_76131_a((fadeInTicks - (fx.getAge() + pTicks)) / fadeInTicks, 0.0f, 1.0f);
                return alphaIn * mul;
            }
        };
    }
}
