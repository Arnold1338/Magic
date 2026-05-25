package hellfirepvp.astralsorcery.common.entity;

import net.minecraft.network.syncher.IDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import hellfirepvp.astralsorcery.common.util.data.ASDataSerializers;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import java.awt.Color;
import java.util.Random;
import hellfirepvp.astralsorcery.client.effect.function.VFXRenderOffsetFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXScaleFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.projectile.ThrowableEntity;

public class EntityShootingStar extends ThrowableEntity
{
    private static final EntityDataAccessor<Long> EFFECT_SEED;
    
    protected EntityShootingStar(final Level worldIn) {
        super((EntityType)EntityTypesAS.SHOOTING_STAR, worldIn);
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntityShootingStar.EFFECT_SEED, (Object)this.random.nextLong());
    }
    
    protected EntityShootingStar(final double x, final double y, final double z, final Level worldIn) {
        this(worldIn);
        this.setPos(x, y, z);
    }
    
    public static EntityType.IFactory<EntityShootingStar> factory() {
        return (EntityType.IFactory<EntityShootingStar>)((type, world) -> new EntityShootingStar(world));
    }
    
    protected void func_70088_a() {
        this.field_70180_af.func_187214_a((EntityDataAccessor)EntityShootingStar.EFFECT_SEED, (Object)0L);
    }
    
    public long getEffectSeed() {
        return (long)this.field_70180_af.func_187225_a((EntityDataAccessor)EntityShootingStar.EFFECT_SEED);
    }
    
    public void func_70071_h_() {
        this.adjustMotion();
        super.tick();
        if (this.level().level()) {
            this.spawnEffects();
        }
    }
    
    private void adjustMotion() {
        final Vec3 motion = this.func_213322_ci();
        final double y = Math.min(-0.699999988079071, motion.getY());
        this.func_213317_d(new Vec3(motion.getX(), y, motion.getZ()));
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnEffects() {
        final float maxRenderPosDist = 96.0f;
        final VFXRenderOffsetFunction<FXFacingParticle> renderFn = (fx, iPos, pTicks) -> {
            final Player pl = (Player)Minecraft.getInstance().player;
            if (pl == null) {
                return iPos;
            }
            else {
                final Vector3 v = fx.getPosition().clone().subtract(Vector3.atEntityCorner((Entity)pl));
                if (v.length() <= maxRenderPosDist) {
                    return iPos;
                }
                else {
                    return Vector3.atEntityCorner((Entity)pl).add(v.normalize().multiply(maxRenderPosDist));
                }
            }
        };
        final VFXScaleFunction<EntityVisualFX> scaleFn = (fx, scaleIn, pTicks) -> {
            final Player pl2 = (Player)Minecraft.getInstance().player;
            if (pl2 == null) {
                return scaleIn;
            }
            else {
                final Vector3 v2 = fx.getPosition().clone().subtract(Vector3.atEntityCorner((Entity)pl2));
                final float mul = (v2.length() <= maxRenderPosDist) ? 1.0f : ((float)(maxRenderPosDist / v2.length()));
                return scaleIn * 0.25f + (mul * scaleIn - scaleIn * 0.25f);
            }
        };
        final Vector3 thisPosition = Vector3.atEntityCorner((Entity)this);
        for (int i = 0; i < 4; ++i) {
            if (this.random.nextFloat() <= 0.75f) {
                final Vector3 dir = new Vector3(this.func_213322_ci()).clone().multiply(this.random.nextFloat() * -0.6f);
                dir.setX(dir.getX() + this.random.nextFloat() * 0.008 * (this.random.nextBoolean() ? 1 : -1));
                dir.setZ(dir.getZ() + this.random.nextFloat() * 0.008 * (this.random.nextBoolean() ? 1 : -1));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(thisPosition).color(VFXColorFunction.WHITE).setMotion(dir).setAlphaMultiplier(0.85f).setScaleMultiplier(1.2f + this.random.nextFloat() * 0.5f).scale(VFXScaleFunction.SHRINK.andThen(scaleFn)).alpha(VFXAlphaFunction.FADE_OUT).renderOffset(renderFn).setMaxAge(90 + this.random.nextInt(40));
            }
        }
        final float scale = 4.0f + this.random.nextFloat() * 3.0f;
        final int age = 5 + this.random.nextInt(2);
        final Random effectSeed = new Random(this.getEffectSeed());
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(thisPosition).color(VFXColorFunction.constant(Color.getHSBColor(effectSeed.nextFloat() * 360.0f, 1.0f, 1.0f))).setScaleMultiplier(scale).scale(VFXScaleFunction.SHRINK.andThen(scaleFn)).renderOffset(renderFn).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(age);
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(thisPosition).color(VFXColorFunction.WHITE).setScaleMultiplier(scale * 0.6f).scale(VFXScaleFunction.SHRINK.andThen(scaleFn)).renderOffset(renderFn).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(Math.round(age * 1.5f));
    }
    
    public void func_70107_b(final double x, final double y, final double z) {
        final int chunkX = Mth.func_76128_c(this.getX() / 16.0);
        final int chunkZ = Mth.func_76128_c(this.getZ() / 16.0);
        final int newChunkX = Mth.func_76128_c(x / 16.0);
        final int newChunkZ = Mth.func_76128_c(z / 16.0);
        if ((chunkX != newChunkX || chunkZ != newChunkZ) && !this.level().func_217354_b(newChunkX, newChunkZ)) {
            this.func_70106_y();
            return;
        }
        super.setPos(x, y, z);
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
    
    static {
        EFFECT_SEED = SynchedEntityData.func_187226_a((Class)EntityShootingStar.class, (IDataSerializer)ASDataSerializers.LONG);
    }
}
