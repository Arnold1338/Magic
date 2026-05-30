package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.effect.MobEffect;
import net.minecraft.world.level.effect.MobEffectInstance;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import hellfirepvp.astralsorcery.common.data.config.registry.EntityTransmutationRegistry;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalPelotrio;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectPelotrio extends CEffectAbstractList<ListEntries.EntitySpawnEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    private static final AABB PROXIMITY_BOX;
    public static PelotrioConfig CONFIG;
    
    public CEffectPelotrio(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.pelotrio, (int)CEffectPelotrio.CONFIG.maxAmount.get(), (world, pos, state) -> {
            if (!(world instanceof ServerLevel)) {
                return false;
            }
            else {
                pos = world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).above();
                return ListEntries.EntitySpawnEntry.createEntry((ServerLevel)world, pos, MobSpawnType.SPAWNER) != null;
            }
        });
    }
    
    @Nullable
    @Override
    public ListEntries.EntitySpawnEntry recreateElement(final CompoundTag tag, final BlockPos pos) {
        return null;
    }
    
    @Nullable
    @Override
    public ListEntries.EntitySpawnEntry createElement(final Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel)) {
            return null;
        }
        pos = world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).above();
        return ListEntries.EntitySpawnEntry.createEntry((ServerLevel)world, pos, MobSpawnType.SPAWNER);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final Level world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
        if (CEffectPelotrio.rand.nextFloat() < 0.2f) {
            final Vector3 at = Vector3.random().normalize().multiply(CEffectPelotrio.rand.nextFloat() * prop.getSize()).add((Vec3i)pos).add(0.5, 0.5, 0.5);
            EffectHelper.spawnSource(new FXOrbitalPelotrio(at).setOrbitAxis(Vector3.random()).setOrbitRadius(0.8 + CEffectPelotrio.rand.nextFloat() * 0.7).setTicksPerRotation(20 + CEffectPelotrio.rand.nextInt(20)));
        }
    }
    
    @Override
    public boolean playEffect(final Level world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        if (!(world instanceof ServerLevel)) {
            return false;
        }
        boolean update = false;
        final List<LivingEntity> nearbyEntities = world.func_217357_a((Class)LivingEntity.class, CEffectPelotrio.PROXIMITY_BOX.func_186670_a(pos).func_186662_g(properties.getSize()));
        if (properties.isCorrupted()) {
            for (final LivingEntity entity : nearbyEntities) {
                if (entity != null && entity.isAlive() && CEffectPelotrio.rand.nextInt(300) == 0) {
                    final LivingEntity transmuted = EntityTransmutationRegistry.INSTANCE.transmuteEntity((ServerLevel)world, entity);
                    if (transmuted == null) {

                    }
                    transmuted.func_195064_c(new MobEffectInstance((MobEffect)EffectsAS.EFFECT_DROP_MODIFIER, Integer.MAX_VALUE, 1));
                    AstralSorcery.getProxy().scheduleDelayed(() -> world.addFreshEntity((Entity)transmuted));
                    update = true;
                }
            }
            return update;
        }
        final ListEntries.EntitySpawnEntry entry = this.getRandomElementChanced();
        if (entry != null) {
            int count = entry.getCounter();
            ++count;
            entry.setCounter(count);
            this.sendConstellationPing(world, new Vector3((Vec3i)entry.getPos()).add(0.5, 0.5, 0.5));
            if (count >= 10) {
                entry.spawn((ServerLevel)world, MobSpawnType.SPAWNER);
                this.removeElement(entry);
            }
            update = true;
        }
        if (nearbyEntities.size() > (int)CEffectPelotrio.CONFIG.proximityAmount.get()) {
            return update;
        }
        if (CEffectPelotrio.rand.nextFloat() < (double)CEffectPelotrio.CONFIG.spawnChance.get() && this.findNewPosition(world, pos, properties).left().isPresent()) {
            update = true;
        }
        return update;
    }
    
    @Override
    public Config getConfig() {
        return CEffectPelotrio.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectPelotrio.FLAG;
    }
    
    static {
        CEffectPelotrio.FLAG = ConstellationEffect.makeAffectionFlag("pelotrio");
        PROXIMITY_BOX = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        CEffectPelotrio.CONFIG = new PelotrioConfig();
    }
    
    private static class PelotrioConfig extends CountConfig
    {
        private final double defaultSpawnChance = 0.05;
        private final int defaultProximityAmount = 24;
        public ForgeConfigSpec.DoubleValue spawnChance;
        public ForgeConfigSpec.IntValue proximityAmount;
        
        public PelotrioConfig() {
            super("pelotrio", 12.0, 0.0, 5);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the per-tick chance that a new position for a entity-spawn will be searched for.").translation(this.translationKey("spawnChance"));
            final String s = "spawnChance";
            this.getClass();
            this.spawnChance = translation.defineInRange(s, 0.05, 0.0, 1.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Defines the threshold at which the ritual will stop spawning mobs. If there are more or equal amount of mobs near this ritual, the ritual will not spawn more mobs. Mainly to reduce potential server lag.").translation(this.translationKey("proximityAmount"));
            final String s2 = "proximityAmount";
            this.getClass();
            this.proximityAmount = translation2.defineInRange(s2, 24, 0, 256);
        }
    }
}
