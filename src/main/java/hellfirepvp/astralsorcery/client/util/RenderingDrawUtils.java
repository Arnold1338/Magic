package hellfirepvp.astralsorcery.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix3f;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.client.Camera;
import hellfirepvp.astralsorcery.client.util.draw.RenderInfo;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedList;
import net.minecraft.util.Tuple;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.lib.RenderTypesAS;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.LanguageMap;
import net.minecraft.util.FormattedCharSequence;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ITextProperties;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;

public class RenderingDrawUtils
{
    private static final Random rand;
    private static final PoseStack EMPTY;
    
    public static void renderStringCentered(@Nullable FontRenderer fr, final PoseStack renderStack, final ITextProperties text, final int x, final int y, final float scale, final int color) {
        if (fr == null) {
            fr = Minecraft.getInstance().font;
        }
        final float strLength = fr.func_238414_a_(text) * scale;
        final float offsetLeft = x - strLength;
        renderStack.popPose();
        renderStack.func_227861_a_((double)offsetLeft, (double)y, 0.0);
        renderStack.translate(scale, scale, scale);
        renderStringAt(fr, renderStack, text, color);
        renderStack.scale();
    }
    
    public static float renderString(final ITextProperties text) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, Minecraft.getInstance().font, Color.WHITE.getRGB(), false);
    }
    
    public static float renderString(final FormattedCharSequence text) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, Minecraft.getInstance().font, Color.WHITE.getRGB(), false);
    }
    
    public static float renderString(final ITextProperties text, final int color) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, Minecraft.getInstance().font, color, false);
    }
    
    public static float renderString(final FormattedCharSequence text, final int color) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, Minecraft.getInstance().font, color, false);
    }
    
    public static float renderString(@Nullable final FontRenderer fr, final ITextProperties text, final int color) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, fr, color, false);
    }
    
    public static float renderString(@Nullable final FontRenderer fr, final FormattedCharSequence text, final int color) {
        return renderStringAt(text, RenderingDrawUtils.EMPTY, fr, color, false);
    }
    
    public static float renderStringAt(@Nullable final FontRenderer fr, final PoseStack renderStack, final ITextProperties text, final int color) {
        return renderStringAt(text, renderStack, fr, color, true);
    }
    
    public static float renderStringAt(@Nullable final FontRenderer fr, final PoseStack renderStack, final FormattedCharSequence text, final int color) {
        return renderStringAt(text, renderStack, fr, color, true);
    }
    
    public static float renderStringAt(final ITextProperties text, final PoseStack renderStack, @Nullable final FontRenderer fr, final int color, final boolean dropShadow) {
        return renderStringAt(LanguageMap.func_74808_a().func_241870_a(text), renderStack, fr, color, dropShadow);
    }
    
    public static float renderStringAt(final FormattedCharSequence text, final PoseStack renderStack, @Nullable FontRenderer fr, final int color, final boolean dropShadow) {
        if (fr == null) {
            fr = Minecraft.getInstance().font;
        }
        final MultiBufferSource.Impl buffer = MultiBufferSource.func_228455_a_(Tessellator.func_178181_a().func_178180_c());
        final int length = fr.func_238416_a_(text, 0.0f, 0.0f, color, dropShadow, renderStack.last().translate(), (MultiBufferSource)buffer, false, 0, LightmapUtil.getPackedFullbrightCoords());
        buffer.func_228461_a_();
        return (float)length;
    }
    
    public static Rectangle drawInfoStar(final PoseStack renderStack, final IDrawRenderTypeBuffer buffer, final float widthHeightBase, final float pTicks) {
        final VertexConsumer vb = buffer.getBuffer(RenderTypesAS.GUI_MISC_INFO_STAR);
        final float tick = ClientScheduler.getClientTick() + pTicks;
        float deg = tick * 2.0f % 360.0f;
        float wh = widthHeightBase - widthHeightBase / 6.0f * (Mth.func_76126_a((float)Math.toRadians(tick * 4.0f % 360.0f)) + 1.0f);
        drawInfoStarSingle(renderStack, vb, wh, Math.toRadians(deg));
        deg = (tick + 22.5f) * 2.0f % 360.0f;
        wh = widthHeightBase - widthHeightBase / 6.0f * (Mth.func_76126_a((float)Math.toRadians((tick + 45.0f) * 4.0f % 360.0f)) + 1.0f);
        drawInfoStarSingle(renderStack, vb, wh, Math.toRadians(deg));
        buffer.draw(RenderTypesAS.GUI_MISC_INFO_STAR);
        return new Rectangle(Mth.func_76141_d(-widthHeightBase / 2.0f), Mth.func_76141_d(-widthHeightBase / 2.0f), Mth.func_76141_d(widthHeightBase), Mth.func_76141_d(widthHeightBase));
    }
    
    private static void drawInfoStarSingle(final PoseStack renderStack, final VertexConsumer vb, final float widthHeight, final double deg) {
        final Vector3 offset = new Vector3(-widthHeight / 2.0, -widthHeight / 2.0, 0.0).rotate(deg, Vector3.RotAxis.Z_AXIS);
        final Vector3 uv01 = new Vector3(-widthHeight / 2.0, widthHeight / 2.0, 0.0).rotate(deg, Vector3.RotAxis.Z_AXIS);
        final Vector3 uv2 = new Vector3(widthHeight / 2.0, widthHeight / 2.0, 0.0).rotate(deg, Vector3.RotAxis.Z_AXIS);
        final Vector3 uv3 = new Vector3(widthHeight / 2.0, -widthHeight / 2.0, 0.0).rotate(deg, Vector3.RotAxis.Z_AXIS);
        final Matrix4f matr = renderStack.last().translate();
        vb.func_227888_a_(matr, (float)uv01.getX(), (float)uv01.getY(), 0.0f).setPos(0.0f, 1.0f).blockPosition();
        vb.func_227888_a_(matr, (float)uv2.getX(), (float)uv2.getY(), 0.0f).setPos(1.0f, 1.0f).blockPosition();
        vb.func_227888_a_(matr, (float)uv3.getX(), (float)uv3.getY(), 0.0f).setPos(1.0f, 0.0f).blockPosition();
        vb.func_227888_a_(matr, (float)offset.getX(), (float)offset.getY(), 0.0f).setPos(0.0f, 0.0f).blockPosition();
    }
    
    public static void renderBlueTooltipComponents(final PoseStack renderStack, final float x, final float y, final float zLevel, final List<ITextProperties> tooltipData, final FontRenderer fontRenderer, final boolean isFirstLineHeadline) {
        final List<Tuple<ItemStack, ITextProperties>> stackTooltip = MapStream.ofValues((Collection<ITextProperties>)tooltipData, t -> ItemStack.EMPTY).toTupleList();
        renderBlueTooltip(renderStack, x, y, zLevel, stackTooltip, fontRenderer, isFirstLineHeadline);
    }
    
    public static void renderBlueTooltip(final PoseStack renderStack, final float x, final float y, final float zLevel, final List<Tuple<ItemStack, ITextProperties>> tooltipData, final FontRenderer fontRenderer, final boolean isFirstLineHeadline) {
        renderTooltip(renderStack, x, y, zLevel, tooltipData, fontRenderer, isFirstLineHeadline, -16777177, -16777148, Color.WHITE);
    }
    
    public static void renderTooltip(final PoseStack renderStack, float x, final float y, final float zLevel, final List<Tuple<ItemStack, ITextProperties>> tooltipData, final FontRenderer fontRenderer, final boolean isFirstLineHeadline, final int color, final int colorFade, final Color strColor) {
        final int stackBoxSize = 18;
        if (!tooltipData.isEmpty()) {
            boolean anyItemFound = false;
            int maxWidth = 0;
            for (final Tuple<ItemStack, ITextProperties> toolTip : tooltipData) {
                FontRenderer customFR = ((ItemStack)toolTip.getA()).getItem().getFontRenderer((ItemStack)toolTip.getA());
                if (customFR == null) {
                    customFR = fontRenderer;
                }
                int width = customFR.func_238414_a_((ITextProperties)toolTip.getB());
                if (!((ItemStack)toolTip.getA()).isEmpty()) {
                    anyItemFound = true;
                }
                if (anyItemFound) {
                    width += stackBoxSize;
                }
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
            if (x + 15.0f + maxWidth > Minecraft.getInstance().func_228018_at_().func_198107_o()) {
                x -= maxWidth + 24;
            }
            final int formatWidth = anyItemFound ? (maxWidth - stackBoxSize) : maxWidth;
            final List<Tuple<ItemStack, List<FormattedCharSequence>>> lengthLimitedToolTip = new LinkedList<Tuple<ItemStack, List<FormattedCharSequence>>>();
            for (final Tuple<ItemStack, ITextProperties> toolTip2 : tooltipData) {
                FontRenderer customFR2 = ((ItemStack)toolTip2.getA()).getItem().getFontRenderer((ItemStack)toolTip2.getA());
                if (customFR2 == null) {
                    customFR2 = fontRenderer;
                }
                List<FormattedCharSequence> textLines = customFR2.func_238425_b_((ITextProperties)toolTip2.getB(), formatWidth);
                if (textLines.isEmpty()) {
                    textLines = Collections.singletonList(FormattedCharSequence.field_242232_a);
                }
                lengthLimitedToolTip.add((Tuple<ItemStack, List<FormattedCharSequence>>)new Tuple(toolTip2.getA(), (Object)textLines));
            }
            final float pX = x + 12.0f;
            final float pY = y - 12.0f;
            int sumLineHeight = 0;
            if (!lengthLimitedToolTip.isEmpty()) {
                if (lengthLimitedToolTip.size() > 1 && isFirstLineHeadline) {
                    sumLineHeight += 2;
                }
                final Iterator<Tuple<ItemStack, List<FormattedCharSequence>>> iterator = lengthLimitedToolTip.iterator();
                while (iterator.hasNext()) {
                    final Tuple<ItemStack, List<FormattedCharSequence>> toolTip3 = iterator.next();
                    int segmentHeight = 0;
                    if (!((ItemStack)toolTip3.getA()).isEmpty()) {
                        segmentHeight += 2;
                        segmentHeight += stackBoxSize;
                        segmentHeight += Math.max(((List)toolTip3.getB()).size() - 1, 0) * 10;
                    }
                    else {
                        segmentHeight += ((List)toolTip3.getB()).size() * 10;
                    }
                    if (!iterator.hasNext()) {
                        segmentHeight -= 2;
                    }
                    sumLineHeight += segmentHeight;
                }
            }
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY - 4.0f, pX + maxWidth + 3.0f, pY - 3.0f, color, colorFade);
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY + sumLineHeight + 3.0f, pX + maxWidth + 3.0f, pY + sumLineHeight + 4.0f, color, colorFade);
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY - 3.0f, pX + maxWidth + 3.0f, pY + sumLineHeight + 3.0f, color, colorFade);
            drawGradientRect(renderStack, zLevel, pX - 4.0f, pY - 3.0f, pX - 3.0f, pY + sumLineHeight + 3.0f, color, colorFade);
            drawGradientRect(renderStack, zLevel, pX + maxWidth + 3.0f, pY - 3.0f, pX + maxWidth + 4.0f, pY + sumLineHeight + 3.0f, color, colorFade);
            final int col = (color & 0xFFFFFF) | (color & 0xFF000000);
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY - 3.0f + 1.0f, pX - 3.0f + 1.0f, pY + sumLineHeight + 3.0f - 1.0f, color, col);
            drawGradientRect(renderStack, zLevel, pX + maxWidth + 2.0f, pY - 3.0f + 1.0f, pX + maxWidth + 3.0f, pY + sumLineHeight + 3.0f - 1.0f, color, col);
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY - 3.0f, pX + maxWidth + 3.0f, pY - 3.0f + 1.0f, col, col);
            drawGradientRect(renderStack, zLevel, pX - 3.0f, pY + sumLineHeight + 2.0f, pX + maxWidth + 3.0f, pY + sumLineHeight + 3.0f, color, color);
            final int offset = anyItemFound ? stackBoxSize : 0;
            renderStack.popPose();
            renderStack.func_227861_a_((double)pX, (double)pY, 0.0);
            boolean first = true;
            for (final Tuple<ItemStack, List<FormattedCharSequence>> toolTip4 : lengthLimitedToolTip) {
                int minYShift = 10;
                if (!((ItemStack)toolTip4.getA()).isEmpty()) {
                    renderStack.popPose();
                    renderStack.func_227861_a_(0.0, 0.0, (double)zLevel);
                    RenderingUtils.renderItemStackGUI(renderStack, (ItemStack)toolTip4.getA(), null);
                    renderStack.scale();
                    minYShift = stackBoxSize;
                    renderStack.func_227861_a_(0.0, 2.0, 0.0);
                }
                for (final FormattedCharSequence text : (List)toolTip4.getB()) {
                    FontRenderer customFR3 = ((ItemStack)toolTip4.getA()).getItem().getFontRenderer((ItemStack)toolTip4.getA());
                    if (customFR3 == null) {
                        customFR3 = fontRenderer;
                    }
                    renderStack.popPose();
                    renderStack.func_227861_a_((double)offset, 0.0, (double)zLevel);
                    renderStringAt(text, renderStack, customFR3, strColor.getRGB(), false);
                    renderStack.scale();
                    renderStack.func_227861_a_(0.0, 10.0, 0.0);
                    minYShift -= 10;
                }
                if (minYShift > 0) {
                    renderStack.func_227861_a_(0.0, (double)minYShift, 0.0);
                }
                if (isFirstLineHeadline && first) {
                    renderStack.func_227861_a_(0.0, 2.0, 0.0);
                }
                first = false;
            }
            renderStack.scale();
        }
    }
    
    public static void renderBlueTooltipBox(final PoseStack renderStack, final int x, final int y, final int width, final int height) {
        renderTooltipBox(renderStack, x, y, width, height, 39, 68);
    }
    
    public static void renderTooltipBox(final PoseStack renderStack, final int x, final int y, final int width, final int height, final int color, final int colorFade) {
        final int pX = x + 12;
        final int pY = y - 12;
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY - 4), (float)(pX + width + 3), (float)(pY - 3), color, colorFade);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY + height + 3), (float)(pX + width + 3), (float)(pY + height + 4), color, colorFade);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY - 3), (float)(pX + width + 3), (float)(pY + height + 3), color, colorFade);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 4), (float)(pY - 3), (float)(pX - 3), (float)(pY + height + 3), color, colorFade);
        drawGradientRect(renderStack, 0.0f, (float)(pX + width + 3), (float)(pY - 3), (float)(pX + width + 4), (float)(pY + height + 3), color, colorFade);
        final int col = (color & 0xFFFFFF) | (color & 0xFF000000);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY - 3 + 1), (float)(pX - 3 + 1), (float)(pY + height + 3 - 1), color, col);
        drawGradientRect(renderStack, 0.0f, (float)(pX + width + 2), (float)(pY - 3 + 1), (float)(pX + width + 3), (float)(pY + height + 3 - 1), color, col);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY - 3), (float)(pX + width + 3), (float)(pY - 3 + 1), col, col);
        drawGradientRect(renderStack, 0.0f, (float)(pX - 3), (float)(pY + height + 2), (float)(pX + width + 3), (float)(pY + height + 3), color, color);
    }
    
    public static void drawGradientRect(final PoseStack renderStack, final float zLevel, final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float startAlpha = (startColor >> 24 & 0xFF) / 255.0f;
        final float startRed = (startColor >> 16 & 0xFF) / 255.0f;
        final float startGreen = (startColor >> 8 & 0xFF) / 255.0f;
        final float startBlue = (startColor & 0xFF) / 255.0f;
        final float endAlpha = (endColor >> 24 & 0xFF) / 255.0f;
        final float endRed = (endColor >> 16 & 0xFF) / 255.0f;
        final float endGreen = (endColor >> 8 & 0xFF) / 255.0f;
        final float endBlue = (endColor & 0xFF) / 255.0f;
        RenderSystem.disableTexture();
        Blending.DEFAULT.apply();
        RenderSystem.shadeModel(7425);
        RenderingUtils.draw(7, DefaultVertexFormat.field_181706_f, buf -> {
            final Matrix4f offset = renderStack.last().translate();
            buf.func_227888_a_(offset, right, top, zLevel).pushPose()startRed, startGreen, startBlue, startAlpha).blockPosition();
            buf.func_227888_a_(offset, left, top, zLevel).pushPose()startRed, startGreen, startBlue, startAlpha).blockPosition();
            buf.func_227888_a_(offset, left, bottom, zLevel).pushPose()endRed, endGreen, endBlue, endAlpha).blockPosition();
            buf.func_227888_a_(offset, right, bottom, zLevel).pushPose()endRed, endGreen, endBlue, endAlpha).blockPosition();
            return;
        });
        RenderSystem.shadeModel(7424);
        RenderSystem.enableTexture();
    }
    
    public static void renderLightRayFan(final PoseStack renderStack, final MultiBufferSource buffer, final Color color, final long seed, final int minScale, final float scale, final int count) {
        RenderingDrawUtils.rand.setSeed(seed);
        final float f1 = ClientScheduler.getClientTick() / 400.0f;
        final float f2 = 0.0f;
        final int alpha = (int)(255.0f * (1.0f - f2));
        final VertexConsumer vb = buffer.getBuffer(RenderTypesAS.EFFECT_LIGHTRAY_FAN);
        renderStack.popPose();
        for (int i = 0; i < count; ++i) {
            renderStack.popPose();
            renderStack.mulPose(Vector3f.field_229179_b_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f));
            renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f));
            renderStack.mulPose(Vector3f.field_229183_f_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f));
            renderStack.mulPose(Vector3f.field_229179_b_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f));
            renderStack.mulPose(Vector3f.field_229181_d_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f));
            renderStack.mulPose(Vector3f.field_229183_f_.getMultiBufferSource()RenderingDrawUtils.rand.nextFloat() * 360.0f + f1 * 360.0f));
            final Matrix4f matr = renderStack.last().translate();
            float fa = RenderingDrawUtils.rand.nextFloat() * 20.0f + 5.0f + f2 * 10.0f;
            float f3 = RenderingDrawUtils.rand.nextFloat() * 2.0f + 1.0f + f2 * 2.0f;
            fa /= 30.0f / (Math.min((float)minScale, 10.0f * scale) / 10.0f);
            f3 /= 30.0f / (Math.min((float)minScale, 10.0f * scale) / 10.0f);
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, -0.7f * f3, fa, -0.5f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            vb.func_227888_a_(matr, 0.7f * f3, fa, -0.5f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, 0.7f * f3, fa, -0.5f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            vb.func_227888_a_(matr, 0.0f, fa, 1.0f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, 0.0f, 0.0f, 0.0f).setPos(color.getRed(), color.getGreen(), color.getBlue(), alpha).blockPosition();
            vb.func_227888_a_(matr, 0.0f, fa, 1.0f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            vb.func_227888_a_(matr, -0.7f * f3, fa, -0.5f * f3).setPos(color.getRed(), color.getGreen(), color.getBlue(), 0).blockPosition();
            renderStack.scale();
        }
        renderStack.scale();
        RenderingUtils.refreshDrawing(vb, RenderTypesAS.EFFECT_LIGHTRAY_FAN);
    }
    
    public static void renderFacingFullQuadVB(final VertexConsumer vb, final PoseStack renderStack, final double px, final double py, final double pz, final float scale, final float angle, final int r, final int g, final int b, final int alpha) {
        renderFacingQuadVB(vb, renderStack, px, py, pz, scale, angle, 0.0f, 0.0f, 1.0f, 1.0f, r, g, b, alpha);
    }
    
    public static void renderFacingSpriteVB(final VertexConsumer vb, final PoseStack renderStack, final double px, final double py, final double pz, final float scale, final float angle, final SpriteSheetResource sprite, final long spriteTick, final int r, final int g, final int b, final int alpha) {
        final Tuple<Float, Float> uv = sprite.getUVOffset(spriteTick);
        renderFacingQuadVB(vb, renderStack, px, py, pz, scale, angle, (float)uv.getA(), (float)uv.getB(), sprite.getULength(), sprite.getVLength(), r, g, b, alpha);
    }
    
    public static void renderFacingQuadVB(final VertexConsumer vb, final PoseStack renderStack, final double px, final double py, final double pz, final float scale, final float angle, final float u, final float v, final float uLength, final float vLength, final int r, final int g, final int b, final int alpha) {
        final Vector3 pos = new Vector3(px, py, pz);
        final RenderInfo ri = RenderInfo.getInstance();
        final Camera ari = ri.getARI();
        final float arX = ri.getRotationX();
        final float arZ = ri.getRotationZ();
        final float arYZ = ri.getRotationYZ();
        final float arXY = ri.getRotationXY();
        final float arXZ = ri.getRotationXZ();
        final Vec3 view = ari.func_216785_c();
        final Vector3f look = ari.func_227996_l_();
        final Vector3 iPos = new Vector3(view);
        Vector3 v2 = new Vector3(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
        Vector3 v3 = new Vector3(-arX * scale + arYZ * scale, arXZ * scale, -arZ * scale + arXY * scale);
        Vector3 v4 = new Vector3(arX * scale + arYZ * scale, arXZ * scale, arZ * scale + arXY * scale);
        Vector3 v5 = new Vector3(arX * scale - arYZ * scale, -arXZ * scale, arZ * scale - arXY * scale);
        if (angle != 0.0f) {
            final float cAngle = Mth.func_76134_b(angle * 0.5f);
            final float cAngleSq = cAngle * cAngle;
            final Vector3 vAngle = new Vector3(Mth.func_76126_a(angle * 0.5f) * look.func_195899_a(), Mth.func_76126_a(angle * 0.5f) * look.func_195900_b(), Mth.func_76126_a(angle * 0.5f) * look.func_195902_c());
            v2 = vAngle.clone().multiply(2.0 * v2.dot(vAngle)).add(v2.clone().multiply(cAngleSq - vAngle.dot(vAngle))).add(vAngle.clone().crossProduct(v2.clone().multiply(2.0f * cAngle)));
            v3 = vAngle.clone().multiply(2.0 * v3.dot(vAngle)).add(v3.clone().multiply(cAngleSq - vAngle.dot(vAngle))).add(vAngle.clone().crossProduct(v3.clone().multiply(2.0f * cAngle)));
            v4 = vAngle.clone().multiply(2.0 * v4.dot(vAngle)).add(v4.clone().multiply(cAngleSq - vAngle.dot(vAngle))).add(vAngle.clone().crossProduct(v4.clone().multiply(2.0f * cAngle)));
            v5 = vAngle.clone().multiply(2.0 * v5.dot(vAngle)).add(v5.clone().multiply(cAngleSq - vAngle.dot(vAngle))).add(vAngle.clone().crossProduct(v5.clone().multiply(2.0f * cAngle)));
        }
        final Matrix4f matr = renderStack.last().translate();
        pos.clone().add(v2).subtract(iPos).drawPos(matr, vb).setPos(r, g, b, alpha).setPos(u + uLength, v + vLength).blockPosition();
        pos.clone().add(v3).subtract(iPos).drawPos(matr, vb).setPos(r, g, b, alpha).setPos(u + uLength, v).blockPosition();
        pos.clone().add(v4).subtract(iPos).drawPos(matr, vb).setPos(r, g, b, alpha).setPos(u, v).blockPosition();
        pos.clone().add(v5).subtract(iPos).drawPos(matr, vb).setPos(r, g, b, alpha).setPos(u, v + vLength).blockPosition();
    }
    
    public static void renderTexturedCubeCentralColorLighted(final VertexConsumer buf, final PoseStack renderStack, final float u, final float v, final float uLength, final float vLength, final int r, final int g, final int b, final int a, final int combinedLight) {
        final Matrix4f matr = renderStack.last().translate();
        buf.func_227888_a_(matr, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227886_a_(combinedLight).blockPosition();
        buf.func_227888_a_(matr, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227886_a_(combinedLight).blockPosition();
    }
    
    public static void renderTexturedCubeCentralColorNormal(final PoseStack renderStack, final VertexConsumer vb, final float u, final float v, final float uLength, final float vLength, final int r, final int g, final int b, final int a, final Matrix3f normalMatr) {
        final Matrix4f offset = renderStack.last().translate();
        vb.func_227888_a_(offset, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, -0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, -0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, -0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, 0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u + uLength, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
        vb.func_227888_a_(offset, -0.5f, 0.5f, 0.5f).setPos(r, g, b, a).setPos(u, v + vLength).func_227887_a_(normalMatr, 0.0f, 0.0f, 0.0f).blockPosition();
    }
    
    public static void renderAngleRotatedTexturedRectVB(final VertexConsumer vb, final PoseStack renderStack, final Vector3 renderOffset, final Vector3 axis, final float angleRad, final float scale, final float u, final float v, final float uLength, final float vLength, final int r, final int g, final int b, final int a) {
        final Vector3 renderStart = axis.clone().perpendicular().rotate(angleRad, axis).normalize();
        final Matrix4f matr = renderStack.last().translate();
        Vector3 vec = renderStart.clone().rotate(Math.toRadians(90.0), axis).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(matr, vb).setPos(r, g, b, a).setPos(u, v + vLength).blockPosition();
        vec = renderStart.clone().multiply(-1).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(matr, vb).setPos(r, g, b, a).setPos(u + uLength, v + vLength).blockPosition();
        vec = renderStart.clone().rotate(Math.toRadians(270.0), axis).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(matr, vb).setPos(r, g, b, a).setPos(u + uLength, v).blockPosition();
        vec = renderStart.clone().normalize().multiply(scale).add(renderOffset);
        vec.drawPos(matr, vb).setPos(r, g, b, a).setPos(u, v).blockPosition();
    }
    
    static {
        rand = new Random();
        EMPTY = new PoseStack();
    }
}
