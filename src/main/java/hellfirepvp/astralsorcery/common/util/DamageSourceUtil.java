package hellfirepvp.astralsorcery.common.util;

import java.util.function.Consumer;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.EntityDamageSource;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.damagesource.DamageSource;
import javax.annotation.Nonnull;

public class DamageSourceUtil
{
    public static DamageSource newType(@Nonnull final String damageType) {
        return new DamageSource(damageType);
    }
    
    public static DamageSource withEntityDirect(@Nonnull final String damageType, @Nullable final Entity source) {
        return (DamageSource)new EntityDamageSource(damageType, source);
    }
    
    public static DamageSource withEntityIndirect(@Nonnull final String damageType, @Nullable final Entity actualSource, @Nullable final Entity indirectSource) {
        return (DamageSource)new IndirectEntityDamageSource(damageType, indirectSource, actualSource);
    }
    
    @Nullable
    public static DamageSource withEntityDirect(@Nonnull final DamageSource damageType, @Nullable final Entity source) {
        return override(damageType, source, null);
    }
    
    @Nullable
    public static DamageSource withEntityIndirect(@Nonnull final DamageSource damageType, @Nullable final Entity actualSource, @Nullable final Entity indirectSource) {
        return override(damageType, indirectSource, actualSource);
    }
    
    @Nullable
    public static DamageSource setToFireDamage(@Nonnull final DamageSource src) {
        return changeAttribute(src, DamageSource::func_76361_j);
    }
    
    @Nullable
    public static DamageSource setToBypassArmor(@Nonnull final DamageSource src) {
        return changeAttribute(src, DamageSource::func_76348_h);
    }
    
    @Nullable
    public static DamageSource changeAttribute(@Nonnull final DamageSource src, final Consumer<DamageSource> update) {
        return overrideWithChanges(src, update);
    }
    
    private static boolean mayChangeAttributes(final DamageSource src) {
        final Class<?> srcClass = src.getClass();
        return srcClass.equals(DamageSource.class) || srcClass.equals(EntityDamageSource.class) || srcClass.equals(IndirectEntityDamageSource.class);
    }
    
    @Nullable
    private static DamageSource overrideWithChanges(@Nonnull final DamageSource source, final Consumer<DamageSource> run) {
        final DamageSource dst = override(source, null, null);
        if (dst != null) {
            run.accept(dst);
        }
        return dst;
    }
    
    @Nullable
    private static DamageSource override(final DamageSource src, @Nullable final Entity directSource, @Nullable final Entity trueSource) {
        if (!mayChangeAttributes(src)) {
            return null;
        }
        DamageSource dst;
        if (src.getClass().equals(DamageSource.class)) {
            dst = new DamageSource(src.func_76355_l());
        }
        else if (src.getClass().equals(EntityDamageSource.class)) {
            dst = (DamageSource)new EntityDamageSource(src.func_76355_l(), (directSource != null) ? directSource : src.func_76364_f());
        }
        else {
            dst = (DamageSource)new IndirectEntityDamageSource(src.func_76355_l(), (directSource != null) ? directSource : src.func_76364_f(), (trueSource != null) ? trueSource : ((directSource != null) ? directSource : src.getEnchantments());
        }
        copy(src, dst);
        return dst;
    }
    
    private static void copy(final DamageSource src, final DamageSource dest) {
        if (src.func_76357_e()) {
            dest.func_76359_i();
        }
        if (src.func_151517_h()) {
            dest.func_151518_m();
        }
        if (src.func_76352_a()) {
            dest.func_76349_b();
        }
        if (src.func_94541_c()) {
            dest.func_94540_d();
        }
        if (src.func_76347_k()) {
            dest.func_76361_j();
        }
        if (src.func_82725_o()) {
            dest.func_82726_p();
        }
        if (src.func_76350_n()) {
            dest.func_76351_m();
        }
    }
}
