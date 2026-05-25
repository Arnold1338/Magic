package hellfirepvp.astralsorcery.common.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

// In 1.20.1, DamageSource is a record - it cannot be mutated or subclassed.
// Damage type customization is now done through the DamageType registry system.
// These methods are kept for API compatibility but have limited functionality.
public class DamageSourceUtil {

    // In 1.20.1 DamageSource is registered via data. For now return the original source.
    @Nullable
    public static DamageSource withEntityDirect(@Nonnull final DamageSource damageType, @Nullable final Entity source) {
        return damageType;
    }

    @Nullable
    public static DamageSource withEntityIndirect(@Nonnull final DamageSource damageType,
                                                   @Nullable final Entity actualSource,
                                                   @Nullable final Entity indirectSource) {
        return damageType;
    }

    @Nullable
    public static DamageSource setToFireDamage(@Nonnull final DamageSource src) {
        return src;
    }

    @Nullable
    public static DamageSource setToBypassArmor(@Nonnull final DamageSource src) {
        return src;
    }

    @Nullable
    public static DamageSource changeAttribute(@Nonnull final DamageSource src,
                                               final Consumer<DamageSource> update) {
        return src;
    }
}
