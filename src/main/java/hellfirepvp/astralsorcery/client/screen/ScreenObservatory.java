package hellfirepvp.astralsorcery.client.screen;

import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.container.ContainerTileEntity;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.level.inventory.AbstractContainerMenu;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.screen.base.SkyScreen;
import org.joml.Matrix4f;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.screen.base.WidthHeightScreen;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.options.PointOfView;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import hellfirepvp.astralsorcery.client.screen.telescope.PlayerAngledConstellationInformation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.screen.telescope.FullScreenDrawArea;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;
import net.minecraft.client.gui.IHasContainer;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import hellfirepvp.astralsorcery.client.screen.base.TileConstellationDiscoveryScreen;

public class ScreenObservatory extends TileConstellationDiscoveryScreen<TileObservatory, DrawArea> implements IHasContainer<ContainerObservatory>
{
    private static final Random RAND;
    private static final int FRAME_TEXTURE_SIZE = 16;
    private static final int randomStars = 220;
    private final List<Point2D.Float> usedStars;
    private final ContainerObservatory container;
    
    public ScreenObservatory(final ContainerObservatory container) {
        super(((ContainerTileEntity<BlockEntity>)container).getTileEntity(), Minecraft.getInstance().func_228018_at_().func_198087_p() - 32, Minecraft.getInstance().func_228018_at_().func_198107_o() - 32);
        this.usedStars = new ArrayList<Point2D.Float>(220);
        this.container = container;
        final Player player = (Player)Minecraft.getInstance().player;
        if (player != null) {
            final TileObservatory observatory = ((TileConstellationDiscoveryScreen<TileObservatory, D>)this).getTile();
            player.xRot = observatory.observatoryPitch;
            player.field_70127_C = observatory.prevObservatoryPitch;
            player.yRot = observatory.observatoryYaw;
            player.field_70126_B = observatory.prevObservatoryYaw;
        }
    }
    
    public ContainerObservatory getContainer() {
        return this.container;
    }
    
    @Nonnull
    protected List<DrawArea> createDrawAreas() {
        return Lists.newArrayList((Object[])new DrawArea[] { new FullScreenDrawArea() });
    }
    
    protected void fillConstellations(final WorldContext ctx, final List<DrawArea> drawAreas) {
        final DrawArea area = drawAreas.get(0);
        final Random gen = ctx.getDayRandom();
        final Map<IConstellation, Point2D.Float> placed = new HashMap<IConstellation, Point2D.Float>();
        for (final IConstellation cst : ctx.getActiveCelestialsHandler().getActiveConstellations()) {
            Point2D.Float foundPoint;
            do {
                foundPoint = this.tryEmptyPlace(placed.values(), gen);
            } while (foundPoint == null);
            area.addConstellationToArea(cst, new PlayerAngledConstellationInformation(150.0f, foundPoint.y, foundPoint.x));
            placed.put(cst, foundPoint);
        }
        for (int i = 0; i < 220; ++i) {
            this.usedStars.add(new Point2D.Float(16.0f + gen.nextFloat() * this.getGuiWidth(), 16.0f + gen.nextFloat() * this.getGuiHeight()));
        }
    }
    
    private Point2D.Float tryEmptyPlace(final Collection<Point2D.Float> placed, final Random gen) {
        double constellationGap = 12.0;
        constellationGap = Math.sqrt(constellationGap * constellationGap * 2.0);
        final float rPitch = -25.0f + gen.nextFloat() * -50.0f;
        final float rYaw = gen.nextFloat() * 360.0f;
        for (final Point2D.Float point : placed) {
            if (point.distance(rPitch, rYaw) <= constellationGap || point.distance(rPitch, rYaw - 360.0f) <= constellationGap) {
                return null;
            }
        }
        return new Point2D.Float(rPitch, rYaw);
    }
    
    public void init() {
        super.init();
        EventFlags.GUI_CLOSING.executeWithFlag(() -> Minecraft.getInstance().player.giveExperienceLevels();
    }
    
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableDepthTest();
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        Minecraft.getInstance().options.func_243229_a(PointOfView.FIRST_PERSON);
        final double guiFactor = Minecraft.getInstance().func_228018_at_().func_198100_s();
        GL11.glEnable(3089);
        GL11.glScissor(Mth.func_76128_c(14.0 * guiFactor), Mth.func_76128_c(14.0 * guiFactor), Mth.func_76128_c((this.getGuiWidth() + 2) * guiFactor), Mth.func_76128_c((this.getGuiHeight() + 2) * guiFactor));
        this.drawObservatoryScreen(renderStack, pTicks);
        GL11.glDisable(3089);
        this.drawFrame(renderStack);
    }
    
    private void drawObservatoryScreen(final PoseStack renderStack, final float pTicks) {
        final boolean canSeeSky = this.canObserverSeeSky(((TileConstellationDiscoveryScreen<TileObservatory, D>)this).getTile().getBlockState(), 2);
        final double guiFactor = Minecraft.getInstance().func_228018_at_().func_198100_s();
        final float pitch = Minecraft.getInstance().player.func_195050_f(pTicks);
        float angleOpacity = 0.0f;
        if (pitch < -30.0f) {
            angleOpacity = 1.0f;
        }
        else if (pitch <= -9.0f) {
            angleOpacity = 0.2f + 0.8f * ((Math.abs(pitch) - 10.0f) / 20.0f);
            angleOpacity = Mth.func_76129_c(angleOpacity);
        }
        final float brMultiplier = angleOpacity;
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        this.func_230926_e_(-10);
        this.drawSkyBackground(renderStack, pTicks, canSeeSky, angleOpacity);
        if (!this.isInitialized()) {
            this.func_230926_e_(0);
            Blending.DEFAULT.apply();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            return;
        }
        float playerYaw = Minecraft.getInstance().player.yRot % 360.0f;
        if (playerYaw < 0.0f) {
            playerYaw += 360.0f;
        }
        if (playerYaw >= 180.0f) {
            playerYaw -= 360.0f;
        }
        final float playerPitch = Minecraft.getInstance().player.xRot;
        final float rainBr = 1.0f - Minecraft.getInstance().level.func_72867_j(pTicks);
        final WorldContext ctx = SkyHandler.getContext((Level)Minecraft.getInstance().level, LogicalSide.CLIENT);
        if (ctx != null && canSeeSky) {
            final Random gen = ctx.getDayRandom();
            this.func_230926_e_(-9);
            TexturesAS.TEX_STAR_1.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> {
                this.usedStars.iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final Point2D.Float star = iterator3.next();
                    final float size2 = 3.0f + gen.nextFloat() * 3.0f;
                    final float brightness = 0.4f + RenderingConstellationUtils.stdFlicker(ClientScheduler.getClientTick(), pTicks, 10 + gen.nextInt(20)) * 0.5f;
                    final float brightness2 = this.multiplyStarBrightness(pTicks, brightness);
                    final float brightness3 = brightness2 * brMultiplier;
                    RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(16.0f + star.x, 16.0f + star.y).dim(size2, size2).color(brightness3, brightness3, brightness3, brightness3).draw();
                }
                return;
            });
            this.func_230926_e_(-7);
            for (final DrawArea area : this.getVisibleDrawAreas()) {
                for (final IConstellation cst : area.getDisplayMap().keySet()) {
                    final ConstellationDisplayInformation info = area.getDisplayMap().get(cst);
                    info.getFrameDrawInformation().clear();
                    if (!(info instanceof PlayerAngledConstellationInformation)) {
                        continue;
                    }
                    final PlayerAngledConstellationInformation cstInfo = (PlayerAngledConstellationInformation)info;
                    final float size = cstInfo.getRenderSize();
                    float diffYaw = playerYaw - cstInfo.getYaw();
                    final float diffPitch = playerPitch - cstInfo.getPitch();
                    if ((Math.abs(diffYaw) > size && Math.abs(diffYaw += 360.0f) > size) || Math.abs(diffPitch) > size) {
                        continue;
                    }
                    final int wPart = Mth.func_76141_d(this.getGuiWidth() * 0.1f);
                    final int hPart = Mth.func_76141_d(this.getGuiHeight() * 0.1f);
                    final float xFactor = diffYaw / 8.0f;
                    final float yFactor = diffPitch / 8.0f;
                    final Map<StarLocation, Rectangle2D.Float> cstRenderInfo = RenderingConstellationUtils.renderConstellationIntoGUI(cst, renderStack, (float)(this.getGuiLeft() + wPart + Mth.func_76128_c(xFactor / guiFactor * this.getGuiWidth())), (float)(this.getGuiTop() + hPart + Mth.func_76128_c(yFactor / guiFactor * this.getGuiHeight())), (float)this.getGuiZLevel(), (float)Mth.func_76141_d(this.getGuiHeight() * 0.6f), (float)Mth.func_76141_d(this.getGuiHeight() * 0.6f), 2.0, () -> (0.2f + 0.7f * RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, 5 + gen.nextInt(15)) * rainBr) * brMultiplier, ResearchHelper.getClientProgress().hasConstellationDiscovered(cst), true);
                    cstInfo.getFrameDrawInformation().putAll(cstRenderInfo);
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
    
    private void drawFrame(final PoseStack renderStack) {
        this.func_230926_e_(10);
        TexturesAS.TEX_GUI_OBSERVATORY.bindTexture();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> {
            final Matrix4f offset = renderStack.last().pose();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(0.0f, 0.0f).dim(16.0f, 16.0f).tex(0.0f, 0.0f, 0.4f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at((float)(this.getGuiWidth() + 16), 0.0f).dim(16.0f, 16.0f).tex(0.4f, 0.0f, 0.4f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at((float)(this.getGuiWidth() + 16), (float)(this.getGuiHeight() + 16)).dim(16.0f, 16.0f).tex(0.4f, 0.4f, 0.4f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(0.0f, (float)(this.getGuiHeight() + 16)).dim(16.0f, 16.0f).tex(0.0f, 0.4f, 0.4f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(16.0f, 0.0f).dim((float)this.getGuiWidth(), 16.0f).tex(0.8f, 0.0f, 0.05f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at((float)(this.getGuiWidth() + 16), 16.0f).dim(16.0f, (float)this.getGuiHeight()).tex(0.0f, 0.85f, 0.4f, 0.05f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(16.0f, (float)(this.getGuiHeight() + 16)).dim((float)this.getGuiWidth(), 16.0f).tex(0.85f, 0.0f, 0.05f, 0.4f).draw();
            RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, this).at(0.0f, 16.0f).dim(16.0f, (float)this.getGuiHeight()).tex(0.0f, 0.8f, 0.4f, 0.05f).draw();
            return;
        });
        this.func_230926_e_(0);
    }
    
    private void drawSkyBackground(final PoseStack renderStack, final float pTicks, final boolean canSeeSky, final float angleOpacity) {
        final Tuple<Color, Color> rgbFromTo = SkyScreen.getSkyGradient(canSeeSky, angleOpacity, pTicks);
        RenderingDrawUtils.drawGradientRect(renderStack, (float)this.getGuiZLevel(), (float)this.guiLeft, (float)this.guiTop, (float)(this.guiLeft + this.guiWidth), (float)(this.guiTop + this.guiHeight), ((Color)rgbFromTo.getA()).getRGB(), ((Color)rgbFromTo.getB()).getRGB());
    }
    
    public void func_212927_b(final double xPos, final double yPos) {
        if (!Minecraft.getInstance().field_71417_B.func_198035_h()) {
            return;
        }
        final int offsetX = 6;
        final int offsetY = 6;
        final int width = this.guiWidth - 12;
        final int height = this.guiHeight - 12;
        final Minecraft mc = Minecraft.getInstance();
        final double xDiff = mc.field_71417_B.func_198024_e() - xPos / (mc.func_228018_at_().func_198107_o() / (double)mc.func_228018_at_().func_198105_m());
        double yDiff = mc.field_71417_B.func_198026_f() - yPos / (mc.func_228018_at_().func_198087_p() / (double)mc.func_228018_at_().func_198083_n());
        final float pitch = Minecraft.getInstance().player.xRot;
        if (pitch <= -89.99f && yDiff > 0.0) {
            yDiff = 0.0;
        }
        if (pitch >= -10.0f) {
            Minecraft.getInstance().player.xRot = -10.0f;
            yDiff = 0.0;
        }
        if (pitch <= -75.0f) {
            Minecraft.getInstance().player.xRot = -75.0f;
            yDiff = 0.0;
        }
        for (final Point2D.Float float1 : this.usedStars) {
            final Point2D.Float sl = float1;
            float1.x -= (float)xDiff;
            final Point2D.Float float2 = sl;
            float2.y += (float)yDiff;
            if (sl.x < offsetX) {
                final Point2D.Float float3 = sl;
                float3.x += width;
            }
            else if (sl.x > offsetX + width) {
                final Point2D.Float float4 = sl;
                float4.x -= width;
            }
            if (sl.y < offsetY) {
                final Point2D.Float float5 = sl;
                float5.y += height;
            }
            else {
                if (sl.y <= offsetY + height) {
                    continue;
                }
                final Point2D.Float float6 = sl;
                float6.y -= height;
            }
        }
    }
    
    public boolean func_231177_au__() {
        return false;
    }
    
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
    
    static {
        RAND = new Random();
    }
}
