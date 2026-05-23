package hellfirepvp.observerlib.common.event.handler;

import hellfirepvp.observerlib.common.data.WorldCacheManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerServer {
    public static final EventHandlerServer INSTANCE = new EventHandlerServer();
    private EventHandlerServer() {}

    @SubscribeEvent
    public void onSave(LevelEvent.Save event) {
        if (!(event.getLevel() instanceof Level world)) return;
        if (world.isClientSide()) return;
        WorldCacheManager.getInstance().doSave(world);
    }
}
