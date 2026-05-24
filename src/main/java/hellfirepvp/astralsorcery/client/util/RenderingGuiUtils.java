package hellfirepvp.astralsorcery.client.util;

import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.awt.Color;
import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

public class RenderingGuiUtils
{
    private static final PoseStack EMPTY;
    
    @Deprecated
    public static void drawTexturedRectAtCurrentPos(final float width, final float height, final float zLevel, final float uFrom, final float vFrom, final float uWidth, final float vWidth) {
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> rect((VertexConsumer)buf, 0.0f, 0.0f, zLevel, width, height).tex(uFrom, vFrom, uWidth, vWidth).draw());
    }
    
    @Deprecated
    public static void drawTexturedRectAtCurrentPos(final float width, final float height, final float zLevel) {
        drawTexturedRectAtCurrentPos(width, height, zLevel, 0.0f, 0.0f, 1.0f, 1.0f);
    }
    
    @Deprecated
    public static void drawRect(final float offsetX, final float offsetY, final float zLevel, final float width, final float height) {
        drawRect(new PoseStack(), offsetX, offsetY, zLevel, width, height);
    }
    
    public static void drawRect(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float width, final float height) {
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> rect((VertexConsumer)buf, renderStack, offsetX, offsetY, zLevel, width, height).draw());
    }
    
    public static void drawTexturedRect(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float width, final float height, final AbstractRenderableTexture tex) {
        final Tuple<Float, Float> uv = tex.getUVOffset();
        drawTexturedRect(renderStack, offsetX, offsetY, zLevel, width, height, (float)uv.func_76341_a(), (float)uv.func_76340_b(), tex.getUWidth(), tex.getVWidth());
    }
    
    @Deprecated
    public static void drawTexturedRect(final float offsetX, final float offsetY, final float zLevel, final float width, final float height, final float uFrom, final float vFrom, final float uWidth, final float vWidth) {
        drawTexturedRect(RenderingGuiUtils.EMPTY, offsetX, offsetY, zLevel, width, height, uFrom, vFrom, uWidth, vWidth);
    }
    
    public static void drawTexturedRect(final PoseStack renderStack, final float width, final float height, final float uFrom, final float vFrom, final float uWidth, final float vWidth) {
        drawTexturedRect(renderStack, 0.0f, 0.0f, 0.0f, width, height, uFrom, vFrom, uWidth, vWidth);
    }
    
    public static void drawTexturedRect(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float width, final float height, final float uFrom, final float vFrom, final float uWidth, final float vWidth) {
        RenderingUtils.draw(7, DefaultVertexFormat.field_227851_o_, buf -> rect((VertexConsumer)buf, renderStack, offsetX, offsetY, zLevel, width, height).tex(uFrom, vFrom, uWidth, vWidth).draw());
    }
    
    @Deprecated
    public static DrawBuilder rect(final VertexConsumer buf, final WidthHeightScreen screen) {
        return rect(buf, (float)screen.getGuiLeft(), (float)screen.getGuiTop(), (float)screen.getGuiZLevel(), (float)screen.getGuiWidth(), (float)screen.getGuiHeight());
    }
    
    public static DrawBuilder rect(final VertexConsumer buf, final PoseStack renderStack, final WidthHeightScreen screen) {
        return rect(buf, renderStack, (float)screen.getGuiLeft(), (float)screen.getGuiTop(), (float)screen.getGuiZLevel(), (float)screen.getGuiWidth(), (float)screen.getGuiHeight());
    }
    
    @Deprecated
    public static DrawBuilder rect(final VertexConsumer buf, final float offsetX, final float offsetY, final float offsetZ, final float width, final float height) {
        return rect(buf, RenderingGuiUtils.EMPTY, offsetX, offsetY, offsetZ, width, height);
    }
    
    public static DrawBuilder rect(final VertexConsumer buf, final PoseStack renderStack, final float offsetX, final float offsetY, final float offsetZ, final float width, final float height) {
        return new DrawBuilder(buf, renderStack, offsetX, offsetY, offsetZ, width, height);
    }
    
    static {
        EMPTY = new PoseStack();
    }
    
    public static class DrawBuilder
    {
        private final VertexConsumer buf;
        private final PoseStack renderStack;
        private float offsetX;
        private float offsetY;
        private float offsetZ;
        private float width;
        private float height;
        private float u;
        private float v;
        private float uWidth;
        private float vWidth;
        private Color color;
        
        private DrawBuilder(final VertexConsumer buf, final PoseStack renderStack, final float offsetX, final float offsetY, final float offsetZ, final float width, final float height) {
            this.u = 0.0f;
            this.v = 0.0f;
            this.uWidth = 1.0f;
            this.vWidth = 1.0f;
            this.color = Color.WHITE;
            this.buf = buf;
            this.renderStack = renderStack;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.width = width;
            this.height = height;
        }
        
        @Deprecated
        public DrawBuilder at(final float offsetX, final float offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }
        
        @Deprecated
        public DrawBuilder zLevel(final float offsetZ) {
            this.offsetZ = offsetZ;
            return this;
        }
        
        public DrawBuilder dim(final float width, final float height) {
            this.width = width;
            this.height = height;
            return this;
        }
        
        public DrawBuilder tex(final TextureAtlasSprite tas) {
            return this.tex(tas.func_94209_e(), tas.func_94206_g(), tas.func_94212_f() - tas.func_94209_e(), tas.func_94210_h() - tas.func_94206_g());
        }
        
        public DrawBuilder tex(final AbstractRenderableTexture texture) {
            final Tuple<Float, Float> uv = texture.getUVOffset();
            return this.tex((float)uv.func_76341_a(), (float)uv.func_76340_b(), texture.getUWidth(), texture.getVWidth());
        }
        
        public DrawBuilder tex(final SpriteSheetResource sprite, final long tick) {
            final Tuple<Float, Float> uv = sprite.getUVOffset(tick);
            return this.tex((float)uv.func_76341_a(), (float)uv.func_76340_b(), sprite.getUWidth(), sprite.getVWidth());
        }
        
        public DrawBuilder tex(final float u, final float v, final float uWidth, final float vWidth) {
            this.u = u;
            this.v = v;
            this.uWidth = uWidth;
            this.vWidth = vWidth;
            return this;
        }
        
        public DrawBuilder color(final Color color) {
            this.color = color;
            return this;
        }
        
        public DrawBuilder color(final int color) {
            return this.color(new Color(color, true));
        }
        
        public DrawBuilder color(final int r, final int g, final int b, final int a) {
            return this.color(new Color(r, g, b, a));
        }
        
        public DrawBuilder color(final float r, final float g, final float b, final float a) {
            return this.color(new Color(r, g, b, a));
        }
        
        public DrawBuilder draw() {
            final int r = this.color.getRed();
            final int g = this.color.getGreen();
            final int b = this.color.getBlue();
            final int a = this.color.getAlpha();
            final Matrix4f offset = this.renderStack.func_227866_c_().func_227870_a_();
            this.buf.func_227888_a_(offset, this.offsetX, this.offsetY + this.height, this.offsetZ).func_225586_a_(r, g, b, a).func_225583_a_(this.u, this.v + this.vWidth).func_181675_d();
            this.buf.func_227888_a_(offset, this.offsetX + this.width, this.offsetY + this.height, this.offsetZ).func_225586_a_(r, g, b, a).func_225583_a_(this.u + this.uWidth, this.v + this.vWidth).func_181675_d();
            this.buf.func_227888_a_(offset, this.offsetX + this.width, this.offsetY, this.offsetZ).func_225586_a_(r, g, b, a).func_225583_a_(this.u + this.uWidth, this.v).func_181675_d();
            this.buf.func_227888_a_(offset, this.offsetX, this.offsetY, this.offsetZ).func_225586_a_(r, g, b, a).func_225583_a_(this.u, this.v).func_181675_d();
            return this;
        }
    }
}
