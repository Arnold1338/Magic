package hellfirepvp.astralsorcery.common.starlight.network;

import hellfirepvp.astralsorcery.common.data.sync.server.DataLightBlockEndpoints;
import hellfirepvp.astralsorcery.common.data.sync.server.DataLightConnections;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import java.util.function.Consumer;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.LinkedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.server.level.ServerLevel;
import java.util.HashSet;
import java.util.HashMap;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import java.util.List;
import net.minecraft.world.level.ChunkPos;
import java.util.Map;
import java.util.Random;

public class TransmissionWorldHandler
{
    private static final Random rand;
    private final Map<ChunkPos, List<IIndependentStarlightSource>> involvedSourceMap;
    private final Map<IIndependentStarlightSource, List<ChunkPos>> activeChunkMap;
    private final Map<IIndependentStarlightSource, TransmissionChain> cachedSourceChain;
    private final Map<BlockPos, List<IIndependentStarlightSource>> posToSourceMap;
    private final Set<BlockPos> sourcePosBuilding;
    private final RegistryKey<World> dim;
    
    public TransmissionWorldHandler(final RegistryKey<World> dimKey) {
        this.involvedSourceMap = new HashMap<ChunkPos, List<IIndependentStarlightSource>>();
        this.activeChunkMap = new HashMap<IIndependentStarlightSource, List<ChunkPos>>();
        this.cachedSourceChain = new HashMap<IIndependentStarlightSource, TransmissionChain>();
        this.posToSourceMap = new HashMap<BlockPos, List<IIndependentStarlightSource>>();
        this.sourcePosBuilding = new HashSet<BlockPos>();
        this.dim = dimKey;
    }
    
    public void tick(final ServerLevel world) {
        final WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler((World)world);
        for (final Tuple<BlockPos, IIndependentStarlightSource> sourceTuple : handler.getAllSources()) {
            final BlockPos at = (BlockPos)sourceTuple.func_76341_a();
            final IIndependentStarlightSource source = (IIndependentStarlightSource)sourceTuple.func_76340_b();
            if (!this.cachedSourceChain.containsKey(source) && !this.sourcePosBuilding.contains(at)) {
                this.sourcePosBuilding.add(at);
                this.buildNetworkChain((World)world, source, handler, at);
            }
            final List<ChunkPos> activeChunks = this.activeChunkMap.get(source);
            if (activeChunks != null) {
                if (activeChunks.isEmpty()) {
                    continue;
                }
                final TransmissionChain chain = this.cachedSourceChain.get(source);
                final float starlight = source.produceStarlightTick(world, at);
                final IWeakConstellation type = source.getStarlightType();
                if (type == null) {
                    continue;
                }
                final Map<BlockPos, Float> lossMultipliers = chain.getLossMultipliers();
                Float multiplier = null;
                for (final ITransmissionReceiver rec : chain.getEndpointsNodes()) {
                    final BlockPos pos = rec.getLocationPos();
                    multiplier = lossMultipliers.get(pos);
                    if (multiplier != null) {
                        rec.onStarlightReceive((World)world, type, starlight * multiplier);
                    }
                }
                if (starlight > 0.01f) {
                    chain.getTransmissionUpdates().forEach((node, multiplier) -> {
                        if (multiplier >= 0.01f) {
                            node.onTransmissionTick((World)world, starlight * multiplier, type);
                        }
                        return;
                    });
                }
                for (final BlockPos endPointPos : chain.getUncheckedEndpointsBlock()) {
                    MiscUtils.executeWithChunk((IWorldReader)world, endPointPos, () -> {
                        final BlockState endState = world.getBlockState(endPointPos);
                        final Block b = endState.getBlock();
                        if (b instanceof BlockStarlightRecipient) {
                            final Float multiplier2 = lossMultipliers.get(endPointPos);
                            if (multiplier2 != null) {
                                ((BlockStarlightRecipient)b).receiveStarlight((World)world, TransmissionWorldHandler.rand, endPointPos, type, starlight * multiplier2);
                            }
                        }
                        else {
                            final StarlightNetworkRegistry.IStarlightBlockHandler handle = StarlightNetworkRegistry.getStarlightHandler((World)world, endPointPos, endState, type);
                            if (handle != null) {
                                final Float multiplier3 = lossMultipliers.get(endPointPos);
                                if (multiplier3 != null) {
                                    handle.receiveStarlight((World)world, TransmissionWorldHandler.rand, endPointPos, endState, type, starlight * multiplier3);
                                }
                            }
                            else {
                                chain.updatePosAsResolved((World)world, endPointPos);
                            }
                        }
                    });
                }
            }
        }
    }
    
    private void buildNetworkChain(final World world, final IIndependentStarlightSource source, final WorldNetworkHandler handler, final BlockPos sourcePos) {
        TransmissionChain.buildNetworkChain(world, this, source, handler, sourcePos);
    }
    
    void updateNetworkChainData(final World world, final TransmissionChain chain, final IIndependentStarlightSource source, final WorldNetworkHandler handle, final BlockPos sourcePos) {
        this.sourcePosBuilding.remove(sourcePos);
        this.cachedSourceChain.put(source, chain);
        final List<ChunkPos> activeChunks = new LinkedList<ChunkPos>();
        for (final ChunkPos pos : chain.getInvolvedChunks()) {
            final List<IIndependentStarlightSource> sources = this.involvedSourceMap.computeIfAbsent(pos, k -> new LinkedList());
            sources.add(source);
            MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> activeChunks.add(pos));
        }
        if (!activeChunks.isEmpty()) {
            this.activeChunkMap.put(source, activeChunks);
        }
        for (final BlockPos pos2 : chain.getLossMultipliers().keySet()) {
            final List<IIndependentStarlightSource> sources = this.posToSourceMap.computeIfAbsent(pos2, k -> new LinkedList());
            sources.add(source);
        }
        final List<IIndependentStarlightSource> sources2 = this.posToSourceMap.computeIfAbsent(sourcePos, k -> new LinkedList());
        sources2.add(source);
    }
    
    public Collection<TransmissionChain> getTransmissionChains() {
        return (Collection<TransmissionChain>)ImmutableList.copyOf((Collection)this.cachedSourceChain.values());
    }
    
    public void notifyTransmissionNodeChange(final IPrismTransmissionNode node) {
        final BlockPos pos = node.getLocationPos();
        final List<IIndependentStarlightSource> sources = this.posToSourceMap.get(pos);
        if (sources != null) {
            new ArrayList(sources).forEach((Consumer)this::breakSourceNetwork);
        }
    }
    
    public TransmissionChain getSourceChain(final IIndependentStarlightSource source) {
        return this.cachedSourceChain.get(source);
    }
    
    public void breakSourceNetwork(final IIndependentStarlightSource source) {
        final TransmissionChain knownChain = this.cachedSourceChain.get(source);
        if (knownChain != null) {
            for (final ChunkPos chPos : knownChain.getInvolvedChunks()) {
                final List<IIndependentStarlightSource> sources = this.involvedSourceMap.get(chPos);
                if (sources != null) {
                    sources.remove(source);
                    if (!sources.isEmpty()) {
                        continue;
                    }
                    this.involvedSourceMap.remove(chPos);
                }
            }
            for (final BlockPos pos : knownChain.getLossMultipliers().keySet()) {
                final List<IIndependentStarlightSource> sources = this.posToSourceMap.get(pos);
                if (sources != null) {
                    sources.remove(source);
                    if (!sources.isEmpty()) {
                        continue;
                    }
                    this.posToSourceMap.remove(pos);
                }
            }
            SyncDataHolder.executeServer(SyncDataHolder.DATA_LIGHT_CONNECTIONS, DataLightConnections.class, data -> data.removeOldConnectionsThreaded(this.dim, knownChain.getFoundConnections()));
            SyncDataHolder.executeServer(SyncDataHolder.DATA_LIGHT_BLOCK_ENDPOINTS, DataLightBlockEndpoints.class, data -> data.removeEndpoints(this.dim, knownChain.getResolvedNormalBlockPositions()));
        }
        this.activeChunkMap.remove(source);
        this.cachedSourceChain.remove(source);
    }
    
    public void informChunkUnload(final ChunkPos pos) {
        final List<IIndependentStarlightSource> sources = this.involvedSourceMap.get(pos);
        if (sources != null) {
            for (final IIndependentStarlightSource source : sources) {
                final List<ChunkPos> activeChunks = this.activeChunkMap.get(source);
                if (activeChunks != null) {
                    activeChunks.remove(pos);
                    if (!activeChunks.isEmpty()) {
                        continue;
                    }
                    this.activeChunkMap.remove(source);
                }
            }
        }
    }
    
    public void informChunkLoad(final ChunkPos pos) {
        final List<IIndependentStarlightSource> sources = this.involvedSourceMap.get(pos);
        if (sources != null) {
            for (final IIndependentStarlightSource source : sources) {
                final TransmissionChain chain = this.cachedSourceChain.get(source);
                if (chain != null && chain.getInvolvedChunks().contains(pos)) {
                    if (this.activeChunkMap.containsKey(source)) {
                        final List<ChunkPos> positions = this.activeChunkMap.get(source);
                        if (positions.contains(pos)) {
                            continue;
                        }
                        positions.add(pos);
                    }
                    else {
                        final List<ChunkPos> positions = new LinkedList<ChunkPos>();
                        positions.add(pos);
                        this.activeChunkMap.put(source, positions);
                    }
                }
            }
        }
    }
    
    public void clear() {
        this.activeChunkMap.clear();
        this.cachedSourceChain.clear();
        this.involvedSourceMap.clear();
        this.posToSourceMap.clear();
    }
    
    static {
        rand = new Random();
    }
}
