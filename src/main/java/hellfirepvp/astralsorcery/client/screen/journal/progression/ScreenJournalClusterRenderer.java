package hellfirepvp.astralsorcery.client.screen.journal.progression;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import org.lwjgl.opengl.GL11;
import org.joml.Matrix4f;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import net.minecraft.client.renderer.RenderHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import java.awt.geom.Point2D;
import java.util.List;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.FormattedCharSequence;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalPages;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import java.awt.Rectangle;
import java.util.Map;
import hellfirepvp.astralsorcery.client.screen.helper.ScalingPoint;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;

public class ScreenJournalClusterRenderer
{
    private ProgressionSizeHandler progressionSizeHandler;
    private ResearchProgression progression;
    private ScalingPoint mousePointScaled;
    private ScalingPoint previousMousePointScaled;
    private int renderOffsetX;
    private int renderOffsetY;
    private int renderGuiHeight;
    private int renderGuiWidth;
    private boolean hasPrevOffset;
    private float alpha;
    private Map<Rectangle, ResearchNode> clickableNodes;
    
    public ScreenJournalClusterRenderer(final ResearchProgression progression, final int guiHeight, final int guiWidth, final int guiLeft, final int guiTop) {
        this.hasPrevOffset = false;
        this.alpha = 1.0f;
        this.clickableNodes = new HashMap<Rectangle, ResearchNode>();
        this.progression = progression;
        (this.progressionSizeHandler = new ProgressionSizeHandler(progression)).setMaxScale(1.2f);
        this.progressionSizeHandler.setMinScale(0.1f);
        this.progressionSizeHandler.setScaleSpeed(0.044999998f);
        this.progressionSizeHandler.updateSize();
        this.progressionSizeHandler.forceScaleTo(0.1f);
        this.mousePointScaled = ScalingPoint.createPoint(0.0f, 0.0f, this.progressionSizeHandler.getScalingFactor(), false);
        this.centerMouse();
        this.applyMovedMouseOffset();
        this.renderOffsetX = guiLeft;
        this.renderOffsetY = guiTop;
        this.renderGuiHeight = guiHeight;
        this.renderGuiWidth = guiWidth;
    }
    
    public boolean propagateClick(final ScreenJournalProgression parent, final double mouseX, final double mouseY) {
        final Rectangle frame = new Rectangle(this.renderOffsetX, this.renderOffsetY, this.renderGuiWidth, this.renderGuiHeight);
        if (frame.contains(mouseX, mouseY)) {
            for (final Rectangle r : this.clickableNodes.keySet()) {
                if (r.contains(mouseX, mouseY)) {
                    final ResearchNode clicked = this.clickableNodes.get(r);
                    Minecraft.getInstance().func_147108_a((Screen)new ScreenJournalPages(parent, clicked));
                    return true;
                }
            }
        }
        return false;
    }
    
    public void drawMouseHighlight(final PoseStack renderStack, final float zLevel, final int mouseX, final int mouseY) {
        final Rectangle frame = new Rectangle(this.renderOffsetX, this.renderOffsetY, this.renderGuiWidth, this.renderGuiHeight);
        if (frame.contains(mouseX, mouseY)) {
            for (final Rectangle r : this.clickableNodes.keySet()) {
                if (r.contains(mouseX, mouseY)) {
                    final FormattedCharSequence name = (FormattedCharSequence)this.clickableNodes.get(r).getName();
                    renderStack.popPose();
                    renderStack.translate(r.getX(), r.getY(), (double)(zLevel + 200.0f));
                    renderStack.translate(this.progressionSizeHandler.getScalingFactor(), this.progressionSizeHandler.getScalingFactor(), 1.0f);
                    RenderingDrawUtils.renderBlueTooltipComponents(renderStack, 0.0f, 0.0f, 0.0f, Lists.newArrayList((Object[])new FormattedCharSequence[] { name }), Minecraft.getInstance().font, false);
                    renderStack.popPose();
                }
            }
        }
    }
    
    public void centerMouse() {
        final Point2D.Float center = this.progressionSizeHandler.getRelativeCenter();
        this.moveMouse(center.x, center.y);
    }
    
    public void moveMouse(final float changedX, final float changedY) {
        if (this.hasPrevOffset) {
            this.mousePointScaled.updateScaledPos(this.progressionSizeHandler.clampX(this.previousMousePointScaled.getScaledPosX() + changedX), this.progressionSizeHandler.clampY(this.previousMousePointScaled.getScaledPosY() + changedY), this.progressionSizeHandler.getScalingFactor());
        }
        else {
            this.mousePointScaled.updateScaledPos(this.progressionSizeHandler.clampX(changedX), this.progressionSizeHandler.clampY(changedY), this.progressionSizeHandler.getScalingFactor());
        }
    }
    
    public void applyMovedMouseOffset() {
        this.previousMousePointScaled = ScalingPoint.createPoint(this.mousePointScaled.getScaledPosX(), this.mousePointScaled.getScaledPosY(), this.progressionSizeHandler.getScalingFactor(), true);
        this.hasPrevOffset = true;
    }
    
    public void handleZoomOut() {
        this.progressionSizeHandler.handleZoomOut();
        this.rescale(this.progressionSizeHandler.getScalingFactor());
    }
    
    public void handleZoomIn() {
        this.progressionSizeHandler.handleZoomIn();
        this.rescale(this.progressionSizeHandler.getScalingFactor());
    }
    
    public float getMouseX() {
        return this.mousePointScaled.getPosX();
    }
    
    public float getMouseY() {
        return this.mousePointScaled.getPosY();
    }
    
    private void rescale(final float newScale) {
        this.mousePointScaled.rescale(newScale);
        if (this.previousMousePointScaled != null) {
            this.previousMousePointScaled.rescale(newScale);
        }
        this.moveMouse(0.0f, 0.0f);
    }
    
    public void drawClusterScreen(final PoseStack renderStack, final WidthHeightScreen parentGui, final float zLevel) {
        this.clickableNodes.clear();
        this.drawNodesAndConnections(renderStack, parentGui, zLevel);
    }
    
    private void drawNodesAndConnections(final PoseStack renderStack, final WidthHeightScreen parentGui, final float zLevel) {
        this.alpha = this.progressionSizeHandler.getScalingFactor();
        this.alpha -= 0.25f;
        this.alpha /= 0.75f;
        this.alpha = Mth.canEnchant(this.alpha, 0.0f, 1.0f);
        final Map<ResearchNode, Point2D.Float> displayPositions = new HashMap<ResearchNode, Point2D.Float>();
        final Iterator<ResearchNode> iterator = this.progression.getResearchNodes().iterator();
        ResearchNode node = null;
        while (iterator.hasNext()) {
            node = iterator.next();
            if (!node.canSee(ResearchHelper.getClientProgress())) {
                continue;
            }
            final Point2D.Float from = this.progressionSizeHandler.scalePointToGui(parentGui, this.mousePointScaled, new Point2D.Float(node.renderPosX, node.renderPosZ));
            for (final ResearchNode target : node.getConnectionsTo()) {
                final Point2D.Float to = this.progressionSizeHandler.scalePointToGui(parentGui, this.mousePointScaled, new Point2D.Float(target.renderPosX, target.renderPosZ));
                this.drawConnection(renderStack, from.x, from.y, to.x, to.y, zLevel);
            }
            displayPositions.put(node, from);
        }
        displayPositions.forEach((node, pos) -> this.renderNodeToGUI(renderStack, node, pos, zLevel));
    }
    
    private void renderNodeToGUI(final PoseStack renderStack, final ResearchNode node, final Point2D.Float offset, final float zLevel) {
        final float zoomedWH = this.progressionSizeHandler.getZoomedWHNode();
        final float offsetX = offset.x - zoomedWH / 2.0f;
        final float offsetY = offset.y - zoomedWH / 2.0f;
        node.getBackgroundTexture().resolve().bindTexture();
        if (this.progressionSizeHandler.getScalingFactor() >= 0.7) {
            this.clickableNodes.put(new Rectangle(Mth.func_76141_d(offsetX), Mth.func_76141_d(offsetY), Mth.func_76141_d(zoomedWH), Mth.func_76141_d(zoomedWH)), node);
        }
        this.drawResearchItemBackground(zoomedWH, offsetX, offsetY, zLevel);
        final float pxWH = this.progressionSizeHandler.getZoomedWHNode() / 16.0f;
        switch (node.getNodeRenderType()) {
            case ITEMSTACK: {
                renderStack.popPose();
                renderStack.translate((double)offsetX, (double)offsetY, 0.0);
                renderStack.translate(this.progressionSizeHandler.getScalingFactor(), this.progressionSizeHandler.getScalingFactor(), 1.0f);
                renderStack.translate(3.0, 3.0, 100.0);
                renderStack.translate(0.75f, 0.75f, 1.0f);
                RenderHelper.func_227780_a_();
                RenderingUtils.renderTranslucentItemStackModelGUI(node.getRenderItemStack(ClientScheduler.getClientTick()), renderStack, Color.WHITE, Blending.DEFAULT, Mth.getDescriptionId((int)(this.alpha * 255.0f), 0, 255));
                RenderHelper.func_74518_a();
                renderStack.popPose();
                break;
            }
            case TEXTURE_SPRITE: {
                final Color col = node.getTextureColorHint();
                final float r = col.getRed() / 255.0f * this.alpha;
                final float g = col.getGreen() / 255.0f * this.alpha;
                final float b = col.getBlue() / 255.0f * this.alpha;
                final float a = col.getAlpha() / 255.0f * this.alpha;
                final SpriteSheetResource res = node.getSpriteTexture().resolveSprite();
                res.getResource().bindTexture();
                final Tuple<Float, Float> uvTexture = res.getUVOffset(ClientScheduler.getClientTick());
                renderStack.popPose();
                renderStack.translate((double)offsetX, (double)offsetY, 0.0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderingUtils.draw(7, DefaultVertexFormat.fogColor, buf -> {
                    final Matrix4f matr = renderStack.last().translate();
                    buf.vertex(matr, pxWH, zoomedWH - pxWH, zLevel).pushPose()r, g, b, a).setPos((float)uvTexture.getA(), (float)uvTexture.getB() + res.getVLength()).blockPosition();
                    buf.vertex(matr, zoomedWH - pxWH, zoomedWH - pxWH, zLevel).pushPose()r, g, b, a).setPos((float)uvTexture.getA() + res.getULength(), (float)uvTexture.getB() + res.getVLength()).blockPosition();
                    buf.vertex(matr, zoomedWH - pxWH, pxWH, zLevel).pushPose()r, g, b, a).setPos((float)uvTexture.getA() + res.getULength(), (float)uvTexture.getB()).blockPosition();
                    buf.vertex(matr, pxWH, pxWH, zLevel).pushPose()r, g, b, a).setPos((float)uvTexture.getA(), (float)uvTexture.getB()).blockPosition();
                    return;
                });
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                renderStack.popPose();
                break;
            }
        }
    }
    
    private void drawConnection(final PoseStack renderStack, final float originX, final float originY, final float targetX, final float targetY, final float zLevel) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderSystem.lineWidth(4.0f);
        final long clientTicks = ClientScheduler.getClientTick();
        final Vector3 origin = new Vector3(originX, originY, 0.0f);
        final Vector3 line = origin.vectorFromHereTo(targetX, targetY, 0.0);
        final int segments = (int)Math.ceil(line.length() / 1.0);
        final int activeSegment = (int)(clientTicks % segments);
        final Vector3 segmentIter = line.divide(segments);
        RenderingUtils.draw(3, DefaultVertexFormat.POSITION_COLOR, buf -> {
            for (int i = segments; i >= 0; --i) {
                final double lx = origin.getX();
                final double ly = origin.getY();
                origin.add(segmentIter);
                final float brightness = 0.6f;
                final float brightness2 = brightness + 0.4f * this.evaluateBrightness(i, activeSegment);
                this.drawLinePart((VertexConsumer)buf, renderStack, lx, ly, origin.getX(), origin.getY(), zLevel, brightness2);
            }
            return;
        });
        RenderSystem.lineWidth(2.0f);
        GL11.glDisable(2848);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
    
    private void drawLinePart(final VertexConsumer buf, final PoseStack renderStack, final double lx, final double ly, final double hx, final double hy, final float zLevel, final float brightness) {
        final Matrix4f offset = renderStack.last().translate();
        buf.vertex(offset, (float)lx, (float)ly, zLevel).pushPose()brightness * this.alpha, brightness * this.alpha, brightness * this.alpha, 0.4f * this.alpha).blockPosition();
        buf.vertex(offset, (float)hx, (float)hy, zLevel).pushPose()brightness * this.alpha, brightness * this.alpha, brightness * this.alpha, 0.4f * this.alpha).blockPosition();
    }
    
    private float evaluateBrightness(final int segment, final int activeSegment) {
        if (segment == activeSegment) {
            return 1.0f;
        }
        final float res = (10 - Math.abs(activeSegment - segment)) / 10.0f;
        return Math.max(0.0f, res);
    }
    
    private void drawResearchItemBackground(final double zoomedWH, final double xAdd, final double yAdd, final float zLevel) {
        RenderSystem.enableBlend();
        RenderingUtils.draw(7, DefaultVertexFormat.fogColor, buf -> {
            buf.func_225582_a_(xAdd, yAdd + zoomedWH, (double)zLevel).pushPose()this.alpha, this.alpha, this.alpha, this.alpha).setPos(0.0f, 1.0f).blockPosition();
            buf.func_225582_a_(xAdd + zoomedWH, yAdd + zoomedWH, (double)zLevel).pushPose()this.alpha, this.alpha, this.alpha, this.alpha).setPos(1.0f, 1.0f).blockPosition();
            buf.func_225582_a_(xAdd + zoomedWH, yAdd, (double)zLevel).pushPose()this.alpha, this.alpha, this.alpha, this.alpha).setPos(1.0f, 0.0f).blockPosition();
            buf.func_225582_a_(xAdd, yAdd, (double)zLevel).pushPose()this.alpha, this.alpha, this.alpha, this.alpha).setPos(0.0f, 0.0f).blockPosition();
            return;
        });
        RenderSystem.disableBlend();
    }
}
