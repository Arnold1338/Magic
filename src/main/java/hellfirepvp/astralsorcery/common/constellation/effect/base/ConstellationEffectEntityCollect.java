package hellfirepvp.astralsorcery.common.constellation.effect.base;

import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.world.level.entity.Entity;

public abstract class ConstellationEffectEntityCollect<T extends Entity> extends ConstellationEffect
{
    private final Class<T> entityClazz;
    private final Predicate<T> filter;
    
    protected ConstellationEffectEntityCollect(@Nonnull final ILocatable origin, @Nonnull final IWeakConstellation cst, final Class<T> entityClazz, final Predicate<T> filter) {
        super(origin, cst);
        this.filter = filter;
        this.entityClazz = entityClazz;
    }
    
    @Nonnull
    protected List<T> collectEntities(final World world, final BlockPos center, final ConstellationEffectProperties properties) {
        return world.func_175647_a((Class)this.entityClazz, ConstellationEffectEntityCollect.BOX.func_186662_g(properties.getSize()).func_186670_a(center), (Predicate)this.filter);
    }
}
