package hellfirepvp.astralsorcery.common.constellation;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.util.world.WorldSeedCache;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ISeedReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import com.google.common.collect.Maps;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class SkyHandler implements ITickHandler
{
    private static final SkyHandler instance;
    private final Map<RegistryKey<World>, WorldContext> worldHandlersServer;
    private final Map<RegistryKey<World>, WorldContext> worldHandlersClient;
    private final Map<RegistryKey<World>, Boolean> skyRevertMap;
    
    private SkyHandler() {
        this.worldHandlersServer = Maps.newHashMap();
        this.worldHandlersClient = Maps.newHashMap();
        this.skyRevertMap = Maps.newHashMap();
    }
    
    public static SkyHandler getInstance() {
        return SkyHandler.instance;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (type == TickEvent.Type.WORLD) {
            final World w = (World)context[0];
            if (!w.func_201670_d() && w instanceof ServerLevel) {
                final RegistryKey<World> dimKey = (RegistryKey<World>)w.dimension();
                this.skyRevertMap.put(dimKey, false);
                WorldContext ctx = this.worldHandlersServer.get(dimKey);
                if (ctx == null) {
                    ctx = this.createContext(MiscUtils.getRandomWorldSeed((ISeedReader)w));
                    this.worldHandlersServer.put(dimKey, ctx);
                }
                ctx.tick(w);
            }
        }
        else {
            this.handleClientTick();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void handleClientTick() {
        final World w = (World)Minecraft.func_71410_x().field_71441_e;
        if (w != null) {
            final RegistryKey<World> dimKey = (RegistryKey<World>)w.dimension();
            WorldContext ctx = this.worldHandlersClient.get(dimKey);
            if (ctx == null) {
                final Optional<Long> seedOpt = WorldSeedCache.getSeedIfPresent(dimKey);
                if (!seedOpt.isPresent()) {
                    return;
                }
                ctx = this.createContext(seedOpt.get());
                this.worldHandlersClient.put(dimKey, ctx);
            }
            ctx.tick(w);
        }
    }
    
    private WorldContext createContext(final long seed) {
        return new WorldContext(seed);
    }
    
    @Nullable
    public static WorldContext getContext(final World world) {
        return getContext(world, world.func_201670_d() ? LogicalSide.CLIENT : LogicalSide.SERVER);
    }
    
    @Nullable
    public static WorldContext getContext(final World world, final LogicalSide dist) {
        if (world == null) {
            return null;
        }
        final RegistryKey<World> dimKey = (RegistryKey<World>)world.dimension();
        if (dist.isClient()) {
            return getInstance().worldHandlersClient.getOrDefault(dimKey, null);
        }
        return getInstance().worldHandlersServer.getOrDefault(dimKey, null);
    }
    
    public void revertWorldTimeTick(final ServerLevel world) {
        final RegistryKey<World> dimKey = (RegistryKey<World>)world.dimension();
        final Boolean state = this.skyRevertMap.get(dimKey);
        if (!world.isClientSide && state != null && !state) {
            this.skyRevertMap.put(dimKey, true);
            world.func_241114_a_(world.func_72820_D() - 1L);
        }
    }
    
    public void clientClearCache() {
        this.worldHandlersClient.clear();
    }
    
    public void informWorldUnload(final World world) {
        this.worldHandlersServer.remove(world.dimension());
        this.worldHandlersClient.remove(world.dimension());
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.WORLD, TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "ConstellationSkyhandler";
    }
    
    static {
        instance = new SkyHandler();
    }
}
