package hellfirepvp.astralsorcery.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import java.util.Iterator;
import hellfirepvp.astralsorcery.client.constellation.ConstellationBackgroundInfo;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import com.mojang.math.Matrix4f;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.constellation.ConstellationRenderInfos;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.constellation.world.ActiveCelestialsHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;

public class RenderingConstellationUtils
{
    public static void renderConstellationSky(final IConstellation c, final PoseStack renderStack, final ActiveCelestialsHandler.RenderPosition renderPos, final Supplier<Float> brightnessFn) {
        final Matrix4f matr = renderStack.func_227866_c_().func_227870_a_();
        final Vector3 renderOffset = renderPos.offset;
        final Color rC = c.getTierRenderColor();
        final int r = rC.getRed();
        final int g = rC.getGreen();
        final int b = rC.getBlue();
        final Vector3 dirU = renderPos.incU.clone().subtract(renderOffset).divide(31.0);
        final Vector3 dirV = renderPos.incV.clone().subtract(renderOffset).divide(31.0);
        final double uLength = dirU.length();
        final ConstellationBackgroundInfo backgroundInfo = ConstellationRenderInfos.getBackgroundRenderInfo(c);
        if (backgroundInfo != null) {
            backgroundInfo.getBackgroundTexture().bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
                final int bgScale = 32;
                final Vector3 ofStar = renderOffset.clone().add(dirU.clone()).add(dirV.clone());
                for (int i = 0; i < 4; ++i) {
                    final int u = (i + 1 & 0x2) >> 1;
                    final int v = (i + 2 & 0x2) >> 1;
                    final Vector3 pos = ofStar.clone().add(dirU.clone().multiply(u << 1).multiply(bgScale / 2)).add(dirV.clone().multiply(v << 1).multiply(bgScale / 2));
                    buf.func_227888_a_(matr, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()).func_225586_a_(r, g, b, MathHelper.func_76125_a((int)(brightnessFn.get() * 255.0f * 0.5), 0, 255)).func_225583_a_((float)u, (float)v).func_181675_d();
                }
                return;
            });
        }
        TexturesAS.TEX_STAR_CONNECTION.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
            for (int j = 0; j < 2; ++j) {
                c.getStarConnections().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final StarConnection con = iterator.next();
                    final Vector3 vecA = renderOffset.clone().add(dirU.clone().multiply(con.from.x + 1)).add(dirV.clone().multiply(con.from.y + 1));
                    final Vector3 vecB = renderOffset.clone().add(dirU.clone().multiply(con.to.x + 1)).add(dirV.clone().multiply(con.to.y + 1));
                    final Vector3 vecCV = vecB.subtract(vecA);
                    final Vector3 oPane = dirV.clone().crossProduct(vecCV);
                    final Vector3 vecAD = oPane.clone().crossProduct(vecCV).normalize().multiply(uLength);
                    final Vector3 offset00 = vecA.subtract(vecAD.clone().multiply((j == 0) ? 1 : -1));
                    final Vector3 vecU = vecAD.clone().multiply((j == 0) ? 2 : -2);
                    for (int k = 0; k < 4; ++k) {
                        final Vector3 pos2 = offset00.clone().add(vecU.clone().multiply((k + 1 & 0x2) >> 1)).add(vecCV.clone().multiply((k + 2 & 0x2) >> 1));
                        buf.func_227888_a_(matr, (float)pos2.getX(), (float)pos2.getY(), (float)pos2.getZ()).func_225586_a_(r, g, b, MathHelper.func_76125_a((int)(brightnessFn.get() * 255.0f), 0, 255)).func_225583_a_((float)((k + 2 & 0x2) >> 1), (float)((k + 3 & 0x2) >> 1)).func_181675_d();
                    }
                }
            }
            return;
        });
        TexturesAS.TEX_STAR_1.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
            c.getStars().iterator();
            final Iterator iterator2;
            while (iterator2.hasNext()) {
                final StarLocation star = iterator2.next();
                final int x = star.x;
                final int y = star.y;
                final Vector3 ofStar2 = renderOffset.clone().add(dirU.clone().multiply(x)).add(dirV.clone().multiply(y));
                for (int l = 0; l < 4; ++l) {
                    final int u2 = (l + 1 & 0x2) >> 1;
                    final int v2 = (l + 2 & 0x2) >> 1;
                    final Vector3 pos3 = ofStar2.clone().add(dirU.clone().multiply(u2 << 1)).add(dirV.clone().multiply(v2 << 1));
                    buf.func_227888_a_(matr, (float)pos3.getX(), (float)pos3.getY(), (float)pos3.getZ()).func_225586_a_(r, g, b, MathHelper.func_76125_a((int)(brightnessFn.get() * 255.0f), 0, 255)).func_225583_a_((float)u2, (float)v2).func_181675_d();
                }
            }
        });
    }
    
    public static void renderConstellationIntoWorldFlat(final IConstellation c, final PoseStack renderStack, final MultiBufferSource buffer, final Vector3 offset, final double scale, final double line, final float brightness) {
        renderConstellationIntoWorldFlat(c.getConstellationColor(), c, renderStack, buffer, offset, scale, line, brightness);
    }
    
    public static void renderConstellationIntoWorldFlat(final Color color, final IConstellation c, final PoseStack renderStack, final Vector3 offset, final double scale, final double line, final float brightness) {
        final MultiBufferSource.Impl drawBuffers = MultiBufferSource.func_228455_a_(Tessellator.func_178181_a().func_178180_c());
        renderConstellationIntoWorldFlat(color, c, renderStack, (MultiBufferSource)drawBuffers, offset, scale, line, brightness);
        drawBuffers.func_228461_a_();
    }
    
    public static void renderConstellationIntoWorldFlat(final Color color, final IConstellation c, final PoseStack renderStack, final MultiBufferSource buffer, final Vector3 offset, final double scale, final double line, final float brightness) {
        final Matrix4f matr = renderStack.func_227866_c_().func_227870_a_();
        final Vector3 thisOffset = offset.clone();
        final double starSize = 0.03125 * scale;
        final int r = color.getRed();
        final int g = color.getGreen();
        final int b = color.getBlue();
        final int connAlpha = (int)(brightness * 0.8f * 255.0f);
        final int starAlpha = (int)(brightness * 255.0f);
        final int outlineAlpha = (int)(brightness * 0.5f * 255.0f);
        final Vector3 drawOffset = new Vector3(-15.5 * starSize, 0.0, -15.5 * starSize);
        Vector3 dirU = new Vector3(scale, 0.0, 0.0);
        Vector3 dirV = new Vector3(0.0, 0.0, scale);
        final ConstellationBackgroundInfo backgroundInfo = ConstellationRenderInfos.getBackgroundRenderInfo(c);
        if (backgroundInfo != null) {
            final IVertexBuilder buf = buffer.getBuffer(backgroundInfo.getRenderType());
            Vector3 offsetRender = thisOffset.clone().add(0.0, 0.005, 0.0);
            offsetRender = offsetRender.add(drawOffset);
            Vector3 pos2 = offsetRender.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(1));
            pos2.drawPos(matr, buf).func_225586_a_(r, g, b, outlineAlpha).func_225583_a_(0.0f, 1.0f).func_181675_d();
            pos2 = offsetRender.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(1));
            pos2.drawPos(matr, buf).func_225586_a_(r, g, b, outlineAlpha).func_225583_a_(1.0f, 1.0f).func_181675_d();
            pos2 = offsetRender.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(0));
            pos2.drawPos(matr, buf).func_225586_a_(r, g, b, outlineAlpha).func_225583_a_(1.0f, 0.0f).func_181675_d();
            pos2 = offsetRender.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(0));
            pos2.drawPos(matr, buf).func_225586_a_(r, g, b, outlineAlpha).func_225583_a_(0.0f, 0.0f).func_181675_d();
        }
        IVertexBuilder buf = buffer.getBuffer(RenderTypesAS.CONSTELLATION_WORLD_CONNECTION);
        for (final StarConnection sc : c.getStarConnections()) {
            thisOffset.addY(0.001);
            dirU = new Vector3(sc.to.x, 0, sc.to.y).subtract(sc.from.x, 0.0, sc.from.y).multiply(starSize);
            dirV = dirU.clone().crossProduct(new Vector3(0, 1, 0)).setY(0).normalize().multiply(line * starSize);
            final Vector3 starOffset = thisOffset.clone().addX(sc.from.x * starSize).addZ(sc.from.y * starSize);
            final Vector3 offsetRender2 = starOffset.subtract(dirV.clone().divide(2.0));
            offsetRender2.add(drawOffset);
            Vector3 pos3 = offsetRender2.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(1));
            pos3.drawPos(matr, buf).func_225586_a_(r, g, b, connAlpha).func_225583_a_(1.0f, 0.0f).func_181675_d();
            pos3 = offsetRender2.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(1));
            pos3.drawPos(matr, buf).func_225586_a_(r, g, b, connAlpha).func_225583_a_(0.0f, 0.0f).func_181675_d();
            pos3 = offsetRender2.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(0));
            pos3.drawPos(matr, buf).func_225586_a_(r, g, b, connAlpha).func_225583_a_(0.0f, 1.0f).func_181675_d();
            pos3 = offsetRender2.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(0));
            pos3.drawPos(matr, buf).func_225586_a_(r, g, b, connAlpha).func_225583_a_(1.0f, 1.0f).func_181675_d();
        }
        dirU = new Vector3(starSize * 2.0, 0.0, 0.0);
        dirV = new Vector3(0.0, 0.0, starSize * 2.0);
        buf = buffer.getBuffer(RenderTypesAS.CONSTELLATION_WORLD_STAR);
        for (final StarLocation sl : c.getStars()) {
            final Vector3 offsetRender3 = thisOffset.clone().add(sl.x * starSize - starSize, 0.005, sl.y * starSize - starSize);
            offsetRender3.add(drawOffset);
            Vector3 pos4 = offsetRender3.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(1));
            pos4.drawPos(matr, buf).func_225586_a_(r, g, b, starAlpha).func_225583_a_(1.0f, 0.0f).func_181675_d();
            pos4 = offsetRender3.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(1));
            pos4.drawPos(matr, buf).func_225586_a_(r, g, b, starAlpha).func_225583_a_(0.0f, 0.0f).func_181675_d();
            pos4 = offsetRender3.clone().add(dirU.clone().multiply(1)).add(dirV.clone().multiply(0));
            pos4.drawPos(matr, buf).func_225586_a_(r, g, b, starAlpha).func_225583_a_(0.0f, 1.0f).func_181675_d();
            pos4 = offsetRender3.clone().add(dirU.clone().multiply(0)).add(dirV.clone().multiply(0));
            pos4.drawPos(matr, buf).func_225586_a_(r, g, b, starAlpha).func_225583_a_(1.0f, 1.0f).func_181675_d();
        }
    }
    
    public static Map<StarLocation, Rectangle2D.Float> renderConstellationIntoGUI(final IConstellation c, final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float width, final float height, final double linebreadth, final Supplier<Float> brightnessFn, final boolean isKnown, final boolean applyStarBrightness) {
        return renderConstellationIntoGUI(c.getTierRenderColor(), c, renderStack, offsetX, offsetY, zLevel, width, height, linebreadth, brightnessFn, isKnown, applyStarBrightness);
    }
    
    public static Map<StarLocation, Rectangle2D.Float> renderConstellationIntoGUI(final Color col, final IConstellation c, final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float width, final float height, final double linebreadth, final Supplier<Float> brightnessFn, final boolean isKnown, final boolean applyStarBrightness) {
        final Matrix4f offset = renderStack.func_227866_c_().func_227870_a_();
        final float ulength = width / 32.0f;
        final float vlength = height / 32.0f;
        final int r = col.getRed();
        final int g = col.getGreen();
        final int b = col.getBlue();
        float starBrightness = 1.0f;
        if (applyStarBrightness && Minecraft.func_71410_x().field_71441_e != null) {
            starBrightness = Minecraft.func_71410_x().field_71441_e.func_228330_j_(1.0f);
            if (starBrightness <= 0.23f) {
                return new HashMap<StarLocation, Rectangle2D.Float>();
            }
            starBrightness *= 2.0f;
        }
        final float brightness = starBrightness;
        if (isKnown) {
            final ConstellationBackgroundInfo backgroundInfo = ConstellationRenderInfos.getBackgroundRenderInfo(c);
            if (backgroundInfo != null) {
                backgroundInfo.getBackgroundTexture().bindTexture();
                RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
                    final int alpha = MathHelper.func_76125_a((int)(brightnessFn.get() * brightness * 0.5 * 255.0), 0, 255);
                    final Vector3 bgVec = new Vector3(offsetX, offsetY, zLevel);
                    for (int i = 0; i < 4; ++i) {
                        final int u = (i + 1 & 0x2) >> 1;
                        final int v = (i + 2 & 0x2) >> 1;
                        final Vector3 pos = bgVec.clone().addX(width * u).addY(height * v);
                        buf.func_227888_a_(offset, offsetX + width * u, offsetY + height * v, zLevel).func_225586_a_(r, g, b, MathHelper.func_76125_a((int)(alpha * 1.2f + 0.2f), 0, 255)).func_225583_a_((float)u, (float)v).func_181675_d();
                    }
                    return;
                });
            }
            TexturesAS.TEX_STAR_CONNECTION.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
                for (int j = 0; j < 2; ++j) {
                    c.getStarConnections().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final StarConnection sc = iterator.next();
                        final int alpha2 = MathHelper.func_76125_a((int)(brightnessFn.get() * brightness * 255.0f), 0, 255);
                        final Vector3 fromStar = new Vector3(offsetX + sc.from.x * ulength, offsetY + sc.from.y * vlength, zLevel);
                        final Vector3 toStar = new Vector3(offsetX + sc.to.x * ulength, offsetY + sc.to.y * vlength, zLevel);
                        final Vector3 dir = toStar.clone().subtract(fromStar);
                        final Vector3 degLot = dir.clone().crossProduct(new Vector3(0, 0, 1)).normalize().multiply(linebreadth);
                        final Vector3 vec00 = fromStar.clone().add(degLot);
                        final Vector3 vecV = degLot.clone().multiply(-2);
                        for (int k = 0; k < 4; ++k) {
                            final int u2 = (k + 1 & 0x2) >> 1;
                            final int v2 = (k + 2 & 0x2) >> 1;
                            final Vector3 pos2 = vec00.clone().add(dir.clone().multiply(u2)).add(vecV.clone().multiply(v2));
                            buf.func_227888_a_(offset, (float)pos2.getX(), (float)pos2.getY(), (float)pos2.getZ()).func_225586_a_(r, g, b, alpha2).func_225583_a_((float)u2, (float)v2).func_181675_d();
                        }
                    }
                }
                return;
            });
        }
        final Map<StarLocation, Rectangle2D.Float> starRectangles = new HashMap<StarLocation, Rectangle2D.Float>();
        TexturesAS.TEX_STAR_1.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
            c.getStars().iterator();
            final Iterator iterator2;
            while (iterator2.hasNext()) {
                final StarLocation sl = iterator2.next();
                final int alpha3 = MathHelper.func_76125_a((int)(brightnessFn.get() * brightness * 255.0f), 0, 255);
                final int starX = sl.x;
                final int starY = sl.y;
                final Vector3 starVec = new Vector3(starX * ulength - ulength, starY * vlength - vlength, 0.0f).add(offsetX, offsetY, zLevel);
                for (int l = 0; l < 4; ++l) {
                    final int u3 = (l + 1 & 0x2) >> 1;
                    final int v3 = (l + 2 & 0x2) >> 1;
                    final Vector3 pos3 = starVec.clone().addX(ulength * u3 * 2.0f).addY(vlength * v3 * 2.0f);
                    buf.func_227888_a_(offset, (float)pos3.getX(), (float)pos3.getY(), (float)pos3.getZ()).func_225586_a_(isKnown ? r : alpha3, isKnown ? g : alpha3, isKnown ? b : alpha3, MathHelper.func_76125_a((int)(alpha3 * 1.2f + 0.2f), 0, 255)).func_225583_a_((float)u3, (float)v3).func_181675_d();
                }
                starRectangles.put(sl, new Rectangle2D.Float((float)starVec.getX(), (float)starVec.getY(), ulength * 2.0f, vlength * 2.0f));
            }
            return;
        });
        return starRectangles;
    }
    
    public static float stdFlicker(final long wtime, final float partialTicks, final int divisor) {
        return flickerSin(wtime, partialTicks, divisor, 2.0f, 0.5f);
    }
    
    public static float conCFlicker(final long wtime, final float partialTicks, final int divisor) {
        return flickerSin(wtime, partialTicks, divisor, 4.0f, 0.375f);
    }
    
    private static float flickerSin(final long wtime, final float partialTicks, final double divisor, final float div, final float move) {
        final double rad = (wtime % ((int)GeneralConfig.CONFIG.dayLength.get() / 2) + partialTicks) / divisor;
        final float sin = MathHelper.func_76126_a((float)rad);
        return sin / div + move;
    }
}
