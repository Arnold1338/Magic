package hellfirepvp.astralsorcery.client.sky;

import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.client.sky.astral.AstralSkyRenderer;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import java.util.List;
import net.minecraft.client.multiplayer.DimensionRenderInfo;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.ISkyRenderHandler;

public class ChainingSkyRenderer implements ISkyRenderHandler
{
    private final ISkyRenderHandler existingSkyRenderer;
    
    public ChainingSkyRenderer(final ISkyRenderHandler existingSkyRenderer) {
        this.existingSkyRenderer = existingSkyRenderer;
    }
    
    public void render(final int ticks, final float partialTicks, final PoseStack renderStack, final ClientLevel world, final Minecraft mc) {
        EventFlags.SKY_RENDERING.executeWithFlag(() -> {
            final RegistryKey dim = world.dimension();
            if (world.func_239132_a_().func_241683_c_() == DimensionRenderInfo.FogType.NORMAL) {
                if (((List)RenderingConfig.CONFIG.dimensionsWithOnlyConstellationRendering.get()).contains(dim.func_240901_a_())) {
                    if (this.existingSkyRenderer != null) {
                        this.existingSkyRenderer.render(ticks, partialTicks, renderStack, world, mc);
                    }
                    else {
                        final ISkyRenderHandler existing = world.func_239132_a_().getSkyRenderHandler();
                        world.func_239132_a_().setSkyRenderHandler((ISkyRenderHandler)null);
                        Minecraft.getInstance().field_71438_f.func_228424_a_(renderStack, partialTicks);
                        world.func_239132_a_().setSkyRenderHandler(existing);
                    }
                    this.renderConstellations(world, renderStack, partialTicks);
                }
                else {
                    AstralSkyRenderer.INSTANCE.render(ticks, partialTicks, renderStack, world, mc);
                }
            }
            else {
                final ISkyRenderHandler existing2 = world.func_239132_a_().getSkyRenderHandler();
                world.func_239132_a_().setSkyRenderHandler((ISkyRenderHandler)null);
                Minecraft.getInstance().field_71438_f.func_228424_a_(renderStack, partialTicks);
                world.func_239132_a_().setSkyRenderHandler(existing2);
            }
        });
    }
    
    private void renderConstellations(final ClientLevel world, final PoseStack renderStack, final float pTicks) {
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        Blending.ADDITIVE_ALPHA.apply();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(false);
        final float alphaSubRain = 1.0f - world.func_72867_j(pTicks);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, alphaSubRain);
        renderStack.popPose();
        renderStack.mulPose(new org.joml.Quaternionf().rotateX((float)Math.toRadians(180.0f)));
        AstralSkyRenderer.renderConstellationsSky(world, renderStack, pTicks);
        renderStack.popPose();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
        RenderSystem.disableTexture();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
}
