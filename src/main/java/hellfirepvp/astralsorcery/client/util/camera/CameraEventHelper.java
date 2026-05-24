package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderPlayerEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class CameraEventHelper
{
    public static void attachEventListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)CameraEventHelper::onPlayerRender);
    }
    
    private static void onPlayerRender(final RenderPlayerEvent.Pre event) {
        if (event.getPlayer() == Minecraft.getInstance().field_71439_g && ClientCameraManager.INSTANCE.hasActiveTransformer()) {
            event.setCanceled(true);
        }
    }
}
