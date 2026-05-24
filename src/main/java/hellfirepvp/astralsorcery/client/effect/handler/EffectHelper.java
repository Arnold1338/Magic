package hellfirepvp.astralsorcery.client.effect.handler;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import javax.annotation.Nonnull;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.EffectType;
import javax.annotation.Nullable;
import java.util.UUID;
import hellfirepvp.astralsorcery.client.effect.source.FXSource;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public final class EffectHelper
{
    public static <T extends EntityVisualFX, C extends BatchRenderContext<T>> void refresh(final T vfx, final C context) {
        refresh((EntityVisualFX)vfx, (EffectProperties<EntityVisualFX>)of(context));
    }
    
    public static <T extends EntityVisualFX> void refresh(final T vfx, final EffectProperties<T> properties) {
        if (vfx.isRemoved()) {
            EffectRegistrar.registerFX(vfx, properties);
        }
    }
    
    public static <T extends EntityVisualFX, C extends BatchRenderContext<T>> Builder<T> of(final C ctx) {
        return new Builder<T>(ctx);
    }
    
    public static <E extends EntityVisualFX, T extends BatchRenderContext<E>, S extends FXSource<E, T>> S spawnSource(final S src) {
        return EffectRegistrar.registerSource(src);
    }
    
    public static class Builder<T extends EntityVisualFX> extends EffectProperties<T>
    {
        public Builder(final BatchRenderContext<T> ctx) {
            super(ctx);
        }
        
        @Override
        public Builder<T> setOwner(@Nullable final UUID owner) {
            return super.setOwner(owner);
        }
        
        @Override
        public Builder<T> setType(@Nullable final EffectType type) {
            return super.setType(type);
        }
        
        @Override
        public Builder<T> setPosition(@Nonnull final Vector3i position) {
            return super.setPosition(position);
        }
        
        @Override
        public Builder<T> setIgnoreLimit(final boolean ignoreLimit) {
            return super.setIgnoreLimit(ignoreLimit);
        }
        
        public T spawn(@Nonnull final Vector3 spawnPos) {
            this.setPosition((Vector3i)spawnPos.toBlockPos());
            return EffectRegistrar.registerFX(this.getContext().makeParticle(spawnPos), this);
        }
    }
}
