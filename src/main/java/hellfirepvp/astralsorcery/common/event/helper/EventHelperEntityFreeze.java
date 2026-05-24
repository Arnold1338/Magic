package hellfirepvp.astralsorcery.common.event.helper;

import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.data.sync.server.DataTimeFreezeEntities;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import hellfirepvp.astralsorcery.common.util.time.TimeStopController;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.util.tick.TimeoutList;

public class EventHelperEntityFreeze
{
    private static final TimeoutList<Entity> timeFreezeTimeout;
    
    private EventHelperEntityFreeze() {
    }
    
    public static void attachTickListener(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)EventHelperEntityFreeze.timeFreezeTimeout);
    }
    
    public static void attachListeners(final IEventBus bus) {
        bus.addListener(EventPriority.HIGHEST, (Consumer)EventHelperEntityFreeze::onLivingTick);
        bus.addListener((Consumer)EventHelperEntityFreeze::onLivingKnockBack);
        bus.addListener((Consumer)EventHelperEntityFreeze::onDestroy);
    }
    
    private static void onLivingTick(final LivingEvent.LivingUpdateEvent event) {
        if (TimeStopController.skipLivingTick(event.getEntityLiving())) {
            event.setCanceled(true);
        }
    }
    
    private static void onLivingKnockBack(final LivingKnockBackEvent event) {
        if (TimeStopController.skipLivingTick(event.getEntityLiving())) {
            event.setCanceled(true);
        }
    }
    
    private static void onDestroy(final LivingDestroyBlockEvent event) {
        if (TimeStopController.isFrozenDirectly(event.getEntity())) {
            event.setCanceled(true);
        }
    }
    
    public static boolean freeze(final Entity e) {
        if (EventHelperEntityFreeze.timeFreezeTimeout.setOrAddTimeout(20, e)) {
            SyncDataHolder.executeServer(SyncDataHolder.DATA_TIME_FREEZE_ENTITIES, DataTimeFreezeEntities.class, data -> data.freezeEntity(e));
            return true;
        }
        return false;
    }
    
    static {
        timeFreezeTimeout = new TimeoutList<Entity>(entity -> SyncDataHolder.executeServer(SyncDataHolder.DATA_TIME_FREEZE_ENTITIES, DataTimeFreezeEntities.class, data -> data.unfreezeEntity(entity)), new TickEvent.Type[] { TickEvent.Type.SERVER });
    }
}
