package hellfirepvp.astralsorcery.client.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.play.client.PktRotateTelescope;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;
import java.awt.Point;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.Random;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Collections;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.screen.base.NavigationArrowScreen;
import hellfirepvp.astralsorcery.client.screen.base.SkyScreen;
import hellfirepvp.astralsorcery.client.screen.telescope.TelescopeRotationDrawArea;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.client.screen.base.TileConstellationDiscoveryScreen;

public class ScreenTelescope extends TileConstellationDiscoveryScreen<TileTelescope, TelescopeRotationDrawArea> implements SkyScreen, NavigationArrowScreen
{
    private TileTelescope.TelescopeRotation rotation;
    private Rectangle rectArrowCW;
    private Rectangle rectArrowCCW;
    
    public ScreenTelescope(final TileTelescope telescope) {
        super(telescope, 280, 280);
        this.rectArrowCW = null;
        this.rectArrowCCW = null;
        this.rotation = telescope.getRotation();
    }
    
    @Nonnull
    @Override
    protected List<TelescopeRotationDrawArea> createDrawAreas() {
        final List<TelescopeRotationDrawArea> areas = new LinkedList<TelescopeRotationDrawArea>();
        for (final TileTelescope.TelescopeRotation r : TileTelescope.TelescopeRotation.values()) {
            areas.add(new TelescopeRotationDrawArea(this, r, this.getGuiBox()));
        }
        return areas;
    }
    
    @Override
    protected void fillConstellations(final WorldContext ctx, final List<TelescopeRotationDrawArea> drawAreas) {
        final Random gen = ctx.getDayRandom();
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        List<IWeakConstellation> cst = new ArrayList<IWeakConstellation>();
        for (final IConstellation active : ctx.getActiveCelestialsHandler().getCurrentRenderPositions().keySet()) {
            if (active instanceof IWeakConstellation && active.canDiscover((Player)Minecraft.getInstance().player, prog)) {
                cst.add((IWeakConstellation)active);
            }
        }
        Collections.shuffle(cst, gen);
        cst = cst.subList(0, Math.min(drawAreas.size(), cst.size()));
        for (final IWeakConstellation constellation : cst) {
            Point foundPoint;
            TelescopeRotationDrawArea associatedArea;
            do {
                associatedArea = MiscUtils.getRandomEntry(drawAreas, gen);
                foundPoint = this.findEmptySpace(gen, associatedArea);
            } while (foundPoint == null);
            associatedArea.addConstellationToArea(constellation, foundPoint, 150.0f);
        }
    }
    
    private Point findEmptySpace(final Random rand, final TelescopeRotationDrawArea area) {
        final int size = 150;
        final int wdh = this.guiWidth - 6 - size;
        final int hgt = this.guiHeight - 6 - size;
        final int rX = 6 + rand.nextInt(wdh);
        final int rY = 6 + rand.nextInt(hgt);
        final Rectangle constellationRect = new Rectangle(rX, rY, size, size);
        for (final ConstellationDisplayInformation info : area.getDisplayMap().values()) {
            final Point offset = info.getRenderPosition();
            final Rectangle otherRect = new Rectangle(offset.x, offset.y, size, size);
            if (otherRect.intersects(constellationRect)) {
                return null;
            }
        }
        return new Point(rX, rY);
    }
    
    @Override
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableDepthTest();
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.drawWHRect(renderStack, TexturesAS.TEX_GUI_TELESCOPE);
        this.drawConstellationCell(renderStack, pTicks);
        this.drawNavArrows(renderStack, mouseX, mouseY, pTicks);
    }
    
    private void drawNavArrows(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        this.rectArrowCCW = this.drawArrow(renderStack, this.guiLeft - 40, this.guiTop + this.guiHeight / 2, this.getGuiZLevel(), Type.LEFT, mouseX, mouseY, pTicks);
        this.rectArrowCW = this.drawArrow(renderStack, this.guiLeft + this.guiWidth + 10, this.guiTop + this.guiHeight / 2, this.getGuiZLevel(), Type.RIGHT, mouseX, mouseY, pTicks);
        RenderSystem.disableBlend();
    }
    
    private void drawConstellationCell(final PoseStack renderStack, final float pTicks) {
        final boolean canSeeSky = this.canObserverSeeSky(((TileConstellationDiscoveryScreen<TileTelescope, D>)this).getTile().getBlockState(), 1);
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        this.func_230926_e_(-10);
        this.drawSkyBackground(renderStack, pTicks, canSeeSky);
        if (!this.isInitialized()) {
            this.func_230926_e_(0);
            Blending.DEFAULT.apply();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            return;
        }
        final WorldContext ctx = this.getContext();
        if (ctx != null && canSeeSky) {
            final Random gen = ctx.getDayRandom();
            final PlayerProgress prog = ResearchHelper.getClientProgress();
            for (int i = 0; i < this.rotation.ordinal(); ++i) {
                gen.nextFloat();
            }
            this.func_230926_e_(-9);
            final float starSize = 5.0f;
            TexturesAS.TEX_STAR_1.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> {
                for (int j = 0; j < 72 + gen.nextInt(108); ++j) {
                    final float innerOffsetX = starSize + gen.nextFloat() * (this.guiWidth - starSize * 2.0f) + this.getGuiLeft();
                    final float innerOffsetY = starSize + gen.nextFloat() * (this.guiHeight - starSize * 2.0f) + this.getGuiTop();
                    final float brightness = 0.4f + RenderingConstellationUtils.stdFlicker(ClientScheduler.getClientTick(), pTicks, 10 + gen.nextInt(20)) * 0.5f;
                    final float brightness2 = this.multiplyStarBrightness(pTicks, brightness);
                    RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(innerOffsetX, innerOffsetY).dim(starSize, starSize).color(brightness2, brightness2, brightness2, brightness2).draw();
                }
                return;
            });
            this.func_230926_e_(-7);
            for (final TelescopeRotationDrawArea area : this.getVisibleDrawAreas()) {
                for (final IConstellation cst : area.getDisplayMap().keySet()) {
                    final ConstellationDisplayInformation info = area.getDisplayMap().get(cst);
                    info.getFrameDrawInformation().clear();
                    final Point pos = info.getRenderPosition();
                    final int size = (int)info.getRenderSize();
                    final float rainBr = 1.0f - Minecraft.getInstance().level.func_72867_j(pTicks);
                    final Map<StarLocation, Rectangle2D.Float> cstRenderInfo = RenderingConstellationUtils.renderConstellationIntoGUI(cst, renderStack, (float)(pos.x + this.guiLeft), (float)(pos.y + this.guiTop), (float)this.getGuiZLevel(), (float)size, (float)size, 2.5, () -> RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, 5 + gen.nextInt(15)) * rainBr, prog.hasConstellationDiscovered(cst), true);
                    info.getFrameDrawInformation().putAll(cstRenderInfo);
                }
            }
            this.func_230926_e_(-5);
            this.renderDrawnLines(renderStack, gen, pTicks);
        }
        this.func_230926_e_(0);
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
    
    private void drawSkyBackground(final PoseStack renderStack, final float pTicks, final boolean canSeeSky) {
        final Tuple<Color, Color> rgbFromTo = SkyScreen.getSkyGradient(canSeeSky, 1.0f, pTicks);
        RenderingDrawUtils.drawGradientRect(renderStack, (float)this.getGuiZLevel(), (float)(this.guiLeft + 5), (float)(this.guiTop + 5), (float)(this.guiLeft + this.guiWidth - 5), (float)(this.guiTop + this.guiHeight - 5), ((Color)rgbFromTo.getA()).getRGB(), ((Color)rgbFromTo.getB()).getRGB());
    }
    
    @Override
    public boolean func_231044_a_(final double mouseX, final double mouseY, final int button) {
        if (super.func_231044_a_(mouseX, mouseY, button)) {
            return true;
        }
        final Point p = new Point((int)mouseX, (int)mouseY);
        if (this.rectArrowCW != null && this.rectArrowCW.contains(p)) {
            final PktRotateTelescope pkt = new PktRotateTelescope(true, (RegistryKey<Level>)((TileConstellationDiscoveryScreen<TileTelescope, D>)this).getTile().getLevel().dimension(), ((TileConstellationDiscoveryScreen<TileTelescope, D>)this).getTile().getBlockState());
            PacketChannel.CHANNEL.sendToServer(pkt);
            return true;
        }
        if (this.rectArrowCCW != null && this.rectArrowCCW.contains(p)) {
            final PktRotateTelescope pkt = new PktRotateTelescope(false, (RegistryKey<Level>)((TileConstellationDiscoveryScreen<TileTelescope, D>)this).getTile().getLevel().dimension(), ((TileConstellationDiscoveryScreen<TileTelescope, D>)this).getTile().getBlockState());
            PacketChannel.CHANNEL.sendToServer(pkt);
            return true;
        }
        return false;
    }
    
    public void handleRotationChange(final boolean isClockwise) {
        this.rotation = (isClockwise ? this.rotation.nextClockWise() : this.rotation.nextCounterClockWise());
        this.clearDrawing();
    }
    
    public TileTelescope.TelescopeRotation getRotation() {
        return this.rotation;
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    @Override
    protected boolean isMouseRotatingGui() {
        return false;
    }
}
