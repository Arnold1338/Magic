package hellfirepvp.astralsorcery.mixin.client;

import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @Inject(method = "getSunAngle", at = @At("RETURN"), cancellable = true)
    public void solarEclipseSunBrightness(float partialTicks, CallbackInfoReturnable<Float> cir) {
        ClientLevel world = (ClientLevel)(Object)this;
        WorldContext ctx = SkyHandler.getContext(world, LogicalSide.CLIENT);
        String strDimKey = world.dimension().location().toString();
        if (ctx != null
                && ((List<?>) RenderingConfig.CONFIG.dimensionsWithSkyRendering.get()).contains(strDimKey)
                && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow()) {
            float perc = ctx.getCelestialEventHandler().getSolarEclipsePercent();
            perc = 0.05f + perc * 0.95f;
            cir.setReturnValue(cir.getReturnValue() * perc);
        }
    }
}
