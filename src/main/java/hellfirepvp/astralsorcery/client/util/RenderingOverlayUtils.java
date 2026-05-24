package hellfirepvp.astralsorcery.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import net.minecraft.util.text.ITextProperties;
import java.util.Iterator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Tuple;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;

public class RenderingOverlayUtils
{
    public static void renderDefaultItemDisplay(final PoseStack renderStack, final List<Tuple<ItemStack, Integer>> itemStacks) {
        final int heightNormal = 26;
        final int heightSplit = 13;
        final int width = 26;
        final int offsetX = 30;
        final int offsetY = 15;
        final ItemRenderer itemRender = Minecraft.func_71410_x().func_175599_af();
        final FontRenderer fontRenderer = Minecraft.func_71410_x().field_71466_p;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int tempY = offsetY;
        for (int i = 0; i < itemStacks.size(); ++i) {
            final boolean first = i == 0;
            final boolean last = i + 1 == itemStacks.size();
            final float currentY = (float)tempY;
            if (first) {
                TexturesAS.TEX_OVERLAY_ITEM_FRAME.bindTexture();
                RenderingUtils.draw(7, DefaultVertexFormats.field_181707_g, buf -> {
                    final Matrix4f offset = renderStack.func_227866_c_().func_227870_a_();
                    buf.func_227888_a_(offset, (float)offsetX, currentY + heightSplit, 10.0f).func_225583_a_(0.0f, 0.5f).func_181675_d();
                    buf.func_227888_a_(offset, (float)(offsetX + width), currentY + heightSplit, 10.0f).func_225583_a_(1.0f, 0.5f).func_181675_d();
                    buf.func_227888_a_(offset, (float)(offsetX + width), currentY, 10.0f).func_225583_a_(1.0f, 0.0f).func_181675_d();
                    buf.func_227888_a_(offset, (float)offsetX, currentY, 10.0f).func_225583_a_(0.0f, 0.0f).func_181675_d();
                    return;
                });
                tempY += heightSplit;
            }
            else {
                TexturesAS.TEX_OVERLAY_ITEM_FRAME_EXTENSION.bindTexture();
                RenderingUtils.draw(7, DefaultVertexFormats.field_181707_g, buf -> {
                    final Matrix4f offset2 = renderStack.func_227866_c_().func_227870_a_();
                    buf.func_227888_a_(offset2, (float)offsetX, currentY + heightNormal, 10.0f).func_225583_a_(0.0f, 1.0f).func_181675_d();
                    buf.func_227888_a_(offset2, (float)(offsetX + width), currentY + heightNormal, 10.0f).func_225583_a_(1.0f, 1.0f).func_181675_d();
                    buf.func_227888_a_(offset2, (float)(offsetX + width), currentY, 10.0f).func_225583_a_(1.0f, 0.0f).func_181675_d();
                    buf.func_227888_a_(offset2, (float)offsetX, currentY, 10.0f).func_225583_a_(0.0f, 0.0f).func_181675_d();
                    return;
                });
                tempY += heightNormal;
            }
            if (last) {
                final float drawY = (float)tempY;
                TexturesAS.TEX_OVERLAY_ITEM_FRAME.bindTexture();
                RenderingUtils.draw(7, DefaultVertexFormats.field_181707_g, buf -> {
                    final Matrix4f offset3 = renderStack.func_227866_c_().func_227870_a_();
                    buf.func_227888_a_(offset3, (float)offsetX, drawY + heightSplit, 10.0f).func_225583_a_(0.0f, 1.0f).func_181675_d();
                    buf.func_227888_a_(offset3, (float)(offsetX + width), drawY + heightSplit, 10.0f).func_225583_a_(1.0f, 1.0f).func_181675_d();
                    buf.func_227888_a_(offset3, (float)(offsetX + width), drawY, 10.0f).func_225583_a_(1.0f, 0.5f).func_181675_d();
                    buf.func_227888_a_(offset3, (float)offsetX, drawY, 10.0f).func_225583_a_(0.0f, 0.5f).func_181675_d();
                    return;
                });
                tempY += heightSplit;
            }
        }
        RenderSystem.disableBlend();
        BlockAtlasTexture.getInstance().bindTexture();
        tempY = offsetY;
        for (final Tuple<ItemStack, Integer> stackTpl : itemStacks) {
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(offsetX + 5), (double)(tempY + 5), 0.0);
            RenderingUtils.renderItemStackGUI(renderStack, (ItemStack)stackTpl.func_76341_a(), null);
            renderStack.func_227865_b_();
            tempY += heightNormal;
        }
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(offsetX + 14), (double)(offsetY + 16), 0.0);
        final int txtColor = 14540253;
        for (final Tuple<ItemStack, Integer> stackTpl2 : itemStacks) {
            final ItemStack stack = (ItemStack)stackTpl2.func_76341_a();
            FontRenderer fr;
            if ((fr = stack.getItem().getFontRenderer(stack)) == null) {
                fr = fontRenderer;
            }
            String amountStr = String.valueOf(stackTpl2.func_76340_b());
            if ((int)stackTpl2.func_76340_b() == -1) {
                amountStr = "\u221e";
            }
            final ITextProperties prop = (ITextProperties)new Component(amountStr);
            final int length = fontRenderer.func_238414_a_(prop);
            renderStack.func_227860_a_();
            renderStack.func_227861_a_((double)(-length / 3.0f), 0.0, 500.0);
            renderStack.func_227862_a_(0.7f, 0.7f, 1.0f);
            if (amountStr.length() > 3) {
                renderStack.func_227862_a_(0.9f, 0.9f, 1.0f);
            }
            RenderingDrawUtils.renderStringAt(fr, renderStack, prop, txtColor);
            renderStack.func_227865_b_();
            renderStack.func_227861_a_(0.0, (double)heightNormal, 0.0);
        }
        renderStack.func_227865_b_();
    }
}
