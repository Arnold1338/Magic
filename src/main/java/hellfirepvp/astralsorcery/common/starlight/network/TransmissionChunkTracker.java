package hellfirepvp.astralsorcery.common.starlight.network;

import net.minecraftforge.event.level.LevelEvent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ChunkEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class TransmissionChunkTracker
{
    public static final TransmissionChunkTracker INSTANCE;
    
    private TransmissionChunkTracker() {
    }
    
    public void attachListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)this::onChLoad);
        eventBus.addListener((Consumer)this::onChUnload);
        eventBus.addListener((Consumer)this::onWorldLoad);
        eventBus.addListener((Consumer)this::onWorldUnload);
    }
    
    private void onChLoad(final ChunkEvent.Load event) {
        final IWorld iWorld = event.getWorld();
        if (iWorld.func_201670_d() || !(iWorld instanceof World)) {
            return;
        }
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler((World)iWorld);
        if (handle != null) {
            handle.informChunkLoad(event.getChunk().func_76632_l());
        }
    }
    
    private void onChUnload(final ChunkEvent.Unload event) {
        final IWorld iWorld = event.getWorld();
        if (iWorld.func_201670_d() || !(iWorld instanceof World)) {
            return;
        }
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler((World)iWorld);
        if (handle != null) {
            handle.informChunkUnload(event.getChunk().func_76632_l());
        }
    }
    
    private void onWorldLoad(final WorldEvent.Load event) {
        final IWorld iWorld = event.getWorld();
        if (iWorld.func_201670_d() || !(iWorld instanceof World)) {
            return;
        }
        StarlightUpdateHandler.getInstance().informWorldLoad((World)iWorld);
    }
    
    private void onWorldUnload(final WorldEvent.Unload event) {
        final IWorld iWorld = event.getWorld();
        if (iWorld.func_201670_d() || !(iWorld instanceof World)) {
            return;
        }
        StarlightTransmissionHandler.getInstance().informWorldUnload((World)iWorld);
    }
    
    static {
        INSTANCE = new TransmissionChunkTracker();
    }
}
