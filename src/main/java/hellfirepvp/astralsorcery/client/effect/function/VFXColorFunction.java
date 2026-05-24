package hellfirepvp.astralsorcery.client.effect.function;

import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.awt.Color;
import javax.annotation.Nonnull;
import java.util.Random;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public interface VFXColorFunction<T extends EntityVisualFX>
{
    public static final Random rand = new Random();
    public static final VFXColorFunction<? extends EntityVisualFX> WHITE = constant(Color.WHITE);
    public static final VFXColorFunction<? extends EntityVisualFX> BLACK = constant(Color.BLACK);
    
    @Nonnull
    Color getColor(@Nonnull final T p0, final float p1);
    
    default <T extends EntityVisualFX> VFXColorFunction<T> rainbow(final int tickSpeed) {
        final float fTickSpeed = (float)tickSpeed;
        return new VFXColorFunction<T>() {
            @Nonnull
            @Override
            public Color getColor(@Nonnull final T fx, final float pTicks) {
                return Color.getHSBColor(ClientScheduler.getClientTick() % tickSpeed / fTickSpeed, 1.0f, 1.0f);
            }
        };
    }
    
    default <T extends EntityVisualFX> VFXColorFunction<T> randomBetween(final Color c1, final Color c2) {
        final float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        final float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
        final float degree = VFXColorFunction.rand.nextFloat();
        return new VFXColorFunction<T>() {
            @Nonnull
            @Override
            public Color getColor(@Nonnull final T fx, final float pTicks) {
                final float h = RenderingVectorUtils.interpolate(hsb1[0], hsb2[0], degree);
                final float s = RenderingVectorUtils.interpolate(hsb1[1], hsb2[1], degree);
                final float b = RenderingVectorUtils.interpolate(hsb1[2], hsb2[2], degree);
                return Color.getHSBColor(h, s, b);
            }
        };
    }
    
    default <T extends EntityVisualFX> VFXColorFunction<T> random() {
        final Color c = Color.getHSBColor(VFXColorFunction.rand.nextFloat(), 1.0f, 1.0f);
        return new VFXColorFunction<T>() {
            @Nonnull
            @Override
            public Color getColor(@Nonnull final T fx, final float pTicks) {
                return c;
            }
        };
    }
    
    default <T extends EntityVisualFX> VFXColorFunction<T> constant(final Color c) {
        return new VFXColorFunction<T>() {
            @Nonnull
            @Override
            public Color getColor(@Nonnull final T fx, final float pTicks) {
                return c;
            }
        };
    }
}
