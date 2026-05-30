package hellfirepvp.astralsorcery.common.starlight.network;

import java.util.EnumSet;
import javax.annotation.Nullable;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import java.util.HashMap;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class StarlightTransmissionHandler implements ITickHandler
{
    private static final StarlightTransmissionHandler instance;
    private final Map<ResourceKey<Level>, TransmissionWorldHandler> worldHandlers;
    
    private StarlightTransmissionHandler() {
        this.worldHandlers = new HashMap<ResourceKey<Level>, TransmissionWorldHandler>();
    }
    
    public static StarlightTransmissionHandler getInstance() {
        return StarlightTransmissionHandler.instance;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Level world = (Level)context[0];
        if (world.level() || !(world instanceof ServerLevel)) {

        }
        this.worldHandlers.computeIfAbsent((ResourceKey<Level>)world.dimension(), TransmissionWorldHandler::new).tick((ServerLevel)world);
    }
    
    public void clearServer() {
        this.worldHandlers.values().forEach(TransmissionWorldHandler::clear);
        this.worldHandlers.clear();
    }
    
    public void informWorldUnload(final Level world) {
        final ResourceKey<Level> dimKey = (ResourceKey<Level>)world.dimension();
        final TransmissionWorldHandler handle = this.worldHandlers.get(dimKey);
        if (handle != null) {
            handle.clear();
        }
        this.worldHandlers.remove(dimKey);
    }
    
    @Nullable
    public TransmissionWorldHandler getWorldHandler(final Level world) {
        if (world == null) {
            return null;
        }
        return this.worldHandlers.get(world.dimension());
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.WORLD);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.START;
    }
    
    public String getName() {
        return "Starlight Transmission Handler";
    }
    
    static {
        instance = new StarlightTransmissionHandler();
    }
}
