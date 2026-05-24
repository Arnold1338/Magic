package hellfirepvp.astralsorcery.common.entity;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import java.util.List;
import java.util.Collection;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.phys.Vec3;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectBootes;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.monster.PhantomEntity;
import net.minecraft.world.level.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.entity.animal.BatEntity;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import javax.annotation.Nullable;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.data.config.entry.EntityConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.entity.ai.attributes.Attributes;
import net.minecraft.world.level.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.FlyingMob;

public class EntityFlare extends FlyingMob
{
    private static final int RANDOM_WANDER_RANGE = 31;
    private int entityAge;
    private Vector3 currentMoveTarget;
    private boolean ambient;
    private int followingEntityId;
    private Object texClientSprite;
    
    public EntityFlare(final World worldIn) {
        super((EntityType)EntityTypesAS.FLARE, worldIn);
        this.entityAge = 0;
        this.currentMoveTarget = null;
        this.ambient = false;
        this.followingEntityId = -1;
        this.texClientSprite = null;
    }
    
    public static EntityType.IFactory<EntityFlare> factory() {
        return (EntityType.IFactory<EntityFlare>)((type, world) -> new EntityFlare(world));
    }
    
    public static AttributeSupplier.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 1.0);
    }
    
    public static void spawnAmbientFlare(final World world, final BlockPos at) {
        if (world.func_201670_d() || (int)EntityConfig.CONFIG.flareAmbientSpawnChance.get() <= 0) {
            return;
        }
        final float nightPercent = DayTimeHelper.getCurrentDaytimeDistribution(world);
        if (world.field_73012_v.nextInt((int)EntityConfig.CONFIG.flareAmbientSpawnChance.get()) == 0 && world.field_73012_v.nextFloat() < nightPercent) {
            MiscUtils.executeWithChunk((IWorldReader)world, at, () -> {
                if (world.isEmptyBlock(at)) {
                    final EntityFlare flare = (EntityFlare)EntityTypesAS.FLARE.func_200721_a(world);
                    flare.setPos(at.getX() + 0.5, at.getY() + 0.5, at.getZ() + 0.5);
                    flare.setAmbient(true);
                    world.func_217376_c((Entity)flare);
                }
            });
        }
    }
    
    public EntityFlare setAmbient(final boolean ambient) {
        this.ambient = ambient;
        return this;
    }
    
    public boolean isAmbient() {
        return this.ambient;
    }
    
    public EntityFlare setFollowingTarget(final LivingEntity entity) {
        this.followingEntityId = entity.func_145782_y();
        return this;
    }
    
    @Nullable
    public LivingEntity getFollowingTarget() {
        if (this.followingEntityId == -1) {
            return null;
        }
        final Entity e = this.field_70170_p.func_73045_a(this.followingEntityId);
        if (e == null || !e.isAlive() || !(e instanceof LivingEntity)) {
            return null;
        }
        return (LivingEntity)e;
    }
    
    public void func_70071_h_() {
        super.tick();
        ++this.entityAge;
        if (this.func_130014_f_().func_201670_d()) {
            this.tickClient();
        }
        else {
            if (this.isAmbient() && this.entityAge > 600 && this.field_70146_Z.nextInt(600) == 0) {
                DamageUtil.attackEntityFrom((Entity)this, CommonProxy.DAMAGE_SOURCE_STELLAR, 1.0f);
            }
            if (this.isAlive()) {
                if ((boolean)EntityConfig.CONFIG.flareAttackBats.get() && this.field_70146_Z.nextInt(30) == 0) {
                    final BatEntity closest = EntityUtils.getClosestEntity((IWorld)this.func_130014_f_(), BatEntity.class, this.func_174813_aQ().func_186662_g(10.0), Vector3.atEntityCenter((Entity)this));
                    if (closest != null) {
                        this.doLightningAttack((LivingEntity)closest, 100.0f);
                    }
                }
                if ((boolean)EntityConfig.CONFIG.flareAttackPhantoms.get() && this.field_70146_Z.nextInt(30) == 0) {
                    final PhantomEntity closest2 = EntityUtils.getClosestEntity((IWorld)this.func_130014_f_(), PhantomEntity.class, this.func_174813_aQ().func_186662_g(10.0), Vector3.atEntityCenter((Entity)this));
                    if (closest2 != null) {
                        this.doLightningAttack((LivingEntity)closest2, 100.0f);
                    }
                }
                if (this.isAmbient()) {
                    final boolean atTarget = this.currentMoveTarget == null || this.currentMoveTarget.distance((Entity)this) < 5.0;
                    if (atTarget) {
                        this.currentMoveTarget = null;
                    }
                    if (this.currentMoveTarget == null && this.field_70146_Z.nextInt(150) == 0) {
                        final BlockPos newTarget = this.func_233580_cy_().offset(this.field_70146_Z.nextInt(31) * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.field_70146_Z.nextInt(31) * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.field_70146_Z.nextInt(31) * (this.field_70146_Z.nextBoolean() ? 1 : -1));
                        if (newTarget.getY() > 1 && newTarget.getY() < 254 && new Vector3((Vector3i)newTarget).distance((Entity)this) >= 5.0) {
                            MiscUtils.executeWithChunk((IWorldReader)this.func_130014_f_(), newTarget, () -> this.currentMoveTarget = new Vector3((Vector3i)newTarget));
                        }
                    }
                }
                else if (this.func_70638_az() != null) {
                    if (!this.func_70638_az().isAlive() || (this.getFollowingTarget() != null && this.getFollowingTarget().func_70032_d((Entity)this) > 30.0f)) {
                        this.func_70624_b((LivingEntity)null);
                    }
                    else {
                        final Vector3 newTarget2 = Vector3.atEntityCenter((Entity)this.func_70638_az()).addY(1.5);
                        if (newTarget2.getY() > 1.0 && newTarget2.getY() < 254.0 && newTarget2.distance((Entity)this) >= 3.0) {
                            this.currentMoveTarget = newTarget2;
                        }
                        else {
                            this.currentMoveTarget = null;
                        }
                    }
                }
                else {
                    if (this.followingEntityId == -1) {
                        DamageUtil.attackEntityFrom((Entity)this, CommonProxy.DAMAGE_SOURCE_STELLAR, 1.0f);
                        return;
                    }
                    final LivingEntity following = this.getFollowingTarget();
                    if (following == null) {
                        DamageUtil.attackEntityFrom((Entity)this, CommonProxy.DAMAGE_SOURCE_STELLAR, 1.0f);
                    }
                    else {
                        final MantleEffectBootes effect = ItemMantle.getEffect(following, ConstellationsAS.bootes);
                        if (effect == null) {
                            DamageUtil.attackEntityFrom((Entity)this, CommonProxy.DAMAGE_SOURCE_STELLAR, 1.0f);
                            return;
                        }
                        if (this.func_70638_az() != null && !this.func_70638_az().isAlive()) {
                            this.func_70624_b((LivingEntity)null);
                        }
                        if (this.func_70638_az() == null) {
                            final Vector3 newTarget3 = Vector3.atEntityCenter((Entity)following).addY(2.5);
                            if (newTarget3.distance((Entity)this) >= 2.0) {
                                this.currentMoveTarget = newTarget3;
                            }
                            else {
                                this.currentMoveTarget = null;
                            }
                        }
                    }
                }
                final LivingEntity target = this.func_70638_az();
                if (target != null && target.isAlive() && target.func_70032_d((Entity)this) < 10.0f && this.field_70146_Z.nextInt(40) == 0) {
                    DamageUtil.shotgunAttack(target, e -> this.doLightningAttack(e, 2.0f + this.field_70146_Z.nextFloat() * 2.0f));
                }
                this.doMovement();
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickClient() {
        if (this.texClientSprite == null) {
            this.texClientSprite = EffectHelper.of(EffectTemplatesAS.FACING_SPRITE).spawn(Vector3.atEntityCorner((Entity)this).addY(this.func_213302_cg() / 2.0f)).setSprite(SpritesAS.SPR_ENTITY_FLARE).setScaleMultiplier(0.45f).position((fx, position, motionToBeMoved) -> Vector3.atEntityCorner((Entity)this).addY(this.func_213302_cg() / 2.0f)).scale((fx, scaleIn, pTicks) -> this.isAlive() ? scaleIn : 0.0f).refresh(fx -> this.isAlive());
        }
        else if (this.isAlive()) {
            EffectHelper.refresh(this.texClientSprite, EffectTemplatesAS.FACING_SPRITE);
        }
        if (this.field_70146_Z.nextBoolean()) {
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this).add(this.field_70146_Z.nextFloat() * 0.2 * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.func_213302_cg() / 2.0f + this.field_70146_Z.nextFloat() * 0.2 * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.field_70146_Z.nextFloat() * 0.2 * (this.field_70146_Z.nextBoolean() ? 1 : -1))).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.15f + this.field_70146_Z.nextFloat() * 0.1f);
            if (this.field_70146_Z.nextBoolean()) {
                p.color(VFXColorFunction.WHITE);
            }
        }
    }
    
    private void doLightningAttack(final LivingEntity target, final float damage) {
        DamageUtil.attackEntityFrom((Entity)target, CommonProxy.DAMAGE_SOURCE_STELLAR, damage, (Entity)this);
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.LIGHTNING).addData(buf -> {
            ByteBufUtils.writeVector(buf, Vector3.atEntityCorner((Entity)this).addY(this.func_213302_cg() / 2.0f));
            ByteBufUtils.writeVector(buf, Vector3.atEntityCorner((Entity)target).addY(target.func_213302_cg() / 2.0f));
            buf.writeInt(ColorsAS.EFFECT_LIGHTNING.getRGB());
            return;
        });
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.func_130014_f_(), (Vector3i)this.func_233580_cy_(), 32.0));
    }
    
    private void doMovement() {
        if (this.currentMoveTarget != null) {
            final Vec3 motion = this.func_213322_ci();
            final double motionX = (Math.signum(this.currentMoveTarget.getX() - this.func_226277_ct_()) * 0.5 - motion.func_82615_a()) * (this.isAmbient() ? 0.01 : 0.025);
            final double motionY = (Math.signum(this.currentMoveTarget.getY() - this.func_226278_cu_()) * 0.7 - motion.func_82617_b()) * (this.isAmbient() ? 0.01 : 0.025);
            final double motionZ = (Math.signum(this.currentMoveTarget.getZ() - this.func_226281_cx_()) * 0.5 - motion.func_82616_c()) * (this.isAmbient() ? 0.01 : 0.025);
            this.func_213317_d(motion.func_72441_c(motionX, motionY, motionZ));
            this.field_191988_bg = 0.2f;
        }
    }
    
    public void func_70108_f(final Entity entityIn) {
        if (!(entityIn instanceof Player)) {
            super.func_70108_f(entityIn);
        }
    }
    
    protected void func_82167_n(final Entity entityIn) {
        if (!(entityIn instanceof Player)) {
            super.func_70108_f(entityIn);
        }
    }
    
    protected void func_70665_d(final DamageSource damageSrc, final float damageAmount) {
        super.func_70665_d(damageSrc, damageAmount);
        this.func_70606_j(0.0f);
    }
    
    public boolean func_213380_a(final IWorld worldIn, final MobSpawnType spawnReasonIn) {
        return false;
    }
    
    @Nullable
    protected SoundEvent func_184601_bQ(final DamageSource damageSourceIn) {
        return null;
    }
    
    @Nullable
    protected SoundEvent func_184615_bR() {
        return null;
    }
    
    protected void func_70609_aI() {
        this.func_70106_y();
        if (this.func_130014_f_().func_201670_d()) {
            this.tickClientDeathEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickClientDeathEffects() {
        if (this.texClientSprite != null) {
            ((FXFacingSprite)this.texClientSprite).requestRemoval();
        }
        final List<Vector3> posList = MiscUtils.getCirclePositions(Vector3.atEntityCorner((Entity)this).addY(this.func_213302_cg() / 2.0f), Vector3.positiveYRandom(), 0.3, 10);
        posList.addAll(MiscUtils.getCirclePositions(Vector3.atEntityCorner((Entity)this).addY(this.func_213302_cg() / 2.0f), Vector3.positiveYRandom(), 0.8, 20));
        posList.forEach(pos -> {
            final FXFacingParticle p2 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos.add(Vector3.random().multiply(0.45f))).setScaleMultiplier(0.1f + this.field_70146_Z.nextFloat() * 0.25f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(30 + this.field_70146_Z.nextInt(40));
            if (this.field_70146_Z.nextBoolean()) {
                p2.color(VFXColorFunction.WHITE);
            }
            return;
        });
        for (int i = 0; i < 10; ++i) {
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this).add(this.field_70146_Z.nextFloat() * 0.15 * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.func_213302_cg() / 2.0f + this.field_70146_Z.nextFloat() * 0.15 * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.field_70146_Z.nextFloat() * 0.15 * (this.field_70146_Z.nextBoolean() ? 1 : -1))).alpha(VFXAlphaFunction.FADE_OUT).setMotion(Vector3.random().multiply(0.05f)).setScaleMultiplier(0.25f + this.field_70146_Z.nextFloat() * 0.1f).setMaxAge(40 + this.field_70146_Z.nextInt(40));
            if (this.field_70146_Z.nextBoolean()) {
                p.color(VFXColorFunction.WHITE);
            }
        }
    }
    
    public void func_213281_b(final CompoundTag compound) {
        super.func_213281_b(compound);
        compound.putInt("AS_entityAge", this.entityAge);
        compound.putBoolean("AS_ambient", this.ambient);
    }
    
    public void func_70037_a(final CompoundTag compound) {
        super.func_70037_a(compound);
        this.entityAge = compound.getInt("AS_entityAge");
        this.ambient = compound.getBoolean("AS_ambient");
    }
}
