package hellfirepvp.observerlib.client.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientTickHelper {
    public static final ClientTickHelper INSTANCE = new ClientTickHelper();
    private static long clientTick = 0L;

    private ClientTickHelper() {}

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            clientTick++;
        }
    }

    public static long getClientTick() {
        return clientTick;
    }
}
