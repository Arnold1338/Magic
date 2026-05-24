package hellfirepvp.astralsorcery.client.effect.handler;

import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.effect.source.FXSource;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;

public final class EffectRegistrar
{
    private EffectRegistrar() {
    }
    
    static <T extends EntityVisualFX> T registerFX(final T entityComplexFX, final EffectProperties<T> properties) {
        register(entityComplexFX, properties);
        return entityComplexFX;
    }
    
    static <E extends EntityVisualFX, T extends BatchRenderContext<E>, F extends FXSource<E, T>> F registerSource(final F source) {
        register(source);
        return source;
    }
    
    private static void register(final FXSource<?, ?> src) {
        if (Minecraft.func_71410_x().func_147113_T() || Minecraft.func_71410_x().field_71439_g == null) {
            return;
        }
        if (!Thread.currentThread().getName().contains("Render thread")) {
            AstralSorcery.getProxy().scheduleClientside(() -> register(src));
            return;
        }
        EffectHandler.getInstance().queueSource(src);
    }
    
    private static <T extends EntityVisualFX> void register(final T effect, final EffectProperties<T> properties) {
        if (AssetLibrary.isReloading() || effect == null || Minecraft.func_71410_x().func_147113_T() || Minecraft.func_71410_x().field_71439_g == null || !RenderingUtils.canEffectExist(effect)) {
            return;
        }
        if (!Thread.currentThread().getName().contains("Render thread")) {
            AstralSorcery.getProxy().scheduleClientside(() -> register(effect, properties));
            return;
        }
        EffectHandler.getInstance().queueParticle(new EffectHandler.PendingEffect(effect, properties));
    }
}
