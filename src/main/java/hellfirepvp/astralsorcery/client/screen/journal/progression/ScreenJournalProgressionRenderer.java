package hellfirepvp.astralsorcery.client.screen.journal.progression;

import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.client.gui.Font;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import net.minecraft.client.Minecraft;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.awt.Rectangle;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import hellfirepvp.astralsorcery.client.screen.helper.ScalingPoint;
import hellfirepvp.astralsorcery.client.screen.helper.ScreenRenderBoundingBox;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;

public class ScreenJournalProgressionRenderer
{
    private final GalaxySizeHandler sizeHandler;
    private final ScreenJournalProgression parentGui;
    public ScreenRenderBoundingBox realRenderBox;
    private int realCoordLowerX;
    private int realCoordLowerY;
    private int realRenderWidth;
    private int realRenderHeight;
    private final ScalingPoint mousePointScaled;
    private ScalingPoint previousMousePointScaled;
    private ResearchProgression focusedClusterZoom;
    private ResearchProgression focusedClusterMouse;
    private ScreenJournalClusterRenderer clusterRenderer;
    private long doubleClickLast;
    private boolean hasPrevOffset;
    private final Map<Rectangle, ResearchProgression> clusterRectMap;
    
    public ScreenJournalProgressionRenderer(final ScreenJournalProgression gui) {
        this.focusedClusterZoom = null;
        this.focusedClusterMouse = null;
        this.clusterRenderer = null;
        this.doubleClickLast = 0L;
        this.hasPrevOffset = false;
        this.clusterRectMap = new HashMap<Rectangle, ResearchProgression>();
        this.parentGui = gui;
        this.sizeHandler = new GalaxySizeHandler();
        this.refreshSize();
        this.mousePointScaled = ScalingPoint.createPoint(this.sizeHandler.clampX(this.sizeHandler.getTotalWidth() / 2.0f), this.sizeHandler.clampY(this.sizeHandler.getTotalHeight() / 2.0f), this.sizeHandler.getScalingFactor(), false);
        this.moveMouse(this.sizeHandler.getTotalWidth() / 2.0f, this.sizeHandler.getTotalHeight() / 2.0f);
        this.applyMovedMouseOffset();
    }
    
    public void refreshSize() {
        this.sizeHandler.updateSize();
    }
    
    public void setBox(final int left, final int top, final int right, final int bottom) {
        this.realRenderBox = new ScreenRenderBoundingBox(left, top, right, bottom);
        this.realRenderWidth = (int)this.realRenderBox.getWidth();
        this.realRenderHeight = (int)this.realRenderBox.getHeight();
    }
    
    public void moveMouse(final float changedX, final float changedY) {
        if (this.sizeHandler.getScalingFactor() >= 6.1 && this.clusterRenderer != null) {
            this.clusterRenderer.moveMouse(changedX, changedY);
        }
        else if (this.hasPrevOffset) {
            this.mousePointScaled.updateScaledPos(this.sizeHandler.clampX(this.previousMousePointScaled.getScaledPosX() + changedX), this.sizeHandler.clampY(this.previousMousePointScaled.getScaledPosY() + changedY), this.sizeHandler.getScalingFactor());
        }
        else {
            this.mousePointScaled.updateScaledPos(this.sizeHandler.clampX(this.mousePointScaled.getScaledPosX()), this.sizeHandler.clampY(this.mousePointScaled.getScaledPosY()), this.sizeHandler.getScalingFactor());
        }
    }
    
    public void applyMovedMouseOffset() {
        if (this.sizeHandler.getScalingFactor() >= 6.1 && this.clusterRenderer != null) {
            this.clusterRenderer.applyMovedMouseOffset();
        }
        else {
            this.previousMousePointScaled = ScalingPoint.createPoint(this.mousePointScaled.getScaledPosX(), this.mousePointScaled.getScaledPosY(), this.sizeHandler.getScalingFactor(), true);
            this.hasPrevOffset = true;
        }
    }
    
    public void updateOffset(final int guiLeft, final int guiTop) {
        this.realCoordLowerX = guiLeft;
        this.realCoordLowerY = guiTop;
    }
    
    public void centerMouse() {
        this.moveMouse(this.parentGui.getGuiLeft() + this.sizeHandler.getTotalWidth() / 2.0f, this.parentGui.getGuiTop() + this.sizeHandler.getTotalHeight() / 2.0f);
    }
    
    public void updateMouseState() {
        this.moveMouse(0.0f, 0.0f);
    }
    
    public void unfocus() {
        this.focusedClusterZoom = null;
    }
    
    public void focus(@Nonnull final ResearchProgression researchCluster) {
        this.focusedClusterZoom = researchCluster;
        this.clusterRenderer = new ScreenJournalClusterRenderer(researchCluster, this.realRenderHeight, this.realRenderWidth, this.realCoordLowerX, this.realCoordLowerY);
    }
    
    public boolean propagateClick(final float mouseX, final float mouseY) {
        if (this.clusterRenderer != null && this.sizeHandler.getScalingFactor() > 6.0f && this.clusterRenderer.propagateClick(this.parentGui, mouseX, mouseY)) {
            return true;
        }
        if (this.focusedClusterMouse != null && this.sizeHandler.getScalingFactor() <= 6.0f) {
            final long current = System.currentTimeMillis();
            if (current - this.doubleClickLast < 400L) {
                for (int timeout = 500; this.focusedClusterMouse != null && this.sizeHandler.getScalingFactor() < 9.9 && timeout > 0; --timeout) {
                    this.handleZoomIn(mouseX, mouseY);
                }
                this.doubleClickLast = 0L;
                return true;
            }
            this.doubleClickLast = current;
        }
        return false;
    }
    
    public void drawMouseHighlight(final PoseStack renderStack, final float zLevel, final int mouseX, final int mouseY) {
        if (this.clusterRenderer != null && this.sizeHandler.getScalingFactor() > 6.0f) {
            this.clusterRenderer.drawMouseHighlight(renderStack, zLevel, mouseX, mouseY);
        }
    }
    
    public void resetZoom() {
        this.sizeHandler.resetZoom();
        this.rescale(this.sizeHandler.getScalingFactor());
    }
    
    public void handleZoomOut() {
        this.sizeHandler.handleZoomOut();
        this.rescale(this.sizeHandler.getScalingFactor());
        if (this.sizeHandler.getScalingFactor() <= 4.0) {
            this.unfocus();
        }
        else if (this.sizeHandler.getScalingFactor() >= 6.0 && this.clusterRenderer != null) {
            this.clusterRenderer.handleZoomOut();
        }
    }
    
    public void handleZoomIn(final float mouseX, final float mouseY) {
        final float scale = this.sizeHandler.getScalingFactor();
        if (scale >= 4.0f) {
            if (this.focusedClusterZoom == null) {
                final ResearchProgression prog = this.tryFocusCluster(mouseX, mouseY);
                if (prog != null) {
                    this.focus(prog);
                }
            }
            if (this.focusedClusterZoom == null) {
                return;
            }
            if (scale < 6.1f) {
                final float vDiv = (2.0f - (scale - 4.0f)) * 10.0f;
                final JournalCluster cluster = JournalProgressionClusterMapping.getClusterMapping(this.focusedClusterZoom);
                final float x = this.sizeHandler.evRelativePosX((float)cluster.x);
                final float y = this.sizeHandler.evRelativePosY((float)cluster.y);
                final float width = this.sizeHandler.scaledDistanceX((float)cluster.x, (float)cluster.maxX);
                final float height = this.sizeHandler.scaledDistanceY((float)cluster.y, (float)cluster.maxY);
                final Vector3 center = new Vector3(x + width / 2.0f, y + height / 2.0f, 0.0f);
                final Vector3 mousePos = new Vector3(this.mousePointScaled.getScaledPosX(), this.mousePointScaled.getScaledPosY(), 0.0f);
                final Vector3 dir = center.subtract(mousePos);
                if (vDiv > 0.05) {
                    dir.divide(vDiv);
                }
                if (!this.hasPrevOffset) {
                    this.mousePointScaled.updateScaledPos(this.sizeHandler.clampX((float)(mousePos.getX() + dir.getX())), this.sizeHandler.clampY((float)(mousePos.getY() + dir.getY())), this.sizeHandler.getScalingFactor());
                }
                else {
                    this.previousMousePointScaled.updateScaledPos(this.sizeHandler.clampX((float)(mousePos.getX() + dir.getX())), this.sizeHandler.clampY((float)(mousePos.getY() + dir.getY())), this.sizeHandler.getScalingFactor());
                }
                this.updateMouseState();
            }
            else if (this.clusterRenderer != null) {
                this.clusterRenderer.handleZoomIn();
            }
        }
        this.sizeHandler.handleZoomIn();
        this.mousePointScaled.rescale(this.sizeHandler.getScalingFactor());
        if (this.previousMousePointScaled != null) {
            this.previousMousePointScaled.rescale(this.sizeHandler.getScalingFactor());
        }
    }
    
    private void rescale(final float newScale) {
        this.mousePointScaled.rescale(newScale);
        if (this.previousMousePointScaled != null) {
            this.previousMousePointScaled.rescale(newScale);
        }
        this.updateMouseState();
    }
    
    public void drawProgressionPart(final PoseStack renderStack, final float zLevel, final int mouseX, final int mouseY) {
        this.drawBackground(renderStack, zLevel);
        this.drawClusters(renderStack, zLevel);
        this.focusedClusterMouse = this.tryFocusCluster(mouseX, mouseY);
        float scaleX = this.mousePointScaled.getPosX();
        float scaleY = this.mousePointScaled.getPosY();
        if (this.sizeHandler.getScalingFactor() >= 6.1 && this.focusedClusterZoom != null && this.clusterRenderer != null) {
            final JournalCluster cluster = JournalProgressionClusterMapping.getClusterMapping(this.focusedClusterZoom);
            this.drawClusterBackground(renderStack, cluster.clusterBackgroundTexture, zLevel);
            this.clusterRenderer.drawClusterScreen(renderStack, this.parentGui, zLevel);
            scaleX = this.clusterRenderer.getMouseX();
            scaleY = this.clusterRenderer.getMouseY();
        }
        if (this.focusedClusterMouse != null) {
            final JournalCluster cluster = JournalProgressionClusterMapping.getClusterMapping(this.focusedClusterMouse);
            final float width = this.sizeHandler.scaledDistanceX((float)cluster.x, (float)cluster.maxX);
            final float height = this.sizeHandler.scaledDistanceY((float)cluster.y, (float)cluster.maxY);
            final Point2D.Float offset = this.sizeHandler.scalePointToGui(this.parentGui, this.mousePointScaled, new Point2D.Float((float)cluster.x, (float)cluster.y));
            final float scale = this.sizeHandler.getScalingFactor();
            float br = 1.0f;
            if (scale > 8.01f) {
                br = 0.0f;
            }
            else if (scale >= 6.0f) {
                br = 1.0f - (scale - 6.0f) / 2.0f;
            }
            final FormattedCharSequence name = (FormattedCharSequence)this.focusedClusterMouse.getName();
            final float length = Minecraft.getInstance().field_71466_p.func_238414_a_(name) * 1.4f;
            int alpha = 204;
            alpha *= (int)br;
            alpha = Math.max(alpha, 5);
            final int color = 0x5A28FF | alpha << 24;
            renderStack.popPose();
            renderStack.func_227861_a_(offset.x + width / 2.0f - length / 2.0, (double)(offset.y + height / 3.0f), 0.0);
            renderStack.translate(1.4f, 1.4f, 1.0f);
            RenderingDrawUtils.renderStringAt(name, renderStack, null, color, true);
            renderStack.scale();
        }
        this.drawStarParallaxLayers(renderStack, scaleX, scaleY, zLevel);
    }
    
    @Nullable
    private ResearchProgression tryFocusCluster(final double mouseX, final double mouseY) {
        for (final Rectangle r : this.clusterRectMap.keySet()) {
            if (r.contains(mouseX, mouseY)) {
                return this.clusterRectMap.get(r);
            }
        }
        return null;
    }
    
    private void drawClusters(final PoseStack renderStack, final float zLevel) {
        this.clusterRectMap.clear();
        if (this.sizeHandler.getScalingFactor() >= 8.01) {
            return;
        }
        final PlayerProgress thisProgress = ResearchHelper.getClientProgress();
        for (final ResearchProgression progress : thisProgress.getResearchProgression()) {
            this.renderCluster(renderStack, progress, JournalProgressionClusterMapping.getClusterMapping(progress), zLevel);
        }
    }
    
    private void renderCluster(final PoseStack renderStack, final ResearchProgression p, final JournalCluster cluster, final float zLevel) {
        final Point2D.Float pCluster = this.sizeHandler.scalePointToGui(this.parentGui, this.mousePointScaled, new Point2D.Float((float)cluster.x, (float)cluster.y));
        final float width = this.sizeHandler.scaledDistanceX((float)cluster.x, (float)cluster.maxX);
        final float height = this.sizeHandler.scaledDistanceY((float)cluster.y, (float)cluster.maxY);
        final Rectangle r = new Rectangle(MathHelper.func_76141_d(pCluster.x), MathHelper.func_76141_d(pCluster.y), MathHelper.func_76141_d(width), MathHelper.func_76141_d(height));
        this.clusterRectMap.put(r, p);
        cluster.cloudTexture.bindTexture();
        final float scale = this.sizeHandler.getScalingFactor();
        float br;
        if (scale > 8.01f) {
            br = 0.0f;
        }
        else if (scale >= 6.0f) {
            br = 1.0f - (scale - 6.0f) / 2.0f;
        }
        else {
            br = 1.0f;
        }
        RenderSystem.enableBlend();
        Blending.ADDITIVEDARK.apply();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Matrix4f offset = renderStack.last().func_227870_a_();
            buf.vertex(offset, pCluster.x + 0.0f, pCluster.y + height, zLevel).color(br, br, br, br).func_225583_a_(0.0f, 1.0f).endVertex();
            buf.vertex(offset, pCluster.x + width, pCluster.y + height, zLevel).color(br, br, br, br).func_225583_a_(1.0f, 1.0f).endVertex();
            buf.vertex(offset, pCluster.x + width, pCluster.y + 0.0f, zLevel).color(br, br, br, br).func_225583_a_(1.0f, 0.0f).endVertex();
            buf.vertex(offset, pCluster.x + 0.0f, pCluster.y + 0.0f, zLevel).color(br, br, br, br).func_225583_a_(0.0f, 0.0f).endVertex();
            return;
        });
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
    }
    
    private void drawClusterBackground(final PoseStack renderStack, final AbstractRenderableTexture tex, final float zLevel) {
        final float scale = this.sizeHandler.getScalingFactor();
        float br;
        if (scale > 8.01f) {
            br = 0.75f;
        }
        else if (scale >= 6.0f) {
            br = (scale - 6.0f) / 2.0f * 0.75f;
        }
        else {
            br = 0.0f;
        }
        tex.bindTexture();
        RenderSystem.enableBlend();
        Blending.ADDITIVEDARK.apply();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Matrix4f offset = renderStack.last().func_227870_a_();
            buf.vertex(offset, (float)this.realCoordLowerX, (float)(this.realCoordLowerY + this.realRenderHeight), zLevel).color(br, br, br, br).func_225583_a_(0.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.realCoordLowerX + this.realRenderWidth), (float)(this.realCoordLowerY + this.realRenderHeight), zLevel).color(br, br, br, br).func_225583_a_(1.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.realCoordLowerX + this.realRenderWidth), (float)this.realCoordLowerY, zLevel).color(br, br, br, br).func_225583_a_(1.0f, 0.0f).endVertex();
            buf.vertex(offset, (float)this.realCoordLowerX, (float)this.realCoordLowerY, zLevel).color(br, br, br, br).func_225583_a_(0.0f, 0.0f).endVertex();
            return;
        });
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
    
    private void drawBackground(final PoseStack renderStack, final float zLevel) {
        final float br = 0.35f;
        TexturesAS.TEX_GUI_BACKGROUND_DEFAULT.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            final Matrix4f offset = renderStack.last().func_227870_a_();
            buf.vertex(offset, (float)this.realCoordLowerX, (float)(this.realCoordLowerY + this.realRenderHeight), zLevel).color(br, br, br, 1.0f).func_225583_a_(0.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.realCoordLowerX + this.realRenderWidth), (float)(this.realCoordLowerY + this.realRenderHeight), zLevel).color(br, br, br, 1.0f).func_225583_a_(1.0f, 1.0f).endVertex();
            buf.vertex(offset, (float)(this.realCoordLowerX + this.realRenderWidth), (float)this.realCoordLowerY, zLevel).color(br, br, br, 1.0f).func_225583_a_(1.0f, 0.0f).endVertex();
            buf.vertex(offset, (float)this.realCoordLowerX, (float)this.realCoordLowerY, zLevel).color(br, br, br, 1.0f).func_225583_a_(0.0f, 0.0f).endVertex();
        });
    }
    
    private void drawStarParallaxLayers(final PoseStack renderStack, final float scalePosX, final float scalePosY, final float zLevel) {
        TexturesAS.TEX_GUI_STARFIELD_OVERLAY.bindTexture();
        RenderSystem.enableBlend();
        Blending.OVERLAYDARK.apply();
        final float offsetX = scalePosX / 2000.0f;
        final float offsetY = scalePosY / 1000.0f;
        RenderingUtils.draw(7, DefaultVertexFormat.BLIT_SCREEN, buf -> {
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 2.0f);
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 1.5f);
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 1.0f);
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 0.75f);
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 0.5f);
            this.drawStarOverlay((VertexConsumer)buf, renderStack, zLevel, offsetX, offsetY, 0.3f);
            return;
        });
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
    
    private void drawStarOverlay(final VertexConsumer buf, final PoseStack renderStack, final float zLevel, final float scalePosX, final float scalePosY, final float scaleFactor) {
        final float scale = this.sizeHandler.getScalingFactor() / 40.0f;
        final float x = (float)this.parentGui.getGuiLeft();
        final float y = (float)this.parentGui.getGuiTop();
        final float width = (float)this.parentGui.getGuiWidth();
        final float height = (float)this.parentGui.getGuiHeight();
        final float u = 0.2f + scalePosX + scaleFactor + scale;
        final float v = 0.2f + scalePosY + scaleFactor + scale;
        final float uL = 0.6f * scaleFactor - scale * 2.0f;
        final float vL = 0.6f * scaleFactor - scale * 2.0f;
        if (vL <= 0.0f || uL <= 0.0f) {
            return;
        }
        final Matrix4f offset = renderStack.last().func_227870_a_();
        buf.vertex(offset, x, y + height, zLevel).color(0.75f, 0.75f, 0.75f, 0.7f).func_225583_a_(u, v + vL).endVertex();
        buf.vertex(offset, x + width, y + height, zLevel).color(0.75f, 0.75f, 0.75f, 0.7f).func_225583_a_(u + uL, v + vL).endVertex();
        buf.vertex(offset, x + width, y, zLevel).color(0.75f, 0.75f, 0.75f, 0.7f).func_225583_a_(u + uL, v).endVertex();
        buf.vertex(offset, x, y, zLevel).color(0.75f, 0.75f, 0.75f, 0.7f).func_225583_a_(u, v).endVertex();
    }
}
