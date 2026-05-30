package hellfirepvp.astralsorcery.common.entity.goal;

import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectPelotrio;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.ai.control.MoveControl;
import java.util.List;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraft.world.entity.LivingEntity;

public class SpectralToolMeleeAttackGoal extends SpectralToolGoal
{
    private LivingEntity selectedTarget;
    
    public SpectralToolMeleeAttackGoal(final EntitySpectralTool entity, final double speed) {
        super(entity, speed);
        this.selectedTarget = null;
    }
    
    private LivingEntity findClosestAttackableEntity() {
        final List<LivingEntity> entities = this.getEntity().level().func_175647_a((Class)LivingEntity.class, new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0).func_186662_g(8.0).func_186670_a(this.getEntity().func_233580_cy_()), e -> e != null && e.isAlive() && e.level().func_220339_d() == MobCategory.MONSTER);
        return EntityUtils.selectClosest((Collection<LivingEntity>)entities, entity -> Double.valueOf(entity.func_70032_d((Entity)this.getEntity())));
    }
    
    public boolean func_75250_a() {
        final MoveControl ctrl = this.getEntity().func_70605_aq();
        return !ctrl.func_75640_a() || this.findClosestAttackableEntity() != null;
    }
    
    public boolean func_75253_b() {
        return this.selectedTarget != null;
    }
    
    public void func_75249_e() {
        super.func_75249_e();
        final LivingEntity target = this.findClosestAttackableEntity();
        if (target != null) {
            this.selectedTarget = target;
            this.getEntity().func_70605_aq().func_75642_a(this.selectedTarget.getX(), this.selectedTarget.getY() + this.selectedTarget.func_213302_cg() / 2.0f, this.selectedTarget.getZ(), this.getSpeed());
        }
    }
    
    public void func_75251_c() {
        super.func_75251_c();
        this.selectedTarget = null;
        this.actionCooldown = 0;
    }
    
    public void func_75246_d() {
        super.func_75246_d();
        if (!this.func_75253_b()) {

        }
        if (this.actionCooldown < 0) {
            this.actionCooldown = 0;
        }
        boolean resetTimer = false;
        if (!this.selectedTarget.isAlive()) {
            this.selectedTarget = null;
            resetTimer = true;
        }
        else {
            this.getEntity().func_70605_aq().func_75642_a(this.selectedTarget.getX(), this.selectedTarget.getY() + this.selectedTarget.func_213302_cg() / 2.0f, this.selectedTarget.getZ(), this.getSpeed());
            if (Vector3.atEntityCorner((Entity)this.getEntity()).distanceSquared((Entity)this.selectedTarget) <= 16.0) {
                ++this.actionCooldown;
                if (this.actionCooldown >= (int)MantleEffectPelotrio.CONFIG.ticksPerSwordAttack.get()) {
                    DamageUtil.attackEntityFrom((Entity)this.selectedTarget, CommonProxy.DAMAGE_SOURCE_STELLAR, ((Double)MantleEffectPelotrio.CONFIG.swordDamage.get()).floatValue());
                    resetTimer = true;
                }
            }
        }
        if (resetTimer) {
            this.actionCooldown = 0;
        }
    }
}
