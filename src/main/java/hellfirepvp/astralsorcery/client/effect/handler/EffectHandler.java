package hellfirepvp.astralsorcery.client.effect.handler;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import java.io.IOException;
import net.minecraft.world.entity.Entity;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.util.order.DependencySorter;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import net.minecraft.client.Minecraft;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import java.util.Iterator;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import hellfirepvp.astralsorcery.client.resource.AssetLibrary;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.Counter;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import java.util.Map;
import hellfirepvp.astralsorcery.client.effect.source.FXSource;
import hellfirepvp.observerlib.common.util.AlternatingSet;
import java.util.List;
import java.util.Random;

public final class EffectHandler
{
    private static final Random STATIC_EFFECT_RAND;
    private static final EffectHandler INSTANCE;
    private boolean cleanRequested;
    private boolean acceptsNewEffects;
    private final List<PendingEffect> toAddBuffer;
    private final AlternatingSet<FXSource<?, ?>> sources;
    private final Map<BatchRenderContext<?>, List<PendingEffect>> effectMap;
    private List<BatchRenderContext<?>> orderedEffects;
    
    private EffectHandler() {
        this.cleanRequested = false;
        this.acceptsNewEffects = false;
        this.toAddBuffer = Lists.newLinkedList();
        this.sources = (AlternatingSet<FXSource<?, ?>>)new AlternatingSet();
        this.effectMap = Maps.newLinkedHashMap();
        this.orderedEffects = null;
    }
    
    public static EffectHandler getInstance() {
        return EffectHandler.INSTANCE;
    }
    
    public int getEffectCount() {
        final Counter c = new Counter(0);
        this.effectMap.values().stream().flatMap((Function<? super List<PendingEffect>, ? extends Stream<?>>)Collection::stream).forEach(p -> c.increment());
        return c.getValue();
    }
    
    public void render(final PoseStack renderStack, final float pTicks) {
        if (this.orderedEffects == null || AssetLibrary.isReloading()) {
            return;
        }
        final IDrawRenderTypeBuffer drawBuffer = IDrawRenderTypeBuffer.defaultBuffer();
        this.acceptsNewEffects = false;
        for (final BatchRenderContext<?> ctx : this.orderedEffects) {
            final List<PendingEffect> effects = this.effectMap.get(ctx);
            if (!effects.isEmpty()) {
                ctx.renderAll(effects, renderStack, drawBuffer, pTicks);
            }
        }
        this.acceptsNewEffects = true;
    }
    
    void tick() throws IOException {
        if (this.cleanRequested) {
            this.toAddBuffer.clear();
            this.sources.clear();
            this.effectMap.values().forEach(effects -> effects.stream().map(PendingEffect::getEffect).forEach(EntityComplexFX::flagAsRemoved));
            this.effectMap.values().forEach(List::clear);
            this.cleanRequested = false;
        }
        Entity rView = Minecraft.func_71410_x().func_175606_aa();
        if (rView == null) {
            rView = (Entity)Minecraft.func_71410_x().field_71439_g;
        }
        if (rView == null) {
            cleanUp();
            return;
        }
        if (this.orderedEffects == null) {
            (this.orderedEffects = DependencySorter.getSorted(EffectTemplatesAS.LIST_ALL_RENDER_CONTEXT)).forEach(ctx -> {
                final List list = this.effectMap.put(ctx, new ArrayList<PendingEffect>());
                return;
            });
        }
        this.acceptsNewEffects = false;
        this.effectMap.values().forEach(l -> {
            final Iterator iterator = l.iterator();
            while (iterator.hasNext()) {
                final PendingEffect effect = (PendingEffect)iterator.next();
                final EntityComplexFX fx = effect.getEffect();
                fx.tick();
                if (fx.canRemove()) {
                    iterator.remove();
                    fx.flagAsRemoved();
                }
            }
            return;
        });
        this.sources.forEach(src -> {
            src.tick();
            src.tickSpawnFX(new EffectRegistrar(src));
            if (src.canRemove()) {
                src.flagAsRemoved();
                return false;
            }
            return true;
        });
        this.acceptsNewEffects = true;
        this.toAddBuffer.forEach(this::registerUnsafe);
        this.toAddBuffer.clear();
    }
    
    void queueSource(final FXSource<?, ?> source) {
        this.sources.add((Object)source);
    }
    
    void queueParticle(final PendingEffect pendingEffect) {
        if (this.acceptsNewEffects) {
            this.registerUnsafe(pendingEffect);
        }
        else {
            this.toAddBuffer.add(pendingEffect);
        }
    }
    
    private void registerUnsafe(final PendingEffect pendingEffect) {
        if (!this.mayAcceptParticle(pendingEffect.getProperties())) {
            return;
        }
        final EntityVisualFX effect = pendingEffect.getEffect();
        final BatchRenderContext<?> ctx = pendingEffect.getProperties().getContext();
        pendingEffect.getProperties().applySpecialEffects(effect);
        this.effectMap.get(ctx).add(pendingEffect);
        effect.setActive();
    }
    
    private boolean mayAcceptParticle(final EffectProperties<?> properties) {
        if (properties.ignoresSpawnLimit()) {
            return true;
        }
        RenderingConfig.ParticleAmount cfg = (RenderingConfig.ParticleAmount)RenderingConfig.CONFIG.particleAmount.get();
        if (!Minecraft.func_71375_t()) {
            cfg = cfg.less();
        }
        return cfg.shouldSpawn(EffectHandler.STATIC_EFFECT_RAND);
    }
    
    public static void cleanUp() {
        getInstance().cleanRequested = true;
    }
    
    static {
        STATIC_EFFECT_RAND = new Random();
        INSTANCE = new EffectHandler();
    }
    
    private static class EffectRegistrar<E extends EntityVisualFX> implements Function<Vector3, E>
    {
        private final FXSource<E, ?> source;
        
        private EffectRegistrar(final FXSource<E, ?> source) {
            this.source = source;
        }
        
        @Override
        public E apply(final Vector3 pos) {
            final EffectHelper.Builder<E> prop = this.source.generateFX();
            final E fx = prop.getContext().makeParticle(pos);
            final PendingEffect effect = new PendingEffect(fx, prop);
            EffectHandler.getInstance().registerUnsafe(effect);
            return fx;
        }
    }
    
    public static class PendingEffect
    {
        private final EntityVisualFX effect;
        private final EffectProperties<?> runProperties;
        
        PendingEffect(final EntityVisualFX effect, final EffectProperties<?> runProperties) {
            this.effect = effect;
            this.runProperties = runProperties;
        }
        
        EffectProperties<?> getProperties() {
            return this.runProperties;
        }
        
        public EntityVisualFX getEffect() {
            return this.effect;
        }
    }
}
