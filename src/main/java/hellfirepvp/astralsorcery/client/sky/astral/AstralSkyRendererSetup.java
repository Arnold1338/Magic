package hellfirepvp.astralsorcery.client.sky.astral;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Random;

class AstralSkyRendererSetup
{
    private static final Random RAND;
    
    static void generateSky(final BufferBuilder skyBuffer) {
        prepareSky(skyBuffer, 16.0f, false);
    }
    
    static void generateSkyHorizon(final BufferBuilder skyBuffer) {
        prepareSky(skyBuffer, -16.0f, true);
    }
    
    private static void prepareSky(final BufferBuilder buf, final float offsetY, final boolean flip) {
        final int scale = 64;
        final int segments = 6;
        final int width = segments * scale;
        buf.func_181668_a(7, DefaultVertexFormat.field_181705_e);
        for (int x = -width; x <= width; x += scale) {
            for (int z = -width; z <= width; z += scale) {
                float x2 = (float)x;
                float x3 = (float)(x + scale);
                if (flip) {
                    x3 = (float)x;
                    x2 = (float)(x + scale);
                }
                buf.func_225582_a_((double)x2, (double)offsetY, (double)z).func_181675_d();
                buf.func_225582_a_((double)x3, (double)offsetY, (double)z).func_181675_d();
                buf.func_225582_a_((double)x3, (double)offsetY, (double)(z + scale)).func_181675_d();
                buf.func_225582_a_((double)x2, (double)offsetY, (double)(z + scale)).func_181675_d();
            }
        }
    }
    
    static void generateStars(final BufferBuilder starBuffer, final int amount, final float sizeMultiplier) {
        starBuffer.func_181668_a(7, DefaultVertexFormat.field_181707_g);
        for (int i = 0; i < amount; ++i) {
            double x = -1.0f + AstralSkyRendererSetup.RAND.nextFloat() * 2.0f;
            double y = -1.0f + AstralSkyRendererSetup.RAND.nextFloat() * 2.0f;
            double z = -1.0f + AstralSkyRendererSetup.RAND.nextFloat() * 2.0f;
            final double ovrSize = 0.15f + AstralSkyRendererSetup.RAND.nextFloat() * 0.2f;
            double d4 = x * x + y * y + z * z;
            if (d4 < 1.0 && d4 > 0.01) {
                d4 = 1.0 / Math.sqrt(d4);
                x *= d4;
                y *= d4;
                z *= d4;
                final double d5 = x * 100.0;
                final double d6 = y * 100.0;
                final double d7 = z * 100.0;
                final double d8 = Math.atan2(x, z);
                final double d9 = Math.sin(d8);
                final double d10 = Math.cos(d8);
                final double d11 = Math.atan2(Math.sqrt(x * x + z * z), y);
                final double d12 = Math.sin(d11);
                final double d13 = Math.cos(d11);
                final double d14 = AstralSkyRendererSetup.RAND.nextDouble() * 3.141592653589793 * 2.0;
                double size = Math.sin(d14) * 2.0;
                final double d15 = Math.cos(d14);
                size *= sizeMultiplier;
                for (int j = 0; j < 4; ++j) {
                    final double d16 = ((j & 0x2) - 1) * ovrSize;
                    final double d17 = ((j + 1 & 0x2) - 1) * ovrSize;
                    final double d18 = d16 * d15 - d17 * size;
                    final double d19 = d17 * d15 + d16 * size;
                    final double d20 = d18 * d12 + 0.0 * d13;
                    final double d21 = 0.0 * d12 - d18 * d13;
                    final double d22 = d21 * d9 - d19 * d10;
                    final double d23 = d19 * d9 + d21 * d10;
                    starBuffer.func_225582_a_(d5 + d22, d6 + d20, d7 + d23).func_225583_a_((float)((j + 1 & 0x2) >> 1), (float)((j + 2 & 0x2) >> 1)).func_181675_d();
                }
            }
        }
    }
    
    static {
        RAND = new Random();
    }
}
