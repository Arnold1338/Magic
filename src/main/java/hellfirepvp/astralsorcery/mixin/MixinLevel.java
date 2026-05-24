package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class MixinLevel {
    @Shadow private int skyDarken;

    @Inject(method = "calculateSkyLightSources", at = @At("RETURN"), cancellable = true)
    public void solarEclipseSunBrightnessServer(CallbackInfo ci) {
        Level world = (Level)(Object)this;
        WorldContext ctx = SkyHandler.getContext(world);
        if (ctx != null && ctx.getCelestialEventHandler().getSolarEclipse().isActiveNow()) {
            this.skyDarken = 11 - Math.round(ctx.getCelestialEventHandler().getSolarEclipsePercent() * 11.0f);
        }
    }
}
