package hellfirepvp.astralsorcery.client.event;

import java.util.EnumSet;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.time.TimeStopEffectHelper;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.data.sync.client.ClientTimeFreezeEffects;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class TimeStopEffectHandler implements ITickHandler
{
    public static final TimeStopEffectHandler INSTANCE;
    
    private TimeStopEffectHandler() {
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (Minecraft.getInstance().field_71441_e == null) {
            return;
        }
        SyncDataHolder.executeClient(SyncDataHolder.DATA_TIME_FREEZE_EFFECTS, ClientTimeFreezeEffects.class, effects -> {
            final List<TimeStopEffectHelper> zoneEffects = effects.getTimeStopEffects((World)Minecraft.getInstance().field_71441_e);
            zoneEffects.forEach(TimeStopEffectHelper::playClientTickEffect);
        });
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "TimeStop EffectHandler";
    }
    
    static {
        INSTANCE = new TimeStopEffectHandler();
    }
}
