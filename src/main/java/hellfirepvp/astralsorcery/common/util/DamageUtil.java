package hellfirepvp.astralsorcery.common.util;

import net.minecraft.world.entity.LivingEntity;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.Entity;

public class DamageUtil
{
    public static boolean attackEntityFrom(@Nonnull final Entity attacked, @Nonnull final DamageSource type, final float amount) {
        return attacked.hurt(type, amount);
    }
    
    public static boolean attackEntityFrom(@Nonnull final Entity attacked, @Nonnull final DamageSource type, final float amount, @Nullable final Entity newSource) {
        final DamageSource newType = DamageSourceUtil.withEntityDirect(type, newSource);
        return attackEntityFrom(attacked, (newType != null) ? newType : type, amount);
    }
    
    public static <T extends LivingEntity> void shotgunAttack(final T targeted, final Consumer<T> fn) {
        final int hurtTime = targeted.field_70172_ad;
        targeted.field_70172_ad = 0;
        try {
            fn.accept(targeted);
        }
        finally {
            targeted.field_70172_ad = hurtTime;
        }
    }
}
