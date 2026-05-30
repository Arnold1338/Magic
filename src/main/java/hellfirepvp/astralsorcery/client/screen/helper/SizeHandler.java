package hellfirepvp.astralsorcery.client.screen.helper;

import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import java.awt.Point;
import net.minecraft.util.Mth;
import java.awt.geom.Point2D;
import javax.annotation.Nullable;

public abstract class SizeHandler
{
    private static final int W_H_NODE = 18;
    private float widthHeightNodes;
    private float spaceBetweenNodes;
    private float shiftX;
    private float shiftY;
    private float leftOffset;
    private float topOffset;
    private float totalWidth;
    private float totalHeight;
    private float scalingFactor;
    private float maxScale;
    private float minScale;
    private float scaleSpeed;
    
    public SizeHandler() {
        this.widthHeightNodes = 18.0f;
        this.spaceBetweenNodes = 18.0f;
        this.scalingFactor = 1.0f;
        this.maxScale = 10.0f;
        this.minScale = 1.0f;
        this.scaleSpeed = 0.2f;
    }
    
    public void setMaxScale(final float maxScale) {
        this.maxScale = maxScale;
    }
    
    public void setScaleSpeed(final float scaleSpeed) {
        this.scaleSpeed = scaleSpeed;
    }
    
    public void setMinScale(final float minScale) {
        this.minScale = minScale;
    }
    
    public void setWidthHeightNodes(final float widthHeightNodes) {
        this.widthHeightNodes = widthHeightNodes;
    }
    
    public void setSpaceBetweenNodes(final float spaceBetweenNodes) {
        this.spaceBetweenNodes = spaceBetweenNodes;
    }
    
    public void updateSize() {
        this.resetZoom();
        float leftMost = 0.0f;
        float rightMost = 0.0f;
        float upperMost = 0.0f;
        float lowerMost = 0.0f;
        final float[] requiredRect = this.buildRequiredRectangle();
        if (requiredRect != null) {
            leftMost = requiredRect[0];
            rightMost = requiredRect[1];
            upperMost = requiredRect[2];
            lowerMost = requiredRect[3];
        }
        this.shiftX = (leftMost + rightMost) / 2.0f;
        this.shiftY = (lowerMost + upperMost) / 2.0f;
        final float width = rightMost - leftMost;
        final float height = lowerMost - upperMost;
        this.leftOffset = leftMost - this.shiftX;
        this.topOffset = upperMost - this.shiftY;
        this.totalWidth = width * this.widthHeightNodes + Math.max(width - 1.0f, 0.0f) * this.spaceBetweenNodes;
        this.totalHeight = height * this.widthHeightNodes + Math.max(height - 1.0f, 0.0f) * this.spaceBetweenNodes;
    }
    
    @Nullable
    public abstract float[] buildRequiredRectangle();
    
    public float getTotalWidth() {
        return this.totalWidth * this.scalingFactor;
    }
    
    public float getTotalHeight() {
        return this.totalHeight * this.scalingFactor;
    }
    
    public Point2D.Float getRelativeCenter() {
        return new Point2D.Float(this.getTotalWidth() / 2.0f, this.getTotalHeight() / 2.0f);
    }
    
    public float getScalingFactor() {
        return this.scalingFactor;
    }
    
    public float getZoomedWHNode() {
        return this.widthHeightNodes * this.scalingFactor;
    }
    
    public float getZoomedSpaceBetweenNodes() {
        return this.spaceBetweenNodes * this.scalingFactor;
    }
    
    public float scaleAccordingly(final float toScale) {
        return toScale * this.scalingFactor;
    }
    
    public void handleZoomIn() {
        if (this.scalingFactor >= this.maxScale) {

        }
        this.scalingFactor = Math.min(this.maxScale, this.scalingFactor + this.scaleSpeed);
    }
    
    public void handleZoomOut() {
        if (this.scalingFactor <= this.minScale) {

        }
        this.scalingFactor = Math.max(this.minScale, this.scalingFactor - this.scaleSpeed);
    }
    
    public void forceScaleTo(final float scale) {
        this.scalingFactor = scale;
    }
    
    public void resetZoom() {
        this.scalingFactor = 1.0f;
    }
    
    public float clampX(final float centerX) {
        return Mth.canEnchant(centerX, 0.0f, this.getTotalWidth());
    }
    
    public float clampY(final float centerY) {
        return Mth.canEnchant(centerY, 0.0f, this.getTotalHeight());
    }
    
    public float evRelativePosX(final float relativeX) {
        final float shiftedX = relativeX - this.shiftX;
        final float leftShift = shiftedX - this.leftOffset;
        float offsetX = leftShift * (this.getZoomedWHNode() + this.getZoomedSpaceBetweenNodes());
        offsetX += 0.5f * this.getZoomedWHNode();
        return offsetX;
    }
    
    public float evRelativePosY(final float relativeY) {
        final float shiftedY = relativeY - this.shiftY;
        final float topShift = shiftedY - this.topOffset;
        float offsetY = topShift * (this.getZoomedWHNode() + this.getZoomedSpaceBetweenNodes());
        offsetY += 0.5f * this.getZoomedWHNode();
        return offsetY;
    }
    
    public Point2D.Float evRelativePos(final Point2D.Float offset) {
        return new Point2D.Float(this.evRelativePosX(offset.x), this.evRelativePosY(offset.y));
    }
    
    public Point2D.Float evRelativePos(final Point offset) {
        return new Point2D.Float(this.evRelativePosX((float)offset.x), this.evRelativePosY((float)offset.y));
    }
    
    public float scaledDistanceX(final float fromX, final float toX) {
        return this.evRelativePosX(toX) - this.evRelativePosX(fromX);
    }
    
    public float scaledDistanceY(final float fromY, final float toY) {
        return this.evRelativePosY(toY) - this.evRelativePosY(fromY);
    }
    
    public Point2D.Float scalePointToGui(final WidthHeightScreen screen, final ScalingPoint currentOffset, final Point2D.Float point) {
        final Point2D.Float shifted = this.evRelativePos(point);
        final float fX = shifted.x - currentOffset.getScaledPosX() + screen.getGuiLeft() + screen.getGuiWidth() / 2.0f;
        final float fY = shifted.y - currentOffset.getScaledPosY() + screen.getGuiTop() + screen.getGuiHeight() / 2.0f;
        return new Point2D.Float(fX, fY);
    }
}
