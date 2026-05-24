package hellfirepvp.astralsorcery.client.screen.base;

import hellfirepvp.astralsorcery.common.util.data.BiDiPair;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.BlockPos;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.network.play.client.PktDiscoverConstellation;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import com.google.common.collect.Iterables;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Iterator;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import java.util.function.Supplier;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.util.Random;
import hellfirepvp.astralsorcery.client.util.MouseUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.math.MathHelper;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import net.minecraft.network.chat.Component;
import java.awt.Point;
import java.util.List;

public abstract class ConstellationDiscoveryScreen<D extends DrawArea> extends WidthHeightScreen
{
    public static final int DEFAULT_CONSTELLATION_SIZE = 150;
    private List<D> drawAreas;
    private D currentDrawArea;
    private final List<DrawnLine> drawnLines;
    private Point dragStart;
    private Point dragEnd;
    private boolean initialized;
    
    protected ConstellationDiscoveryScreen(final Component titleIn, final int guiHeight, final int guiWidth) {
        super(titleIn, guiHeight, guiWidth);
        this.drawAreas = new LinkedList<D>();
        this.currentDrawArea = null;
        this.drawnLines = new LinkedList<DrawnLine>();
        this.initialized = false;
    }
    
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        this.drawAreas = this.createDrawAreas();
        if (this.getContext() != null) {
            this.fillConstellations(this.getContext(), this.drawAreas);
            this.initialized = true;
        }
    }
    
    protected boolean isMouseRotatingGui() {
        return true;
    }
    
    @Nonnull
    protected abstract List<D> createDrawAreas();
    
    protected abstract void fillConstellations(final WorldContext p0, final List<D> p1);
    
    protected WorldContext getContext() {
        return SkyHandler.getContext((World)Minecraft.func_71410_x().field_71441_e, LogicalSide.CLIENT);
    }
    
    protected boolean isInitialized() {
        return this.initialized;
    }
    
    @Nullable
    public D getCurrentDrawArea() {
        return this.currentDrawArea;
    }
    
    public List<D> getVisibleDrawAreas() {
        return this.drawAreas.stream().filter(DrawArea::isVisible).collect((Collector<? super Object, ?, List<D>>)Collectors.toList());
    }
    
    public List<D> getContainingDrawAreas(final double mouseX, final double mouseY) {
        return this.drawAreas.stream().filter(area -> area.contains(mouseX, mouseY)).collect((Collector<? super Object, ?, List<D>>)Collectors.toList());
    }
    
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        if (!this.initialized && this.getContext() != null) {
            this.fillConstellations(this.getContext(), this.drawAreas);
            this.initialized = true;
        }
    }
    
    protected float multiplyStarBrightness(final float pTicks, float brightnessIn) {
        brightnessIn *= Minecraft.func_71410_x().field_71441_e.func_228330_j_(pTicks) * 2.0f;
        return MathHelper.func_76131_a(brightnessIn * (1.0f - Minecraft.func_71410_x().field_71441_e.func_72867_j(pTicks)), 0.0f, 1.0f);
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float partialTicks) {
        if (this.isMouseRotatingGui()) {
            if (func_231173_s_() && Minecraft.func_71410_x().field_71417_B.func_198035_h()) {
                MouseUtil.ungrab();
            }
            if (!func_231173_s_() && !Minecraft.func_71410_x().field_71417_B.func_198035_h()) {
                MouseUtil.grab();
            }
        }
        super.func_230430_a_(renderStack, mouseX, mouseY, partialTicks);
    }
    
    protected void renderDrawnLines(final PoseStack renderStack, final Random rand, final float pTicks) {
        if (!this.canDraw()) {
            this.clearDrawing();
            return;
        }
        final float lineBreadth = 2.0f;
        final Supplier<Float> brightnessFn = () -> RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, 5 + rand.nextInt(10));
        TexturesAS.TEX_STAR_CONNECTION.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormats.field_227851_o_, buf -> {
            this.drawnLines.iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final DrawnLine line = iterator.next();
                this.drawLine(buf, renderStack, pTicks, line.from, line.to, brightnessFn, lineBreadth);
            }
            if (this.dragStart != null && this.dragEnd != null) {
                final Point adjStart = new Point(this.dragStart.x - this.guiLeft, this.dragStart.y - this.guiTop);
                final Point adjEnd = new Point(this.dragEnd.x - this.guiLeft, this.dragEnd.y - this.guiTop);
                this.drawLine(buf, renderStack, pTicks, adjStart, adjEnd, () -> 0.8f, lineBreadth);
            }
        });
    }
    
    private void drawLine(final BufferBuilder buf, final PoseStack renderStack, final float pTicks, final Point from, final Point to, final Supplier<Float> brightnessFn, final float lineBreadth) {
        final float brightness = brightnessFn.get();
        float starBr = this.multiplyStarBrightness(pTicks, brightness);
        if (starBr <= 0.0f) {
            return;
        }
        starBr = starBr * 0.75f + 0.25f;
        final Vector3 fromStar = new Vector3(this.guiLeft + from.getX(), this.guiTop + from.getY(), this.getGuiZLevel());
        final Vector3 toStar = new Vector3(this.guiLeft + to.getX(), this.guiTop + to.getY(), this.getGuiZLevel());
        final Vector3 dir = toStar.clone().subtract(fromStar);
        final Vector3 degLot = dir.clone().crossProduct(Vector3.RotAxis.Z_AXIS).normalize().multiply(lineBreadth);
        final Vector3 vec00 = fromStar.clone().add(degLot);
        final Vector3 vecV = degLot.clone().multiply(-2);
        final Matrix4f offset = renderStack.func_227866_c_().func_227870_a_();
        for (int i = 0; i < 4; ++i) {
            final int u = (i + 1 & 0x2) >> 1;
            final int v = (i + 2 & 0x2) >> 1;
            final Vector3 pos = vec00.clone().add(dir.clone().multiply(u)).add(vecV.clone().multiply(v));
            pos.drawPos(offset, (IVertexBuilder)buf).func_227885_a_(starBr, starBr, starBr, Math.max(0.0f, starBr)).func_225583_a_((float)u, (float)v).func_181675_d();
        }
    }
    
    @Override
    protected void mouseDragStart(final double mouseX, final double mouseY) {
        if (!this.canDraw()) {
            return;
        }
        if (this.currentDrawArea != null && !this.currentDrawArea.contains(mouseX, mouseY)) {
            this.clearDrawing();
        }
        if (this.currentDrawArea == null) {
            this.currentDrawArea = (D)Iterables.getFirst((Iterable)this.getContainingDrawAreas(mouseX, mouseY), (Object)null);
        }
        if (this.currentDrawArea == null) {
            this.clearDrawing();
            return;
        }
        this.dragStart = new Point((int)mouseX, (int)mouseY);
        this.dragEnd = new Point((int)mouseX, (int)mouseY);
    }
    
    @Override
    protected void mouseDragTick(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY, final double mouseOffsetX, final double mouseOffsetY) {
        if (!this.canDraw() || this.dragStart == null || this.currentDrawArea == null) {
            return;
        }
        if (!this.currentDrawArea.contains(mouseX, mouseY)) {
            this.clearDrawing();
            return;
        }
        this.dragEnd = new Point((int)mouseX, (int)mouseY);
    }
    
    @Override
    protected void mouseDragStop(final double mouseX, final double mouseY, final double mouseDiffX, final double mouseDiffY) {
        if (!this.canDraw() || this.dragStart == null || this.currentDrawArea == null) {
            return;
        }
        if (!this.currentDrawArea.contains(mouseX, mouseY)) {
            this.clearDrawing();
            return;
        }
        this.dragEnd = new Point((int)mouseX, (int)mouseY);
        this.finishDrawingLine();
        this.stopCurrentDrawing();
        this.checkConstellationMatch();
    }
    
    protected void finishDrawingLine() {
        if (Math.abs(this.dragStart.getX() - this.dragEnd.getX()) <= 2.0 && Math.abs(this.dragStart.getY() - this.dragEnd.getY()) <= 2.0) {
            return;
        }
        final Point adjStart = new Point(this.dragStart.x - this.guiLeft, this.dragStart.y - this.guiTop);
        final Point adjEnd = new Point(this.dragEnd.x - this.guiLeft, this.dragEnd.y - this.guiTop);
        final DrawnLine l = new DrawnLine(adjStart, adjEnd);
        this.drawnLines.add(l);
    }
    
    protected boolean canDraw() {
        return !Minecraft.func_71410_x().field_71417_B.func_198035_h() && DayTimeHelper.isNight((World)Minecraft.func_71410_x().field_71441_e) && Minecraft.func_71410_x().field_71441_e.func_72867_j(1.0f) <= 0.1f;
    }
    
    protected void clearDrawing() {
        this.currentDrawArea = null;
        this.drawnLines.clear();
        this.stopCurrentDrawing();
    }
    
    private void stopCurrentDrawing() {
        this.dragStart = null;
        this.dragEnd = null;
    }
    
    public boolean func_231177_au__() {
        return false;
    }
    
    private void checkConstellationMatch() {
        if (this.currentDrawArea == null || this.currentDrawArea.cstDisplay.isEmpty()) {
            return;
        }
        final PlayerProgress progress = ResearchHelper.getClientProgress();
        for (final IConstellation cst : this.currentDrawArea.cstDisplay.keySet()) {
            final ConstellationDisplayInformation info = this.currentDrawArea.cstDisplay.get(cst);
            if (!progress.hasConstellationDiscovered(cst)) {
                if (!progress.hasSeenConstellation(cst)) {
                    continue;
                }
                if (cst.getStarConnections().size() != this.drawnLines.size()) {
                    continue;
                }
                if (!cst.canDiscover((Player)Minecraft.func_71410_x().field_71439_g, progress)) {
                    continue;
                }
                boolean didMatch = true;
                for (final StarConnection cstConnection : cst.getStarConnections()) {
                    final Rectangle2D.Float rctFrom = info.frameDrawInformation.get(((BiDiPair<Object, V>)cstConnection).getLeft());
                    final Rectangle2D.Float rctTo = info.frameDrawInformation.get(((BiDiPair<K, Object>)cstConnection).getRight());
                    if (rctFrom == null || rctTo == null) {
                        didMatch = false;
                        break;
                    }
                    if (!this.hasMatchingDrawnLine(rctFrom, rctTo)) {
                        didMatch = false;
                        break;
                    }
                }
                if (didMatch) {
                    PacketChannel.CHANNEL.sendToServer(new PktDiscoverConstellation(cst));
                    this.clearDrawing();
                    return;
                }
                continue;
            }
        }
    }
    
    private boolean hasMatchingDrawnLine(final Rectangle2D.Float rctFrom, final Rectangle2D.Float rctTo) {
        for (final DrawnLine line : this.drawnLines) {
            Point start = line.from;
            Point end = line.to;
            start = new Point(start.x + this.guiLeft, start.y + this.guiTop);
            end = new Point(end.x + this.guiLeft, end.y + this.guiTop);
            if ((rctFrom.contains(start) && rctTo.contains(end)) || (rctTo.contains(start) && rctFrom.contains(end))) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean canObserverSeeSky(final BlockPos pos, final int xzWidth) {
        final World world = (World)Minecraft.func_71410_x().field_71441_e;
        if (world == null) {
            return false;
        }
        for (int xx = -xzWidth; xx <= xzWidth; ++xx) {
            for (int zz = -xzWidth; zz <= xzWidth; ++zz) {
                final BlockPos other = pos.offset(xx, 0, zz);
                if (xx != 0 || zz != 0) {
                    if (!MiscUtils.canSeeSky(world, other, true, false)) {
                        return false;
                    }
                }
            }
        }
        return MiscUtils.canSeeSky(world, pos.above(), true, false);
    }
    
    public static class DrawArea
    {
        protected final Rectangle area;
        protected final Map<IConstellation, ConstellationDisplayInformation> cstDisplay;
        
        public DrawArea(final Rectangle area) {
            this.cstDisplay = new HashMap<IConstellation, ConstellationDisplayInformation>();
            this.area = area;
        }
        
        public void addConstellationToArea(final IConstellation cst, final Point drawPoint, final float size) {
            this.addConstellationToArea(cst, new ConstellationDisplayInformation(drawPoint, size));
        }
        
        public void addConstellationToArea(final IConstellation cst, final ConstellationDisplayInformation info) {
            this.cstDisplay.put(cst, info);
        }
        
        public Map<IConstellation, ConstellationDisplayInformation> getDisplayMap() {
            return Collections.unmodifiableMap((Map<? extends IConstellation, ? extends ConstellationDisplayInformation>)this.cstDisplay);
        }
        
        public boolean contains(final double mouseX, final double mouseY) {
            return this.isVisible() && this.area.contains(mouseX, mouseY);
        }
        
        public boolean isVisible() {
            return true;
        }
    }
    
    public static class ConstellationDisplayInformation
    {
        private final Point renderPosition;
        private final float renderSize;
        private final Map<StarLocation, Rectangle2D.Float> frameDrawInformation;
        
        protected ConstellationDisplayInformation(final Point renderPosition, final float renderSize) {
            this.frameDrawInformation = new HashMap<StarLocation, Rectangle2D.Float>();
            this.renderPosition = renderPosition;
            this.renderSize = renderSize;
        }
        
        public Point getRenderPosition() {
            return this.renderPosition;
        }
        
        public float getRenderSize() {
            return this.renderSize;
        }
        
        public Map<StarLocation, Rectangle2D.Float> getFrameDrawInformation() {
            return this.frameDrawInformation;
        }
    }
    
    private static class DrawnLine
    {
        private final Point from;
        private final Point to;
        
        private DrawnLine(final Point from, final Point to) {
            this.from = from;
            this.to = to;
        }
        
        public Point getFrom() {
            return this.from;
        }
        
        public Point getTo() {
            return this.to;
        }
    }
}
