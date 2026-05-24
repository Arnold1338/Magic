package hellfirepvp.astralsorcery.client.event.effect;

import hellfirepvp.astralsorcery.client.effect.handler.EffectHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import net.minecraftforge.eventbus.api.EventPriority;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class EffectRenderEventHandler
{
    private static final EffectRenderEventHandler INSTANCE;
    
    private EffectRenderEventHandler() {
    }
    
    public static EffectRenderEventHandler getInstance() {
        return EffectRenderEventHandler.INSTANCE;
    }
    
    public void attachEventListeners(final IEventBus bus) {
        bus.addListener((Consumer)this::onDebugText);
        bus.addListener((Consumer)ClientMiscEventHandler::onRender);
        bus.addListener(EventPriority.LOW, (Consumer)GatewayUIRenderHandler.getInstance()::render);
    }
    
    public void attachTickListeners(final Consumer<ITickHandler> registrar) {
        registrar.accept((ITickHandler)GatewayUIRenderHandler.getInstance());
    }
    
    private void onDebugText(final RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().field_71474_y.field_74330_P) {
            event.getLeft().add("");
            event.getLeft().add(ChatFormatting.BLUE + "[AstralSorcery]" + ChatFormatting.RESET + " EffectHandler:");
            event.getLeft().add(ChatFormatting.BLUE + "[AstralSorcery]" + ChatFormatting.RESET + " > Complex effects: " + EffectHandler.getInstance().getEffectCount());
        }
    }
    
    static {
        INSTANCE = new EffectRenderEventHandler();
    }
}
