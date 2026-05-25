package hellfirepvp.astralsorcery.client.event;

import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.client.ISkyRenderHandler;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import java.util.List;
import hellfirepvp.astralsorcery.client.sky.ChainingSkyRenderer;
import net.minecraft.client.multiplayer.DimensionRenderInfo;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class SkyRenderEventHandler
{
    public static void onRender(final RenderWorldLastEvent event) {
        final ClientLevel world = Minecraft.getInstance().level;
        if (world != null && world.func_239132_a_().func_241683_c_() == DimensionRenderInfo.FogType.NORMAL) {
            final ISkyRenderHandler render = world.func_239132_a_().getSkyRenderHandler();
            if (!(render instanceof ChainingSkyRenderer)) {
                final String strDimKey = world.dimension().func_240901_a_().toString();
                if (((List)RenderingConfig.CONFIG.dimensionsWithSkyRendering.get()).contains(strDimKey)) {
                    world.func_239132_a_().setSkyRenderHandler((ISkyRenderHandler)new ChainingSkyRenderer(world.func_239132_a_().getSkyRenderHandler()));
                }
            }
        }
    }
    
    public static void onFog(final EntityViewRenderEvent.FogColors event) {
        final ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            final String strDimKey = world.dimension().func_240901_a_().toString();
            if (world.func_239132_a_().func_241683_c_() == DimensionRenderInfo.FogType.NORMAL && ((List)RenderingConfig.CONFIG.dimensionsWithSkyRendering.get()).contains(strDimKey) && !((List)RenderingConfig.CONFIG.dimensionsWithOnlyConstellationRendering.get()).contains(strDimKey) && world.func_239132_a_().getSkyRenderHandler() instanceof ChainingSkyRenderer) {
                final WorldContext ctx = SkyHandler.getContext((Level)world, LogicalSide.CLIENT);
                if (ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow()) {
                    float perc = ctx.getCelestialEventHandler().getSolarEclipsePercent();
                    perc = 0.05f + perc * 0.95f;
                    event.setRed(event.getRed() * perc);
                    event.setGreen(event.getGreen() * perc);
                    event.setBlue(event.getBlue() * perc);
                }
            }
        }
    }
}
