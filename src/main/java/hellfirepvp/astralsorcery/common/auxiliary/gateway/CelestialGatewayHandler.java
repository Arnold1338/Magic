package hellfirepvp.astralsorcery.common.auxiliary.gateway;

import java.util.HashSet;
import java.util.HashMap;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktUpdateGateways;
import net.minecraft.world.level.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import java.util.Collection;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.SidedReference;

public class CelestialGatewayHandler
{
    public static final CelestialGatewayHandler INSTANCE;
    private CelestialGatewayFilter filter;
    private boolean startUp;
    private final SidedReference<Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>>> cache;
    
    private CelestialGatewayHandler() {
        this.filter = null;
        this.startUp = false;
        this.cache = new SidedReference<Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>>>();
    }
    
    private CelestialGatewayFilter getFilter() {
        if (this.filter == null) {
            this.filter = new CelestialGatewayFilter();
        }
        return this.filter;
    }
    
    public void addPosition(final World world, final GatewayCache.GatewayNode node) {
        if (world.func_201670_d()) {
            return;
        }
        final RegistryKey<World> dimKey = (RegistryKey<World>)world.dimension();
        if (!this.cache.getData(LogicalSide.SERVER).map(map -> map.get(dimKey)).isPresent()) {
            this.forceLoad((RegistryKey<World>)world.dimension());
        }
        final Optional<Collection<GatewayCache.GatewayNode>> worldData = this.cache.getData(LogicalSide.SERVER).map(map -> map.get(dimKey));
        if (!worldData.isPresent()) {
            AstralSorcery.log.info("Couldn't add gateway at " + node.getPos() + " - loading the world failed.");
            return;
        }
        final Collection<GatewayCache.GatewayNode> nodes = worldData.get();
        this.getFilter().addDim(dimKey);
        if (!nodes.contains(node)) {
            nodes.add(node);
            this.syncToAll();
        }
    }
    
    public void removePosition(final World world, final BlockPos pos) {
        if (world.func_201670_d()) {
            return;
        }
        final RegistryKey<World> dimKey = (RegistryKey<World>)world.dimension();
        final Optional<Collection<GatewayCache.GatewayNode>> worldData = this.cache.getData(LogicalSide.SERVER).map(map -> map.get(dimKey));
        if (!worldData.isPresent()) {
            return;
        }
        final Collection<GatewayCache.GatewayNode> nodes = worldData.get();
        if (nodes.removeIf(node -> node.getPos().equals((Object)pos))) {
            if (nodes.isEmpty()) {
                this.getFilter().removeDim(dimKey);
            }
            this.syncToAll();
        }
    }
    
    private void forceLoad(final RegistryKey<World> world) {
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        srv.func_71218_a((RegistryKey)world);
    }
    
    public void onServerStart() {
        this.startUp = true;
        final CelestialGatewayFilter filter = this.getFilter();
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        this.startUp = false;
    }
    
    public void onServerStop() {
        this.cache.setData(LogicalSide.SERVER, null);
    }
    
    public void onWorldInit(final WorldEvent.Load event) {
        if (this.startUp) {
            return;
        }
        final IWorld world = event.getWorld();
        if (world.func_201670_d() || !(world instanceof World)) {
            return;
        }
        this.loadIntoCache((World)world);
        this.syncToAll();
    }
    
    public void syncToAll() {
        final PktUpdateGateways pkt = new PktUpdateGateways(this.getGatewayCache(LogicalSide.SERVER));
        PacketChannel.CHANNEL.sendToAll(pkt);
    }
    
    public Collection<GatewayCache.GatewayNode> getGatewaysForWorld(final World world, final LogicalSide side) {
        return this.cache.getData(side).map(data -> data.getOrDefault(world.dimension(), Collections.emptyList())).orElse((Collection<GatewayCache.GatewayNode>)Collections.emptyList());
    }
    
    public Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>> getGatewayCache(final LogicalSide side) {
        return this.cache.getData(side).orElse(Collections.emptyMap());
    }
    
    @Nullable
    public GatewayCache.GatewayNode getGatewayNode(final World world, final LogicalSide side, final BlockPos pos) {
        return this.cache.getData(side).map(data -> data.get(world.dimension())).orElse(Collections.emptyList()).stream().filter(node -> node.getPos().equals((Object)pos)).findFirst().orElse(null);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void updateClientCache(@Nullable final Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>> positions) {
        this.cache.setData(LogicalSide.CLIENT, positions);
    }
    
    private void loadIntoCache(final World world) {
        final GatewayCache cache = (GatewayCache)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE);
        final Map<RegistryKey<World>, Collection<GatewayCache.GatewayNode>> gatewayCache = this.cache.getData(LogicalSide.SERVER).orElse(new HashMap());
        gatewayCache.put((RegistryKey<World>)world.dimension(), new HashSet<GatewayCache.GatewayNode>(cache.getGatewayPositions()));
        this.cache.setData(LogicalSide.SERVER, gatewayCache);
    }
    
    static {
        INSTANCE = new CelestialGatewayHandler();
    }
}
