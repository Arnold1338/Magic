package hellfirepvp.astralsorcery.client.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.screen.base.SkyScreen;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;
import hellfirepvp.astralsorcery.client.screen.telescope.PlayerAngledConstellationInformation;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import java.awt.geom.Point2D;
import java.util.List;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;

public class ScreenHandTelescope extends ConstellationDiscoveryScreen<DrawArea>
{
    private static final int randomStars = 40;
    private final List<Point2D.Float> usedStars;
    
    public ScreenHandTelescope() {
        super((Component)new Component("screen.astralsorcery.hand_telescope"), 216, 216);
        this.usedStars = new ArrayList<Point2D.Float>(40);
    }
    
    @Nonnull
    @Override
    protected List<DrawArea> createDrawAreas() {
        return Lists.newArrayList((Object[])new DrawArea[] { new DrawArea(this.getGuiBox()) });
    }
    
    @Override
    protected void fillConstellations(final WorldContext ctx, final List<DrawArea> drawAreas) {
        final Random gen = ctx.getDayRandom();
        final Set<IConstellation> used = new HashSet<IConstellation>();
        for (final DrawArea area : drawAreas) {
            final Collection<IConstellation> available = Lists.newArrayList((Iterable)ctx.getActiveCelestialsHandler().getActiveConstellations());
            available.removeIf(c -> !(c instanceof IMajorConstellation) || used.contains(c) || !c.canDiscover((Player)Minecraft.getInstance().player, ResearchHelper.getClientProgress()));
            final IConstellation cst = MiscUtils.getRandomEntry(available, gen);
            if (cst instanceof IMajorConstellation) {
                used.add(cst);
                final float yaw = gen.nextFloat() * 360.0f - 180.0f;
                final float pitch = -90.0f + gen.nextFloat() * 25.0f;
                area.addConstellationToArea(cst, new PlayerAngledConstellationInformation(150.0f, yaw, pitch));
            }
        }
        final int offsetX = 6;
        final int offsetY = 6;
        final int width = this.guiWidth - 6;
        final int height = this.guiHeight - 6;
        for (int i = 0; i < 40; ++i) {
            this.usedStars.add(new Point2D.Float(offsetX + gen.nextFloat() * width, offsetY + gen.nextFloat() * height));
        }
    }
    
    @Override
    public void func_230430_a_(final PoseStack renderStack, final int mouseX, final int mouseY, final float pTicks) {
        RenderSystem.enableDepthTest();
        super.func_230430_a_(renderStack, mouseX, mouseY, pTicks);
        this.drawWHRect(renderStack, TexturesAS.TEX_GUI_HAND_TELESCOPE);
        this.drawTelescopeCell(renderStack, pTicks);
    }
    
    private void drawTelescopeCell(final PoseStack renderStack, final float pTicks) {
        final boolean canSeeSky = this.canObserverSeeSky(Minecraft.getInstance().player.func_233580_cy_(), 1);
        final float pitch = Minecraft.getInstance().player.func_195050_f(pTicks);
        float angleOpacity = 0.0f;
        if (pitch < -60.0f) {
            angleOpacity = 1.0f;
        }
        else if (pitch < -10.0f) {
            angleOpacity = (Math.abs(pitch) - 10.0f) / 50.0f;
            if (DayTimeHelper.isNight((Level)Minecraft.getInstance().level)) {
                angleOpacity *= angleOpacity;
            }
        }
        final float brMultiplier = angleOpacity;
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.disableAlphaTest();
        this.func_230926_e_(-10);
        this.drawSkyBackground(renderStack, pTicks, canSeeSky, angleOpacity);
        if (!this.isInitialized()) {
            this.func_230926_e_(0);
            RenderSystem.enableAlphaTest();
            Blending.DEFAULT.apply();
            RenderSystem.disableBlend();
            return;
        }
        final WorldContext ctx = SkyHandler.getContext((Level)Minecraft.getInstance().level, LogicalSide.CLIENT);
        if (ctx != null && canSeeSky) {
            final Random gen = ctx.getDayRandom();
            final double guiFactor = Minecraft.getInstance().func_228018_at_().func_198100_s();
            float playerYaw = Minecraft.getInstance().player.yRot % 360.0f;
            if (playerYaw < 0.0f) {
                playerYaw += 360.0f;
            }
            if (playerYaw >= 180.0f) {
                playerYaw -= 360.0f;
            }
            final float playerPitch = Minecraft.getInstance().player.xRot;
            this.func_230926_e_(-9);
            final float starSize = 5.0f;
            TexturesAS.TEX_STAR_1.bindTexture();
            RenderingUtils.draw(7, DefaultVertexFormat.fogColor, buf -> {
                this.usedStars.iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final Point2D.Float pos = iterator3.next();
                    final float brightness = 0.4f + RenderingConstellationUtils.stdFlicker(ClientScheduler.getClientTick(), pTicks, 10 + gen.nextInt(20)) * 0.5f;
                    final float brightness2 = this.multiplyStarBrightness(pTicks, brightness);
                    final float brightness3 = brightness2 * brMultiplier;
                    RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, pos.x + this.getGuiLeft(), pos.y + this.getGuiTop(), (float)this.getGuiZLevel(), starSize, starSize).color(brightness3, brightness3, brightness3, brightness3).draw();
                }
                return;
            });
            this.func_230926_e_(-7);
            for (final DrawArea areas : this.getVisibleDrawAreas()) {
                for (final IConstellation cst : areas.getDisplayMap().keySet()) {
                    final ConstellationDisplayInformation info = areas.getDisplayMap().get(cst);
                    info.getFrameDrawInformation().clear();
                    if (!(info instanceof PlayerAngledConstellationInformation)) {
                        continue;
                    }
                    final PlayerAngledConstellationInformation cstInfo = (PlayerAngledConstellationInformation)info;
                    final float diffYaw = playerYaw - cstInfo.getYaw();
                    final float diffPitch = playerPitch - cstInfo.getPitch();
                    final float maxDistance = 35.0f;
                    if ((Math.abs(diffYaw) > maxDistance && Math.abs(playerYaw + 360.0f) > maxDistance) || Math.abs(diffPitch) > maxDistance) {
                        continue;
                    }
                    final float rainBr = 1.0f - Minecraft.getInstance().level.func_72867_j(pTicks);
                    final int wPart = Mth.func_76141_d(this.getGuiWidth() * 0.1f);
                    final int hPart = Mth.func_76141_d(this.getGuiHeight() * 0.1f);
                    final float xFactor = diffYaw / 8.0f;
                    final float yFactor = diffPitch / 8.0f;
                    GL11.glEnable(3089);
                    GL11.glScissor(Mth.func_76128_c((this.getGuiLeft() + 5) * guiFactor), Mth.func_76128_c((this.getGuiTop() + 5) * guiFactor), Mth.func_76128_c((this.getGuiWidth() - 10) * guiFactor), Mth.func_76128_c((this.getGuiHeight() - 10) * guiFactor));
                    final Map<StarLocation, Rectangle2D.Float> cstRenderInfo = RenderingConstellationUtils.renderConstellationIntoGUI(cst.getTierRenderColor(), cst, renderStack, (float)(this.getGuiLeft() + wPart + Mth.func_76128_c(xFactor / guiFactor * this.getGuiWidth())), (float)(this.getGuiTop() + hPart + Mth.func_76128_c(yFactor / guiFactor * this.getGuiHeight())), (float)this.getGuiZLevel(), (float)(this.getGuiWidth() - Mth.func_76141_d(wPart * 1.5f)), (float)(this.getGuiHeight() - Mth.func_76141_d(hPart * 1.5f)), 2.0, () -> (0.3f + 0.7f * RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, 5 + gen.nextInt(15))) * rainBr * brMultiplier, ResearchHelper.getClientProgress().hasConstellationDiscovered(cst), true);
                    GL11.glDisable(3089);
                    info.getFrameDrawInformation().putAll(cstRenderInfo);
                }
            }
            this.func_230926_e_(-5);
            this.renderDrawnLines(renderStack, gen, pTicks);
        }
        this.func_230926_e_(0);
        RenderSystem.enableAlphaTest();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
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
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.func_195050_f(1.0f) <= -89.99f && yDiff > 0.0) {
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
    
    private void drawSkyBackground(final PoseStack renderStack, final float pTicks, final boolean canSeeSky, final float angleOpacity) {
        final Tuple<Color, Color> rgbFromTo = SkyScreen.getSkyGradient(canSeeSky, angleOpacity, pTicks);
        RenderingDrawUtils.drawGradientRect(renderStack, (float)this.getGuiZLevel(), (float)(this.guiLeft + 4), (float)(this.guiTop + 4), (float)(this.guiLeft + this.guiWidth - 8), (float)(this.guiTop + this.guiHeight - 8), ((Color)rgbFromTo.getA()).getRGB(), ((Color)rgbFromTo.getB()).getRGB());
    }
    
    @Override
    public boolean func_231177_au__() {
        return false;
    }
    
    @Override
    protected boolean shouldRightClickCloseScreen(final double mouseX, final double mouseY) {
        return true;
    }
}
