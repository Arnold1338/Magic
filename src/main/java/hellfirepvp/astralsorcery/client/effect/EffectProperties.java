package hellfirepvp.astralsorcery.client.effect;

import com.google.common.collect.Lists;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;

public class EffectProperties<T extends EntityVisualFX>
{
    private final BatchRenderContext<T> ctx;
    private static List<Consumer<EntityVisualFX>> specialEffects;
    private EffectType type;
    private UUID owner;
    private Vector3i position;
    private boolean ignoreLimit;
    
    public EffectProperties(final BatchRenderContext<T> ctx) {
        this.type = null;
        this.owner = null;
        this.position = Vector3i.field_177959_e;
        this.ignoreLimit = false;
        this.ctx = ctx;
    }
    
    public <I extends EffectProperties<T>> I setOwner(@Nullable final UUID owner) {
        this.owner = owner;
        return (I)this;
    }
    
    public <I extends EffectProperties<T>> I setType(@Nullable final EffectType type) {
        this.type = type;
        return (I)this;
    }
    
    public <I extends EffectProperties<T>> I setPosition(@Nonnull final Vector3i position) {
        this.position = position;
        return (I)this;
    }
    
    public <I extends EffectProperties<T>> I setIgnoreLimit(final boolean ignoreLimit) {
        this.ignoreLimit = ignoreLimit;
        return (I)this;
    }
    
    public BatchRenderContext<T> getContext() {
        return this.ctx;
    }
    
    @Nullable
    public UUID getOwner() {
        return this.owner;
    }
    
    @Nullable
    public EffectType getType() {
        return this.type;
    }
    
    @Nonnull
    public Vector3i getPosition() {
        return this.position;
    }
    
    public boolean ignoresSpawnLimit() {
        return this.ignoreLimit;
    }
    
    public void applySpecialEffects(final EntityVisualFX effect) {
        EffectProperties.specialEffects.forEach(s -> s.accept(effect));
    }
    
    public static void addSpecialEffect(final Consumer<EntityVisualFX> effect) {
        EffectProperties.specialEffects.add(effect);
    }
    
    static {
        EffectProperties.specialEffects = Lists.newArrayList();
    }
}
