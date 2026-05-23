package hellfirepvp.observerlib.common.data.base;

import hellfirepvp.observerlib.common.data.WorldCacheIOThread;
import hellfirepvp.observerlib.common.data.WorldCacheManager;
import net.minecraft.server.MinecraftServer;

public class WorldDataLoader {
    private static final WorldDataLoader INSTANCE = new WorldDataLoader();
    private WorldDataLoader() {}
    public static WorldDataLoader get() { return INSTANCE; }

    public void onServerStart(MinecraftServer server) { WorldCacheIOThread.onServerStart(); }
    public void onServerStop() {
        WorldCacheManager.scheduleSaveAll();
        WorldCacheIOThread.onServerStop();
        WorldCacheManager.cleanUp();
    }
}
