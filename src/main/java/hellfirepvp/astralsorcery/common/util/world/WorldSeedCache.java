package hellfirepvp.astralsorcery.common.util.world;

import java.util.HashMap;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.client.PktRequestSeed;
import java.util.Optional;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;

public class WorldSeedCache
{
    private static long lastServerQuery;
    private static int activeSession;
    private static final Map<RegistryKey<Level>, Long> cacheSeedLookup;
    
    @OnlyIn(Dist.CLIENT)
    public static void clearClient() {
        ++WorldSeedCache.activeSession;
        WorldSeedCache.cacheSeedLookup.clear();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void updateSeedCache(final RegistryKey<Level> dim, final int session, final long seed) {
        if (WorldSeedCache.activeSession == session) {
            WorldSeedCache.cacheSeedLookup.put(dim, seed);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static Optional<Long> getSeedIfPresent(final RegistryKey<Level> dim) {
        if (dim == null) {
            return Optional.empty();
        }
        if (!WorldSeedCache.cacheSeedLookup.containsKey(dim)) {
            final long current = System.currentTimeMillis();
            if (current - WorldSeedCache.lastServerQuery > 5000L) {
                WorldSeedCache.lastServerQuery = current;
                ++WorldSeedCache.activeSession;
                final PktRequestSeed req = new PktRequestSeed(WorldSeedCache.activeSession, dim);
                PacketChannel.CHANNEL.sendToServer(req);
            }
            return Optional.empty();
        }
        return Optional.of(WorldSeedCache.cacheSeedLookup.get(dim));
    }
    
    static {
        WorldSeedCache.lastServerQuery = 0L;
        WorldSeedCache.activeSession = 0;
        cacheSeedLookup = new HashMap<RegistryKey<Level>, Long>();
    }
}
