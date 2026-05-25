package hellfirepvp.astralsorcery.common.entity.technical;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.world.level.phys.EntityHitResult;
import net.minecraft.world.level.phys.HitResult;
import com.google.common.collect.Lists;
import java.util.Collections;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.phys.AABB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperDamageCancelling;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.world.entity.projectile.ThrowableEntity;

public class EntityGrapplingHook extends ThrowableEntity implements IEntityAdditionalSpawnData
{
    private static final EntityDataAccessor<Integer> PULLING_ENTITY;
    private static final EntityDataAccessor<Boolean> PULLING;
    private boolean launchedThrower;
    private int timeout;
    private int previousDist;
    public int despawning;
    public float pullFactor;
    private LivingEntity throwingEntity;
    
    public EntityGrapplingHook(final Level world) {
        super((EntityType)EntityTypesAS.GRAPPLING_HOOK, world);
        this.launchedThrower = false;
        this.timeout = 0;
        this.previousDist = 0;
        this.despawning = -1;
        this.pullFactor = 0.0f;
    }
    
    public EntityGrapplingHook(final LivingEntity thrower, final Level world) {
        super((EntityType)EntityTypesAS.GRAPPLING_HOOK, thrower, world);
        this.launchedThrower = false;
        this.timeout = 0;
        this.previousDist = 0;
        this.despawning = -1;
        this.pullFactor = 0.0f;
        this.shoot(Vector3.directionFromYawPitch(thrower.yRot, thrower.xRot), 1.5f);
        this.throwingEntity = thrower;
    }
    
    public static EntityType.IFactory<EntityGrapplingHook> factory() {
        return (EntityType.IFactory<EntityGrapplingHook>)((spawnEntity, world) -> new EntityGrapplingHook(world));
    }
    
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a((EntityDataAccessor)EntityGrapplingHook.PULLING, (Object)false);
        this.field_70180_af.func_187214_a((EntityDataAccessor)EntityGrapplingHook.PULLING_ENTITY, (Object)(-1));
    }
    
    public void setPulling(final boolean pull, @Nullable final LivingEntity hit) {
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntityGrapplingHook.PULLING, (Object)pull);
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntityGrapplingHook.PULLING_ENTITY, (Object)((hit == null) ? -1 : hit.func_145782_y()));
    }
    
    public boolean isPulling() {
        return (boolean)this.field_70180_af.func_187225_a((EntityDataAccessor)EntityGrapplingHook.PULLING);
    }
    
    @Nullable
    public LivingEntity getPulling() {
        final int idPull = (int)this.field_70180_af.func_187225_a((EntityDataAccessor)EntityGrapplingHook.PULLING_ENTITY);
        if (idPull > 0) {
            try {
                return (LivingEntity)this.level().getEntityById(idPull);
            }
            catch (final Exception ex) {}
        }
        return null;
    }
    
    public float despawnPercentage(final float partial) {
        float p = this.despawning - (1.0f - partial);
        p /= 10.0f;
        return Mth.canEnchant(p, 0.0f, 1.0f);
    }
    
    public boolean isDespawning() {
        return this.despawning != -1;
    }
    
    private void setDespawning() {
        if (this.despawning == -1) {
            this.despawning = 0;
        }
    }
    
    private void despawnTick() {
        ++this.despawning;
        if (this.despawning > 10) {
            this.func_70106_y();
        }
    }
    
    @Nullable
    public Entity func_234616_v_() {
        return (Entity)((this.throwingEntity != null) ? this.throwingEntity : super.func_234616_v_());
    }
    
    protected float func_70185_h() {
        return this.isPulling() ? 0.0f : 0.03f;
    }
    
    public void func_70071_h_() {
        super.tick();
        if (this.func_234616_v_() == null || !this.func_234616_v_().isAlive()) {
            this.setDespawning();
        }
        if (!this.isPulling() && this.field_70173_aa >= 30) {
            this.setDespawning();
        }
        if (this.level()) {
            if (!this.isPulling()) {
                this.pullFactor += 0.02f;
            }
            else {
                this.pullFactor *= 0.7f;
            }
        }
        if (this.isDespawning()) {
            this.despawnTick();
            if (this.level() && this.despawning == 3) {
                this.playDespawnSparkles();
            }
        }
        else {
            final Entity thrower = this.func_234616_v_();
            final double dist = Math.max(0.01, thrower.func_70032_d((Entity)this));
            if (this.isAlive() && this.isPulling()) {
                if (this.getPulling() != null) {
                    final LivingEntity at = this.getPulling();
                    this.setPos(at.getX(), at.getY(), at.getZ());
                }
                if ((this.getPulling() != null && this.field_70173_aa > 60 && dist < 2.0) || (this.getPulling() == null && this.field_70173_aa > 15 && dist < 2.0) || this.timeout > 15) {
                    this.setDespawning();
                }
                else {
                    thrower.field_70143_R = -2.0f;
                    double mx = this.getX() - thrower.getX();
                    double my = this.getY() - thrower.getY();
                    double mz = this.getZ() - thrower.getZ();
                    mx /= dist * 5.0;
                    my /= dist * 5.0;
                    mz /= dist * 5.0;
                    Vec3 v2 = new Vec3(mx, my, mz);
                    if (v2.func_72433_c() > 0.25) {
                        v2 = v2.add();
                        mx = v2.field_72450_a / 4.0;
                        my = v2.field_72448_b / 4.0;
                        mz = v2.field_72449_c / 4.0;
                    }
                    Vec3 motion = thrower.func_213322_ci();
                    motion = motion.func_72441_c(mx, my + 0.03999999910593033, mz);
                    if (!this.launchedThrower) {
                        motion = motion.func_72441_c(0.0, 0.4000000059604645, 0.0);
                        this.launchedThrower = true;
                    }
                    thrower.func_213317_d(motion);
                    if (thrower instanceof Player) {
                        EventHelperDamageCancelling.markInvulnerableToNextDamage((Player)thrower, DamageSource.field_76379_h);
                    }
                    final int roughDst = (int)(dist / 2.5);
                    if (roughDst >= this.previousDist) {
                        ++this.timeout;
                    }
                    else {
                        this.timeout = 0;
                    }
                    this.previousDist = roughDst;
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playDespawnSparkles() {
        if (!this.isPulling()) {
            final Vector3 ePos = RenderingVectorUtils.interpolatePosition((Entity)this, 1.0f);
            final List<Vector3> positions = this.buildLine(1.0f);
            for (final Vector3 pos : positions) {
                if (this.random.nextBoolean()) {
                    final Vector3 motion = Vector3.random().multiply(0.005f);
                    final Vector3 at = pos.add(ePos);
                    final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setScaleMultiplier(0.3f + this.random.nextFloat() * 0.3f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE)).setMotion(motion).setMaxAge(25 + this.random.nextInt(20));
                    if (!this.random.nextBoolean()) {
                        continue;
                    }
                    p.color(VFXColorFunction.WHITE);
                }
            }
        }
    }
    
    public void writeSpawnData(final FriendlyByteBuf buffer) {
        int id = -1;
        if (this.throwingEntity != null) {
            id = this.throwingEntity.func_145782_y();
        }
        buffer.writeInt(id);
    }
    
    public void readSpawnData(final FriendlyByteBuf additionalData) {
        final int id = additionalData.readInt();
        try {
            if (id > 0) {
                this.throwingEntity = (LivingEntity)this.level().getEntityById(id);
            }
        }
        catch (final Exception ex) {}
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean func_70112_a(final double distance) {
        double d0 = this.func_174813_aQ().func_72320_b() * 64.0;
        if (Double.isNaN(d0)) {
            d0 = 64.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    public AABB func_184177_bl() {
        return BlockEntity.INFINITE_EXTENT_AABB;
    }
    
    public List<Vector3> buildLine(final float partial) {
        final Entity thrower = this.func_234616_v_();
        if (thrower == null) {
            return Collections.emptyList();
        }
        final List<Vector3> list = Lists.newLinkedList();
        final Vector3 interpThrower = RenderingVectorUtils.interpolatePosition(thrower, partial);
        final Vector3 interpHook = RenderingVectorUtils.interpolatePosition((Entity)this, partial);
        final Vector3 origin = new Vector3();
        final Vector3 to = interpThrower.clone().subtract(interpHook).addY(thrower.func_213302_cg() / 4.0f);
        final float lineLength = (float)(to.length() * 5.0);
        list.add(origin.clone());
        for (int iter = (int)lineLength, xx = 1; xx < iter - 1; ++xx) {
            final float dist = xx * (lineLength / iter);
            final double dx = (interpThrower.getX() - interpHook.getX()) / iter * xx + Mth.func_76126_a(dist / 10.0f) * this.pullFactor;
            final double dy = (interpThrower.getY() - interpHook.getY() + thrower.func_213302_cg() / 2.0f) / iter * xx + Mth.func_76126_a(dist / 7.0f) * this.pullFactor;
            final double dz = (interpThrower.getZ() - interpHook.getZ()) / iter * xx + Mth.func_76126_a(dist / 2.0f) * this.pullFactor;
            list.add(new Vector3(dx, dy, dz));
        }
        list.add(to.clone());
        return list;
    }
    
    public void shoot(final Vector3 dir, final float velocity) {
        super.func_70186_c(dir.getX(), dir.getY(), dir.getZ(), velocity, 0.0f);
    }
    
    public void func_70186_c(final double x, final double y, final double z, final float velocity, final float inaccuracy) {
        super.func_70186_c(x, y, z, velocity, 0.0f);
    }
    
    protected void func_70227_a(final HitResult result) {
        Vec3 hit = result.func_216347_e();
        switch (result.func_216346_c()) {
            case BLOCK: {
                this.setPulling(true, null);
                break;
            }
            case ENTITY: {
                final Entity e = ((EntityHitResult)result).func_216348_a();
                if (!(e instanceof LivingEntity) || (this.func_234616_v_() != null && e.equals((Object)this.func_234616_v_()))) {
                    return;
                }
                this.setPulling(true, (LivingEntity)((EntityHitResult)result).func_216348_a());
                hit = new Vec3(hit.field_72450_a, hit.field_72448_b + ((EntityHitResult)result).func_216348_a().func_213302_cg() * 3.0f / 4.0f, hit.field_72449_c);
                break;
            }
        }
        this.func_213293_j(0.0, 0.0, 0.0);
        this.setPos(hit.field_72450_a, hit.field_72448_b, hit.field_72449_c);
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        PULLING_ENTITY = SynchedEntityData.func_187226_a((Class)EntityGrapplingHook.class, EntityDataSerializers.field_187192_b);
        PULLING = SynchedEntityData.func_187226_a((Class)EntityGrapplingHook.class, EntityDataSerializers.field_187198_h);
    }
}
