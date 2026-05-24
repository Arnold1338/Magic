package hellfirepvp.astralsorcery.common.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.world.level.phys.HitResult;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Iterator;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.level.phys.AABB;
import net.minecraft.world.entity.projectile.ThrowableEntity;

public class EntityNocturnalSpark extends ThrowableEntity
{
    private static final AABB NO_DUPE_BOX;
    private static final EntityDataAccessor<Boolean> SPAWNING;
    private int ticksSpawning;
    
    public EntityNocturnalSpark(final World world) {
        super((EntityType)EntityTypesAS.NOCTURNAL_SPARK, world);
        this.ticksSpawning = 0;
    }
    
    public EntityNocturnalSpark(final double x, final double y, final double z, final World world) {
        super((EntityType)EntityTypesAS.NOCTURNAL_SPARK, x, y, z, world);
        this.ticksSpawning = 0;
    }
    
    public EntityNocturnalSpark(final LivingEntity thrower, final World world) {
        super((EntityType)EntityTypesAS.NOCTURNAL_SPARK, thrower, world);
        this.ticksSpawning = 0;
        this.func_234612_a_((Entity)thrower, thrower.field_70125_A, thrower.field_70177_z, 0.0f, 0.7f, 0.9f);
    }
    
    public static EntityType.IFactory<EntityNocturnalSpark> factory() {
        return (EntityType.IFactory<EntityNocturnalSpark>)((type, world) -> new EntityNocturnalSpark(world));
    }
    
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a((EntityDataAccessor)EntityNocturnalSpark.SPAWNING, (Object)false);
    }
    
    public void setSpawning() {
        this.func_213317_d(Vec3.field_186680_a);
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntityNocturnalSpark.SPAWNING, (Object)true);
    }
    
    public boolean isSpawning() {
        return (boolean)this.field_70180_af.func_187225_a((EntityDataAccessor)EntityNocturnalSpark.SPAWNING);
    }
    
    public void func_70071_h_() {
        super.tick();
        if (!this.isAlive()) {
            return;
        }
        if (!this.field_70170_p.func_201670_d()) {
            this.removeLights();
            if (this.isSpawning()) {
                ++this.ticksSpawning;
                this.spawnCycle();
                this.removeDuplicates();
                if (this.ticksSpawning > 200) {
                    this.func_70106_y();
                }
            }
        }
        else {
            this.spawnEffects();
        }
    }
    
    private void removeLights() {
        if (this.func_130014_f_() instanceof ServerLevel) {
            final ServerLevel sWorld = (ServerLevel)this.func_130014_f_();
            if (this.field_70173_aa % 5 == 0) {
                final List<BlockPos> lightPositions = BlockDiscoverer.searchForBlocksAround((World)sWorld, this.func_233580_cy_(), 8, (world, pos, state) -> !(state.getBlock() instanceof AirBlock) && state.func_185887_b((IBlockReader)world, pos) != -1.0f && state.getLightValue((IBlockReader)world, pos) > 3);
                for (final BlockPos light : lightPositions) {
                    if (!BlockUtils.breakBlockWithoutPlayer(sWorld, light, sWorld.getBlockState(light), ItemStack.EMPTY, true, true)) {
                        sWorld.func_217377_a(light, false);
                    }
                }
            }
        }
    }
    
    private void removeDuplicates() {
        final List<EntityNocturnalSpark> sparks = this.field_70170_p.func_217357_a((Class)EntityNocturnalSpark.class, EntityNocturnalSpark.NO_DUPE_BOX.func_186670_a(this.func_233580_cy_()));
        for (final EntityNocturnalSpark spark : sparks) {
            if (this.equals((Object)spark)) {
                continue;
            }
            if (!spark.isAlive()) {
                continue;
            }
            if (!spark.isSpawning()) {
                continue;
            }
            spark.func_70106_y();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnEffects() {
        if (this.isSpawning()) {
            for (int i = 0; i < 15; ++i) {
                final Vector3 thisPos = Vector3.atEntityCorner((Entity)this).addY(1.0);
                MiscUtils.applyRandomOffset(thisPos, this.field_70146_Z, (float)(2 + this.field_70146_Z.nextInt(4)));
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(thisPos).setScaleMultiplier(4.0f).alpha(VFXAlphaFunction.PYRAMID).setAlphaMultiplier(0.7f).color(VFXColorFunction.constant(Color.BLACK));
                if (this.field_70146_Z.nextInt(5) == 0) {
                    this.randomizeColor(p);
                }
                if (this.field_70146_Z.nextInt(20) == 0) {
                    final Vector3 at = Vector3.atEntityCorner((Entity)this);
                    MiscUtils.applyRandomOffset(at, this.field_70146_Z, 2.0f);
                    final Vector3 to = Vector3.atEntityCorner((Entity)this);
                    MiscUtils.applyRandomOffset(to, this.field_70146_Z, 2.0f);
                    EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(at).makeDefault(to).color(VFXColorFunction.constant(Color.BLACK));
                }
            }
        }
        else {
            for (int j = 0; j < 6; ++j) {
                final FXFacingParticle p2 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this)).setMotion(new Vector3(0.04f - this.field_70146_Z.nextFloat() * 0.08f, 0.04f - this.field_70146_Z.nextFloat() * 0.08f, 0.04f - this.field_70146_Z.nextFloat() * 0.08f)).setScaleMultiplier(0.25f);
                this.randomizeColor(p2);
            }
            FXFacingParticle p2 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this));
            p2.setScaleMultiplier(0.6f);
            this.randomizeColor(p2);
            p2 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this).add(this.func_213322_ci().func_216372_d(0.5, 0.5, 0.5)));
            p2.setScaleMultiplier(0.6f);
            this.randomizeColor(p2);
        }
    }
    
    private void spawnCycle() {
        if (this.field_70146_Z.nextInt(12) == 0 && this.field_70170_p instanceof ServerLevel) {
            BlockPos pos = this.func_233580_cy_();
            pos.offset(this.field_70146_Z.nextInt(2) - this.field_70146_Z.nextInt(2), 1, this.field_70146_Z.nextInt(2) - this.field_70146_Z.nextInt(2));
            pos = BlockUtils.firstSolidDown((IBlockReader)this.field_70170_p, pos).above();
            if (pos.func_177951_i((Vector3i)this.func_233580_cy_()) >= 16.0) {
                return;
            }
            EntityUtils.performWorldSpawningAt((ServerLevel)this.field_70170_p, pos, MobCategory.MONSTER, MobSpawnType.SPAWNER, true, 11);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void randomizeColor(final FXFacingParticle p) {
        switch (this.field_70146_Z.nextInt(3)) {
            case 0: {
                p.color(VFXColorFunction.constant(ColorsAS.NOCTURNAL_POWDER_1));
                break;
            }
            case 1: {
                p.color(VFXColorFunction.constant(ColorsAS.NOCTURNAL_POWDER_2));
                break;
            }
            case 2: {
                p.color(VFXColorFunction.constant(ColorsAS.NOCTURNAL_POWDER_3));
                break;
            }
        }
    }
    
    protected void func_70227_a(final HitResult result) {
        if (HitResult.Type.ENTITY.equals((Object)result.func_216346_c())) {
            return;
        }
        final Vec3 hit = result.func_216347_e();
        this.setSpawning();
        this.setPos(hit.field_72450_a, hit.field_72448_b, hit.field_72449_c);
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        NO_DUPE_BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).func_186662_g(15.0);
        SPAWNING = SynchedEntityData.func_187226_a((Class)EntityNocturnalSpark.class, EntityDataSerializers.field_187198_h);
    }
}
