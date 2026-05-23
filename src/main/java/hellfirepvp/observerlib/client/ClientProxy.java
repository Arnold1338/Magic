package hellfirepvp.observerlib.client;

import hellfirepvp.observerlib.client.preview.StructurePreviewHandler;
import hellfirepvp.observerlib.client.util.ClientTickHelper;
import hellfirepvp.observerlib.common.CommonProxy;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

public class ClientProxy extends CommonProxy {
    @Override
    public void attachEventHandlers(IEventBus forgeBus) {
        super.attachEventHandlers(forgeBus);
        forgeBus.register(ClientTickHelper.INSTANCE);
        StructurePreviewHandler.getInstance().attachEventListeners(forgeBus);
    }

    @Override
    public void attachTickListeners(Consumer<ITickHandler> registrar) {
        super.attachTickListeners(registrar);
        StructurePreviewHandler.getInstance().attachTickHandlers(registrar);
    }
}
