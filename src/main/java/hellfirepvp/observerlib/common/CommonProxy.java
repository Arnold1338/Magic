package hellfirepvp.observerlib.common;

import hellfirepvp.observerlib.common.change.StructureIntegrityObserver;
import hellfirepvp.observerlib.common.data.WorldCacheIOThread;
import hellfirepvp.observerlib.common.data.WorldCacheManager;
import hellfirepvp.observerlib.common.event.BlockChangeNotifier;
import hellfirepvp.observerlib.common.event.handler.EventHandlerServer;
import hellfirepvp.observerlib.common.registry.RegistryBlocks;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import hellfirepvp.observerlib.common.util.tick.TickManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

public class CommonProxy {
    private TickManager tickManager;

    public void initialize() {
        this.tickManager = new TickManager();
        attachTickListeners(tickManager::register);
        BlockChangeNotifier.addListener(new StructureIntegrityObserver());
        // Register blocks via DeferredRegister
        RegistryBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void attachLifecycle(IEventBus modBus) {
        modBus.addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        RegistryBlocks.onBlocksReady();
    }

    public void attachEventHandlers(IEventBus forgeBus) {
        forgeBus.addListener(this::onServerStarted);
        forgeBus.addListener(this::onServerStopped);
        forgeBus.addListener(this::onLevelSave);
        tickManager.attachListeners(forgeBus);
    }

    public void attachTickListeners(Consumer<ITickHandler> registrar) {
        registrar.accept(WorldCacheManager.getInstance());
    }

    private void onServerStarted(ServerStartedEvent event) {
        WorldCacheIOThread.onServerStart();
    }

    private void onServerStopped(ServerStoppedEvent event) {
        WorldCacheManager.scheduleSaveAll();
        WorldCacheIOThread.onServerStop();
        WorldCacheManager.cleanUp();
    }

    private void onLevelSave(net.minecraftforge.event.level.LevelEvent.Save event) {
        if (!(event.getLevel() instanceof net.minecraft.world.level.Level world)) return;
        if (world.isClientSide()) return;
        WorldCacheManager.getInstance().doSave(world);
    }
}
