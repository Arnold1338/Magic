package hellfirepvp.astralsorcery.common.event.helper;

import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.tick.TimeoutList;

public class EventHelperTemporaryFlight
{
    private static final TimeoutList<Player> temporaryFlight;
    
    private EventHelperTemporaryFlight() {
    }
    
    public static void clearServer() {
        EventHelperTemporaryFlight.temporaryFlight.clear();
    }
    
    public static void onDisconnect(final ServerPlayer player) {
        EventHelperTemporaryFlight.temporaryFlight.remove((Player)player);
    }
    
    public static void attachTickListener(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)EventHelperTemporaryFlight.temporaryFlight);
    }
    
    public static boolean allowFlight(final Player player) {
        return allowFlight(player, 20);
    }
    
    public static boolean allowFlight(final Player player, final int timeout) {
        return EventHelperTemporaryFlight.temporaryFlight.setOrAddTimeout(timeout, player);
    }
    
    static {
        temporaryFlight = new TimeoutList<Player>(player -> {
            if (player instanceof ServerPlayer && ((ServerPlayer)player).field_71134_c.func_73081_b().func_77144_e()) {
                player.field_71075_bZ.field_75101_c = false;
                player.field_71075_bZ.field_75100_b = false;
                player.func_71016_p();
            }
        }, new TickEvent.Type[] { TickEvent.Type.SERVER });
    }
}
