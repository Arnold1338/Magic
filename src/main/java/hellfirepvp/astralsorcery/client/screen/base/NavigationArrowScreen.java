package hellfirepvp.astralsorcery.client.screen.base;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.awt.Rectangle;
import com.mojang.blaze3d.vertex.PoseStack;

public interface NavigationArrowScreen
{
    default Rectangle drawArrow(final PoseStack renderStack, final int offsetLeft, final int offsetTop, final int guiZLevel, final Type direction, final int mouseX, final int mouseY, final float pTicks) {
        final float width = 30.0f;
        final float height = 15.0f;
        final Rectangle rectArrow = new Rectangle(offsetLeft, offsetTop, (int)width, (int)height);
        renderStack.popPose();
        renderStack.translate(rectArrow.getX() + width / 2.0f, rectArrow.getY() + height / 2.0f, 0.0);
        final float vFrom = (direction == Type.LEFT) ? 0.5f : 0.0f;
        float uFrom;
        if (rectArrow.contains(mouseX, mouseY)) {
            uFrom = 0.5f;
            renderStack.translate(1.1f, 1.1f, 1.1f);
        }
        else {
            uFrom = 0.0f;
            final double t = ClientScheduler.getClientTick() + pTicks;
            final float sin = (float)Math.sin(t / 4.0) / 32.0f + 1.0f;
            renderStack.translate(sin, sin, sin);
        }
        renderStack.translate((double)(-(width / 2.0f)), (double)(-(height / 2.0f)), 0.0);
        TexturesAS.TEX_GUI_BOOK_ARROWS.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 0.0f, 0.0f, (float)guiZLevel, width, height).tex(uFrom, vFrom, 0.5f, 0.5f).color(1.0f, 1.0f, 1.0f, 0.8f).draw());
        renderStack.popPose();
        return rectArrow;
    }
    
    public enum Type
    {
        LEFT, 
        RIGHT;
    }
}
