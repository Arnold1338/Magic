package hellfirepvp.astralsorcery.client.effect.source;

import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public abstract class FXSource<E extends EntityVisualFX, T extends BatchRenderContext<E>> extends EntityComplexFX
{
    private final T ctx;
    
    public FXSource(final Vector3 pos, final T template) {
        super(pos);
        this.ctx = template;
    }
    
    public abstract void tickSpawnFX(final Function<Vector3, E> p0);
    
    public abstract void populateProperties(final EffectProperties<E> p0);
    
    public final EffectHelper.Builder<E> generateFX() {
        final EffectHelper.Builder<E> propBuilder = EffectHelper.of(this.ctx);
        this.populateProperties(propBuilder);
        return propBuilder;
    }
}
