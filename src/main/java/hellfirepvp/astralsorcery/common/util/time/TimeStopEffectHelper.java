package hellfirepvp.astralsorcery.common.util.time;

import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.data.config.registry.TileAccelerationBlacklistRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Map;
import net.minecraft.util.Mth;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.level.phys.AABB;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import java.util.Random;

public class TimeStopEffectHelper
{
    private static final Random rand;
    @Nonnull
    private final BlockPos position;
    private final float range;
    private final TimeStopZone.EntityTargetController targetController;
    
    private TimeStopEffectHelper(@Nonnull final BlockPos position, final float range, final TimeStopZone.EntityTargetController targetController) {
        this.position = position;
        this.range = range;
        this.targetController = targetController;
    }
    
    static TimeStopEffectHelper fromZone(final TimeStopZone zone) {
        return new TimeStopEffectHelper(zone.offset, zone.range, zone.targetController);
    }
    
    @Nonnull
    public BlockPos getPosition() {
        return this.position;
    }
    
    public float getRange() {
        return this.range;
    }
    
    public TimeStopZone.EntityTargetController getTargetController() {
        return this.targetController;
    }
    
    @OnlyIn(Dist.CLIENT)
    static void playEntityParticles(final LivingEntity e) {
        final EntityDimensions size = e.func_213305_a(e.func_213283_Z());
        final double x = e.func_226277_ct_() - size.field_220315_a / 2.0f + TimeStopEffectHelper.rand.nextFloat() * size.field_220315_a;
        final double y = e.func_226278_cu_() + TimeStopEffectHelper.rand.nextFloat() * size.field_220316_b;
        final double z = e.func_226281_cx_() - size.field_220315_a / 2.0f + TimeStopEffectHelper.rand.nextFloat() * size.field_220315_a;
        playParticles(x, y, z);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playEntityParticles(final PktPlayEffect ev) {
        final Vector3 at = ByteBufUtils.readVector(ev.getExtraData());
        playParticles(at.getX(), at.getY(), at.getZ());
    }
    
    @OnlyIn(Dist.CLIENT)
    static void playParticles(final double x, final double y, final double z) {
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(x, y, z)).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setScaleMultiplier(0.3f + TimeStopEffectHelper.rand.nextFloat() * 0.5f).setMaxAge(40 + TimeStopEffectHelper.rand.nextInt(20));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void playClientTickEffect() {
        final World world = (World)Minecraft.getInstance().field_71441_e;
        if (world == null) {
            return;
        }
        final List<LivingEntity> entities = world.func_175647_a((Class)LivingEntity.class, new AABB((double)(-this.range), (double)(-this.range), (double)(-this.range), (double)this.range, (double)this.range, (double)this.range).func_72317_d((double)this.position.getX(), (double)this.position.getY(), (double)this.position.getZ()), EntityPredicates.func_188443_a((double)this.position.getX(), (double)this.position.getY(), (double)this.position.getZ(), (double)this.range));
        for (final LivingEntity e : entities) {
            if (e != null && e.isAlive() && this.targetController.shouldFreezeEntity(e) && TimeStopEffectHelper.rand.nextInt(3) == 0) {
                playEntityParticles(e);
            }
        }
        final int minX = Mth.func_76128_c((this.position.getX() - this.range) / 16.0);
        final int maxX = Mth.func_76128_c((this.position.getX() + this.range) / 16.0);
        final int minZ = Mth.func_76128_c((this.position.getZ() - this.range) / 16.0);
        final int maxZ = Mth.func_76128_c((this.position.getZ() + this.range) / 16.0);
        for (int xx = minX; xx <= maxX; ++xx) {
            for (int zz = minZ; zz <= maxZ; ++zz) {
                final Chunk ch = world.func_212866_a_(xx, zz);
                if (!ch.func_76621_g()) {
                    final Map<BlockPos, BlockEntity> map = ch.func_177434_r();
                    for (final Map.Entry<BlockPos, BlockEntity> teEntry : map.entrySet()) {
                        final BlockEntity te = teEntry.getValue();
                        if (TileAccelerationBlacklistRegistry.INSTANCE.canBeInfluenced(te) && te.func_174877_v().func_218141_a((Vector3i)this.position, (double)this.range)) {
                            final double x = te.func_174877_v().getX() + TimeStopEffectHelper.rand.nextFloat();
                            final double y = te.func_174877_v().getY() + TimeStopEffectHelper.rand.nextFloat();
                            final double z = te.func_174877_v().getZ() + TimeStopEffectHelper.rand.nextFloat();
                            playParticles(x, y, z);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 10; ++i) {
            final Vector3 pos = Vector3.random().normalize().multiply(TimeStopEffectHelper.rand.nextFloat() * this.range).add((Vector3i)this.position);
            playParticles(pos.getX(), pos.getY(), pos.getZ());
        }
        if (TimeStopEffectHelper.rand.nextInt(4) == 0) {
            final Vector3 rand1 = Vector3.random().normalize().multiply(TimeStopEffectHelper.rand.nextFloat() * this.range).add((Vector3i)this.position);
            Vector3 rand2 = Vector3.random().normalize().multiply(TimeStopEffectHelper.rand.nextFloat() * this.range).add((Vector3i)this.position);
            if (rand1.distance(rand2) > 10.0) {
                final Vector3 dir = rand1.vectorFromHereTo(rand2);
                rand2 = rand1.clone().add(dir.normalize().multiply(10));
            }
            EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(rand1).makeDefault(rand2).color(VFXColorFunction.WHITE);
        }
    }
    
    @Nonnull
    public CompoundTag serializeNBT() {
        final CompoundTag out = new CompoundTag();
        NBTHelper.writeBlockPosToNBT(this.position, out);
        out.func_74776_a("range", this.range);
        out.put("targetController", (Tag)this.targetController.serializeNBT());
        return out;
    }
    
    @Nonnull
    public static TimeStopEffectHelper deserializeNBT(final CompoundTag cmp) {
        final BlockPos at = NBTHelper.readBlockPosFromNBT(cmp);
        final float range = cmp.getFloat("range");
        return new TimeStopEffectHelper(at, range, TimeStopZone.EntityTargetController.deserializeNBT(cmp.func_74775_l("targetController")));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final TimeStopEffectHelper that = (TimeStopEffectHelper)o;
        return Float.compare(that.range, this.range) == 0 && this.position.equals((Object)that.position);
    }
    
    @Override
    public int hashCode() {
        int result = this.position.hashCode();
        result = 31 * result + ((this.range != 0.0f) ? Float.floatToIntBits(this.range) : 0);
        return result;
    }
    
    static {
        rand = new Random();
    }
}
