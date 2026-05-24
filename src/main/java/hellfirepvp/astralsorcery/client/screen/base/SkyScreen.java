package hellfirepvp.astralsorcery.client.screen.base;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.minecraft.util.Tuple;

public interface SkyScreen
{
    public static final float THRESHOLD_TO_START = 0.8f;
    public static final float THRESHOLD_TO_SHIFT_BLUEGRAD = 0.5f;
    public static final float THRESHOLD_TO_MAX_BLUEGRAD = 0.2f;
    public static final float THRESHOLD_FROM_START = 1.0f;
    public static final float THRESHOLD_FROM_SHIFT_BLUEGRAD = 0.6f;
    public static final float THRESHOLD_FROM_MAX_BLUEGRAD = 0.3f;
    
    default Tuple<Color, Color> getSkyGradient(final boolean canSeeSky, final float angleTransparency, final float partialTicks) {
        final ClientLevel renderWorld = Minecraft.getInstance().field_71441_e;
        int rgbFrom;
        int rgbTo;
        if (canSeeSky && angleTransparency > 1.0E-4) {
            final float starBr = renderWorld.func_228330_j_(partialTicks) * 2.0f;
            final float rain = renderWorld.func_72867_j(partialTicks);
            rgbFrom = RenderingUtils.clampToColorWithMultiplier(calcRGBFromWithRain(starBr, rain), angleTransparency).getRGB();
            rgbTo = RenderingUtils.clampToColorWithMultiplier(calcRGBToWithRain(starBr, rain), angleTransparency).getRGB();
        }
        else {
            rgbFrom = 0;
            rgbTo = 0;
        }
        final int alphaMask = -16777216;
        return (Tuple<Color, Color>)new Tuple((Object)new Color(alphaMask | rgbFrom, true), (Object)new Color(alphaMask | rgbTo, true));
    }
    
    default int calcRGBToWithRain(final float starBr, final float rain) {
        int to = calcRGBTo(starBr);
        if (starBr <= 0.8f) {
            float starMul = 1.0f;
            if (starBr > 0.5f) {
                starMul = 1.0f - (starBr - 0.5f) / 0.3f;
            }
            final float interpDeg = starMul * rain;
            final Color safeTo = RenderingUtils.clampToColor(to);
            final Vector3 vTo = new Vector3(safeTo.getRed(), safeTo.getGreen(), safeTo.getBlue()).divide(255.0);
            final Vector3 rainC = new Vector3(102, 114, 137).divide(255.0);
            final Vector3 interpVec = vTo.copyInterpolateWith(rainC, interpDeg);
            final Color newColor = RenderingUtils.clampToColor((int)(interpVec.getX() * 255.0), (int)(interpVec.getY() * 255.0), (int)(interpVec.getZ() * 255.0));
            to = newColor.getRGB();
        }
        return RenderingUtils.clampToColor(to).getRGB();
    }
    
    default int calcRGBTo(final float starBr) {
        if (starBr >= 0.8f) {
            return 0;
        }
        if (starBr >= 0.5f) {
            final float partSize = 0.3f;
            final float perc = 1.0f - (starBr - 0.5f) / partSize;
            return (int)(perc * 170.0f);
        }
        if (starBr >= 0.2f) {
            final float partSize = 0.3f;
            final float perc = 1.0f - (starBr - 0.2f) / partSize;
            final int green = (int)(perc * 85.0f);
            final int blue = green + 170;
            return green << 8 | blue;
        }
        final float partSize = 0.2f;
        final float perc = 1.0f - (starBr - 0.0f) / partSize;
        final int green = 85 + (int)(perc * 90.0f);
        final int red = (int)(perc * 140.0f);
        return red << 16 | green << 8 | 0xFF;
    }
    
    default int calcRGBFromWithRain(final float starBr, final float rain) {
        int to = calcRGBFrom(starBr);
        if (starBr <= 1.0f) {
            float starMul = 1.0f;
            if (starBr > 0.6f) {
                starMul = 1.0f - (starBr - 0.6f) / 0.39999998f;
            }
            final float interpDeg = starMul * rain;
            final Color safeTo = RenderingUtils.clampToColor(to);
            final Vector3 vTo = new Vector3(safeTo.getRed(), safeTo.getGreen(), safeTo.getBlue()).divide(255.0);
            final Vector3 rainC = new Vector3(102, 114, 137).divide(255.0);
            final Vector3 interpVec = vTo.copyInterpolateWith(rainC, interpDeg);
            final Color newColor = RenderingUtils.clampToColor((int)(interpVec.getX() * 255.0), (int)(interpVec.getY() * 255.0), (int)(interpVec.getZ() * 255.0));
            to = newColor.getRGB();
        }
        return RenderingUtils.clampToColor(to).getRGB();
    }
    
    default int calcRGBFrom(final float starBr) {
        if (starBr >= 1.0f) {
            return 0;
        }
        if (starBr >= 0.6f) {
            final float partSize = 0.39999998f;
            final float perc = 1.0f - (starBr - 0.6f) / partSize;
            return (int)(perc * 170.0f);
        }
        if (starBr >= 0.3f) {
            final float partSize = 0.3f;
            final float perc = 1.0f - (starBr - 0.3f) / partSize;
            final int green = (int)(perc * 85.0f);
            final int blue = green + 170;
            return green << 8 | blue;
        }
        final float partSize = 0.3f;
        final float perc = 1.0f - (starBr - 0.0f) / partSize;
        final int green = 85 + (int)(perc * 90.0f);
        final int red = (int)(perc * 140.0f);
        return red << 16 | green << 8 | 0xFF;
    }
}
