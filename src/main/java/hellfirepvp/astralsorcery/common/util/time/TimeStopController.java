package hellfirepvp.astralsorcery.common.util.time;

import java.util.HashMap;
import java.util.EnumSet;
import net.minecraftforge.event.TickEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEntities;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientTimeFreezeEntities;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEffects;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import java.util.LinkedList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Collections;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class TimeStopController implements ITickHandler
{
    private static final Map<RegistryKey<World>, List<TimeStopZone>> activeTimeStopZones;
    public static final TimeStopController INSTANCE;
    
    private TimeStopController() {
    }
    
    @Nullable
    public static TimeStopZone tryGetZoneAt(final World world, final BlockPos pos) {
        if (world.isClientSide) {
            return null;
        }
        final List<TimeStopZone> zones = TimeStopController.activeTimeStopZones.getOrDefault(world.dimension(), Collections.emptyList());
        for (final TimeStopZone zone : zones) {
            if (zone.offset.equals((Object)pos)) {
                return zone;
            }
        }
        return null;
    }
    
    @Nonnull
    public static TimeStopZone freezeWorldAt(@Nonnull final TimeStopZone.EntityTargetController controller, @Nonnull final World world, @Nonnull final BlockPos offset, final float range, final int maxAge) {
        final TimeStopZone stopZone = new TimeStopZone(controller, range, offset, world, maxAge);
        final List<TimeStopZone> zones = TimeStopController.activeTimeStopZones.computeIfAbsent((RegistryKey<World>)world.dimension(), id -> new LinkedList());
        zones.add(stopZone);
        SyncDataHolder.executeServer(SyncDataHolder.DATA_TIME_FREEZE_EFFECTS, DataTimeFreezeEffects.class, data -> data.addNewEffect((RegistryKey<World>)world.dimension(), TimeStopEffectHelper.fromZone(stopZone)));
        return stopZone;
    }
    
    public static void onWorldUnload(final World world) {
        if (world.func_201670_d()) {
            return;
        }
        final RegistryKey<World> dimKey = (RegistryKey<World>)world.dimension();
        for (final TimeStopZone stop : TimeStopController.activeTimeStopZones.getOrDefault(dimKey, Collections.emptyList())) {
            stop.stopEffect();
        }
        TimeStopController.activeTimeStopZones.remove(dimKey);
    }
    
    public static boolean isFrozenDirectly(final Entity e) {
        if (e.func_130014_f_().func_201670_d()) {
            return SyncDataHolder.computeClient(SyncDataHolder.DATA_TIME_FREEZE_ENTITIES, ClientTimeFreezeEntities.class, data -> data.isFrozen(e)).orElse(false);
        }
        return SyncDataHolder.computeServer(SyncDataHolder.DATA_TIME_FREEZE_ENTITIES, DataTimeFreezeEntities.class, data -> data.isFrozen(e)).orElse(false);
    }
    
    public static boolean skipLivingTick(final LivingEntity e) {
        if (isFrozenDirectly((Entity)e)) {
            boolean shouldFreeze = true;
            if (!e.isAlive() || e.func_110143_aJ() <= 0.0f) {
                shouldFreeze = false;
            }
            if (e instanceof EnderDragonEntity && ((EnderDragonEntity)e).func_184670_cT().func_188756_a().func_188652_i() == PhaseType.field_188750_j) {
                shouldFreeze = false;
            }
            if (shouldFreeze) {
                if (e.field_70170_p.func_201670_d()) {
                    for (int amt = (int)MathHelper.func_76129_c(e.func_213311_cf() * e.func_213302_cg()), i = 0; i < amt; ++i) {
                        if (e.field_70170_p.field_73012_v.nextInt(5) == 0) {
                            TimeStopEffectHelper.playEntityParticles(e);
                        }
                    }
                }
                if (!e.func_130014_f_().func_201670_d()) {
                    TimeStopZone.handleImportantEntityTicks(e);
                    return true;
                }
            }
        }
        final List<TimeStopZone> freezeAreas = TimeStopController.activeTimeStopZones.get(e.func_130014_f_().dimension());
        if (freezeAreas != null && !freezeAreas.isEmpty()) {
            for (final TimeStopZone stop : freezeAreas) {
                if (stop.interceptEntityTick(e) && !e.func_130014_f_().func_201670_d()) {
                    TimeStopZone.handleImportantEntityTicks(e);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        for (final Map.Entry<RegistryKey<World>, List<TimeStopZone>> zoneMap : TimeStopController.activeTimeStopZones.entrySet()) {
            final Iterator<TimeStopZone> iterator = zoneMap.getValue().iterator();
            while (iterator.hasNext()) {
                final TimeStopZone zone = iterator.next();
                if (zone.shouldDespawn()) {
                    zone.stopEffect();
                    SyncDataHolder.executeServer(SyncDataHolder.DATA_TIME_FREEZE_EFFECTS, DataTimeFreezeEffects.class, data -> data.removeEffect((RegistryKey<World>)zoneMap.getKey(), TimeStopEffectHelper.fromZone(zone)));
                    iterator.remove();
                }
                else {
                    zone.onServerTick();
                    if (!zone.shouldDespawn()) {
                        continue;
                    }
                    zone.stopEffect();
                    SyncDataHolder.executeServer(SyncDataHolder.DATA_TIME_FREEZE_EFFECTS, DataTimeFreezeEffects.class, data -> data.removeEffect((RegistryKey<World>)zoneMap.getKey(), TimeStopEffectHelper.fromZone(zone)));
                    iterator.remove();
                }
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase.equals((Object)TickEvent.Phase.START);
    }
    
    public String getName() {
        return "TimeStop Controller";
    }
    
    static {
        activeTimeStopZones = new HashMap<RegistryKey<World>, List<TimeStopZone>>();
        INSTANCE = new TimeStopController();
    }
}
