package hellfirepvp.astralsorcery.client.sky.astral;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import java.util.Iterator;
import java.util.Map;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.constellation.world.ActiveCelestialsHandler;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import org.joml.Vector3d;
import org.joml.Vector3f;
import hellfirepvp.astralsorcery.client.util.Blending;
import net.minecraft.client.renderer.FogRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import java.util.LinkedList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.util.List;
import hellfirepvp.astralsorcery.client.util.BatchedVertexList;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ISkyRenderHandler;

@OnlyIn(Dist.CLIENT)
public class AstralSkyRenderer implements ISkyRenderHandler
{
    private static final Random RAND;
    private static final ResourceLocation REF_TEX_MOON_PHASES;
    private static final ResourceLocation REF_TEX_SUN;
    public static AstralSkyRenderer INSTANCE;
    private final BatchedVertexList sky;
    private final BatchedVertexList skyHorizon;
    private final List<StarDrawList> starLists;
    private boolean initialized;
    
    private AstralSkyRenderer() {
        this.sky = new BatchedVertexList(DefaultVertexFormat.field_181705_e);
        this.skyHorizon = new BatchedVertexList(DefaultVertexFormat.field_181705_e);
        this.starLists = new LinkedList<StarDrawList>();
        this.initialized = false;
    }
    
    public void reset() {
        this.sky.reset();
        this.skyHorizon.reset();
        this.starLists.forEach(BatchedVertexList::reset);
        this.starLists.clear();
        this.initialized = false;
    }
    
    private void initialize() {
        this.sky.batch(AstralSkyRendererSetup::generateSky);
        this.skyHorizon.batch(AstralSkyRendererSetup::generateSkyHorizon);
        for (int i = 0; i < 20; ++i) {
            final AbstractRenderableTexture starTexture = (i % 2 == 0) ? TexturesAS.TEX_STAR_1 : TexturesAS.TEX_STAR_2;
            final int flicker = 12 + AstralSkyRenderer.RAND.nextInt(5);
            final StarDrawList starList = new StarDrawList(starTexture, flicker);
            starList.batch(buf -> AstralSkyRendererSetup.generateStars(buf, 60 + AstralSkyRenderer.RAND.nextInt(60), 1.1f + AstralSkyRenderer.RAND.nextFloat() * 0.3f));
            this.starLists.add(starList);
        }
        this.initialized = true;
    }
    
    public void render(final int ticks, final float pTicks, final PoseStack renderStack, final ClientLevel world, final Minecraft mc) {
        if (AssetLibrary.isReloading()) {
            return;
        }
        if (!this.initialized) {
            this.initialize();
        }
        final Vector3d color = world.func_228318_a_(mc.field_71460_t.func_215316_n().func_216780_d(), pTicks);
        float skyR = (float)color.field_72450_a;
        float skyG = (float)color.field_72448_b;
        float skyB = (float)color.field_72449_c;
        final WorldContext ctx = SkyHandler.getContext((World)world, LogicalSide.CLIENT);
        if (ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow()) {
            float perc = ctx.getCelestialEventHandler().getSolarEclipsePercent();
            perc = 0.05f + perc * 0.95f;
            skyR *= perc;
            skyG *= perc;
            skyB *= perc;
        }
        RenderSystem.disableTexture();
        FogRenderer.func_228373_b_();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color4f(skyR, skyG, skyB, 1.0f);
        this.sky.render(renderStack);
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderSystem.shadeModel(7425);
        final float[] duskDawnColors = world.func_239132_a_().func_230492_a_(world.func_242415_f(pTicks), pTicks);
        if (duskDawnColors != null) {
            this.renderDuskDawn(duskDawnColors, renderStack, world, pTicks);
        }
        RenderSystem.shadeModel(7424);
        RenderSystem.enableTexture();
        Blending.ADDITIVE_ALPHA.apply();
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).func_229187_a_(-90.0f));
        renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).func_229187_a_(world.func_242415_f(pTicks) * 360.0f));
        this.renderCelestials(world, renderStack, pTicks);
        this.renderStars(world, renderStack, pTicks);
        renderStack.scale();
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).func_229187_a_(180.0f));
        renderConstellationsSky(world, renderStack, pTicks);
        renderStack.scale();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        RenderSystem.disableTexture();
        RenderSystem.color4f(0.0f, 0.0f, 0.0f, 1.0f);
        final double horizonDiff = Minecraft.getInstance().player.func_174824_e(pTicks).field_72448_b - world.func_72912_H().func_239159_f_();
        if (horizonDiff < 0.0) {
            renderStack.popPose();
            renderStack.func_227861_a_(0.0, 12.0, 0.0);
            this.skyHorizon.render(renderStack);
            renderStack.scale();
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }
    
    public static void renderConstellationsSky(final ClientLevel world, final PoseStack renderStack, final float pTicks) {
        final WorldContext ctx = SkyHandler.getContext((World)world, LogicalSide.CLIENT);
        if (ctx == null) {
            return;
        }
        final int dayLength = (int)GeneralConfig.CONFIG.dayLength.get();
        final long wTime = (world.func_72820_D() % dayLength + dayLength) % dayLength;
        if (wTime < dayLength / 2.0f) {
            return;
        }
        final float rainDim = 1.0f - world.func_72867_j(pTicks);
        final float brightness = world.func_228330_j_(pTicks) * rainDim;
        if (brightness <= 0.0f) {
            return;
        }
        final Random gen = ctx.getRandom();
        final PlayerProgress clientProgress = ResearchHelper.getClientProgress();
        final Map<IConstellation, ActiveCelestialsHandler.RenderPosition> constellations = ctx.getActiveCelestialsHandler().getCurrentRenderPositions();
        for (final IConstellation cst : constellations.keySet()) {
            if (clientProgress.hasConstellationDiscovered(cst)) {
                if (!ctx.getConstellationHandler().isActiveCurrently(cst, MoonPhase.fromWorld((IWorld)world))) {
                    continue;
                }
                final ActiveCelestialsHandler.RenderPosition pos = constellations.get(cst);
                RenderingConstellationUtils.renderConstellationSky(cst, renderStack, pos, () -> RenderingConstellationUtils.conCFlicker(ClientScheduler.getClientTick(), pTicks, 10 + gen.nextInt(5)) * brightness * 1.25f);
            }
        }
    }
    
    private void renderStars(final ClientLevel world, final PoseStack renderStack, final float pTicks) {
        final float starBrightness = world.func_228330_j_(pTicks) * (1.0f - world.func_72867_j(pTicks));
        if (starBrightness > 0.0f) {
            this.starLists.forEach(list -> {
                final float br = RenderingConstellationUtils.stdFlicker(ClientScheduler.getClientTick(), pTicks, list.flickerSpeed) * starBrightness;
                RenderSystem.color4f(starBrightness, starBrightness, starBrightness, br);
                list.render(renderStack);
                return;
            });
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    private void renderCelestials(final ClientLevel world, final PoseStack renderStack, final float pTicks) {
        final WorldContext ctx = SkyHandler.getContext((World)world, LogicalSide.CLIENT);
        final float rainAlpha = 1.0f - world.func_72867_j(pTicks);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, rainAlpha);
        if (ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow()) {
            this.renderSolarEclipseSun(renderStack, ctx);
        }
        else {
            this.renderSun(renderStack);
        }
        if (ctx != null && ctx.getCelestialEventHandler().getLunarEclipse().isActiveNow()) {
            final int lunarHalf = ctx.getCelestialEventHandler().getLunarEclipse().getEventDuration() / 2;
            float eclTick = ctx.getCelestialEventHandler().getLunarEclipse().getEffectTick(0.0f);
            if (eclTick >= lunarHalf) {
                eclTick -= lunarHalf;
            }
            else {
                eclTick = lunarHalf - eclTick;
            }
            final float perc = eclTick / lunarHalf;
            RenderSystem.color4f(1.0f, 0.4f + 0.6f * perc, 0.4f + 0.6f * perc, rainAlpha);
            this.renderMoon(renderStack, (World)world);
        }
        else {
            this.renderMoon(renderStack, (World)world);
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderSolarEclipseSun(final PoseStack renderStack, final WorldContext ctx) {
        final float sunSize = 30.0f;
        final float eclipseTick = ctx.getCelestialEventHandler().getSolarEclipse().getEffectTick(0.0f);
        final float part = ctx.getCelestialEventHandler().getSolarEclipse().getEventDuration() / 7.0f;
        float u = 0.0f;
        for (float tick = eclipseTick; tick - part > 0.0f; tick -= part, ++u) {}
        final float uOffset = u;
        TexturesAS.TEX_SOLAR_ECLIPSE.bindTexture();
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).func_229187_a_(-90.0f));
        final Matrix4f matr = renderStack.last().func_227870_a_();
        RenderingUtils.draw(7, DefaultVertexFormat.field_181707_g, buf -> {
            buf.vertex(matr, -sunSize, 100.0f, -sunSize).func_225583_a_(uOffset / 7.0f, 0.0f).endVertex();
            buf.vertex(matr, sunSize, 100.0f, -sunSize).func_225583_a_((uOffset + 1.0f) / 7.0f, 0.0f).endVertex();
            buf.vertex(matr, sunSize, 100.0f, sunSize).func_225583_a_((uOffset + 1.0f) / 7.0f, 1.0f).endVertex();
            buf.vertex(matr, -sunSize, 100.0f, sunSize).func_225583_a_(uOffset / 7.0f, 1.0f).endVertex();
            return;
        });
        renderStack.scale();
    }
    
    private void renderSun(final PoseStack renderStack) {
        final float sunSize = 30.0f;
        final Matrix4f matr = renderStack.last().func_227870_a_();
        Minecraft.getInstance().func_110434_K().func_110577_a(AstralSkyRenderer.REF_TEX_SUN);
        RenderingUtils.draw(7, DefaultVertexFormat.field_181707_g, buf -> {
            buf.vertex(matr, -sunSize, 100.0f, -sunSize).func_225583_a_(0.0f, 0.0f).endVertex();
            buf.vertex(matr, sunSize, 100.0f, -sunSize).func_225583_a_(1.0f, 0.0f).endVertex();
            buf.vertex(matr, sunSize, 100.0f, sunSize).func_225583_a_(1.0f, 1.0f).endVertex();
            buf.vertex(matr, -sunSize, 100.0f, sunSize).func_225583_a_(0.0f, 1.0f).endVertex();
        });
    }
    
    private void renderMoon(final PoseStack renderStack, final World world) {
        final float moonSize = 20.0f;
        final int moonPhase = world.func_242414_af();
        final int i = moonPhase % 4;
        final int j = moonPhase / 4 % 2;
        final float minU = i / 4.0f;
        final float minV = j / 2.0f;
        final float maxU = (i + 1) / 4.0f;
        final float maxV = (j + 1) / 2.0f;
        final Matrix4f matr = renderStack.last().func_227870_a_();
        Minecraft.getInstance().func_110434_K().func_110577_a(AstralSkyRenderer.REF_TEX_MOON_PHASES);
        RenderingUtils.draw(7, DefaultVertexFormat.field_181707_g, buf -> {
            buf.vertex(matr, -moonSize, -100.0f, moonSize).func_225583_a_(maxU, maxV).endVertex();
            buf.vertex(matr, moonSize, -100.0f, moonSize).func_225583_a_(minU, maxV).endVertex();
            buf.vertex(matr, moonSize, -100.0f, -moonSize).func_225583_a_(minU, minV).endVertex();
            buf.vertex(matr, -moonSize, -100.0f, -moonSize).func_225583_a_(maxU, minV).endVertex();
        });
    }
    
    private void renderDuskDawn(final float[] duskDawnColors, final PoseStack renderStack, final ClientLevel world, final float pTicks) {
        final float f3 = (Mth.func_76126_a(world.func_72929_e(pTicks)) < 0.0f) ? 180.0f : 0.0f;
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Vector3f(1, 0, 0).func_229187_a_(90.0f));
        renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).func_229187_a_(f3));
        renderStack.mulPose(new org.joml.Vector3f(0, 0, 1).func_229187_a_(90.0f));
        final float r = duskDawnColors[0];
        final float g = duskDawnColors[1];
        final float b = duskDawnColors[2];
        final float a = duskDawnColors[3];
        RenderingUtils.draw(6, DefaultVertexFormat.POSITION_COLOR, buf -> {
            buf.func_225582_a_(0.0, 100.0, 0.0).color(r, g, b, a).endVertex();
            for (int i = 0; i <= 16; ++i) {
                final float f4 = i * 6.2831855f / 16.0f;
                final float f5 = Mth.func_76126_a(f4);
                final float f6 = Mth.func_76134_b(f4);
                buf.func_225582_a_((double)(f5 * 120.0f), (double)(f6 * 120.0f), (double)(-f6 * 40.0f * a)).color(r, g, b, 0.0f).endVertex();
            }
            return;
        });
        renderStack.scale();
    }
    
    static {
        RAND = new Random();
        REF_TEX_MOON_PHASES = new ResourceLocation("textures/environment/moon_phases.png");
        REF_TEX_SUN = new ResourceLocation("textures/environment/sun.png");
        AstralSkyRenderer.INSTANCE = new AstralSkyRenderer();
    }
    
    private static class StarDrawList extends BatchedVertexList
    {
        private final AbstractRenderableTexture texture;
        private final int flickerSpeed;
        
        private StarDrawList(final AbstractRenderableTexture texture, final int flickerSpeed) {
            super(DefaultVertexFormat.field_181707_g);
            this.texture = texture;
            this.flickerSpeed = flickerSpeed;
        }
        
        @Override
        public void render(final PoseStack renderStack) {
            this.texture.bindTexture();
            super.render(renderStack);
        }
    }
}
