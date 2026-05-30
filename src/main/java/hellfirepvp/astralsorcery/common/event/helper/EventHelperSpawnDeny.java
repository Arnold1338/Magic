package hellfirepvp.astralsorcery.common.event.helper;

import net.minecraftforge.event.TickEvent;
import java.util.Iterator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Vec3i;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.MobCategory;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import hellfirepvp.astralsorcery.common.util.tick.TickTokenMap;

public class EventHelperSpawnDeny
{
    public static TickTokenMap<WorldBlockPos, TickTokenMap.SimpleTickToken<Double>> spawnDenyRegions;
    
    public static void clearServer() {
        EventHelperSpawnDeny.spawnDenyRegions.clear();
    }
    
    public static void attachTickListener(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)EventHelperSpawnDeny.spawnDenyRegions);
    }
    
    public static void attachListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)EventHelperSpawnDeny::onSpawn);
    }
    
    private static void onSpawn(final LivingSpawnEvent.CheckSpawn event) {
        if (event.getResult() == Event.Result.DENY || event.getWorld().level() || event.getSpawner() != null) {

        }
        final LivingEntity entity = event.getEntityLiving();
        if (entity.func_184216_O().contains("skip.spawn.deny")) {

        }
        if ((boolean)GeneralConfig.CONFIG.mobSpawningDenyAllTypes.get() || entity.getClassification(false) == MobCategory.MONSTER) {
            final Vector3 entityPos = Vector3.atEntityCorner((Entity)entity);
            for (final Map.Entry<WorldBlockPos, TickTokenMap.SimpleTickToken<Double>> entry : EventHelperSpawnDeny.spawnDenyRegions.entrySet()) {
                if (!entry.getKey().getWorldKey().equals(entity.level().dimension())) {

                }
                if (entityPos.distance((Vec3i)entry.getKey()) <= entry.getValue().getValue()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
    
    static {
        EventHelperSpawnDeny.spawnDenyRegions = new TickTokenMap<WorldBlockPos, TickTokenMap.SimpleTickToken<Double>>(TickEvent.Type.SERVER, new TickEvent.Type[0]);
    }
}
