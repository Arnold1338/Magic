package hellfirepvp.astralsorcery.common.starlight.network;

import java.util.Objects;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import java.util.function.BiFunction;
import hellfirepvp.astralsorcery.common.starlight.transmission.NodeConnection;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.data.sync.server.DataLightBlockEndpoints;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.data.sync.server.DataLightConnections;
import hellfirepvp.astralsorcery.common.data.sync.SyncDataHolder;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraft.core.BlockPos;
import java.util.Map;
import java.util.List;
import net.minecraft.world.level.ChunkPos;
import java.util.Set;

public class TransmissionChain
{
    private final Set<ChunkPos> involvedChunks;
    private final List<LightConnection> foundConnections;
    private final Map<BlockPos, Float> remainMultiplierMap;
    private final Set<BlockPos> uncheckedEndpointsBlock;
    private final Set<BlockPos> resolvedNormalBlockPositions;
    private final Set<ITransmissionReceiver> endpointsNodes;
    private final Map<IPrismTransmissionNode, Float> transmissionUpdateMap;
    private final WorldNetworkHandler handler;
    private final IPrismTransmissionNode sourceNode;
    
    private TransmissionChain(final WorldNetworkHandler netHandler, final IPrismTransmissionNode sourceNode) {
        this.involvedChunks = new HashSet<ChunkPos>();
        this.foundConnections = new LinkedList<LightConnection>();
        this.remainMultiplierMap = new HashMap<BlockPos, Float>();
        this.uncheckedEndpointsBlock = new HashSet<BlockPos>();
        this.resolvedNormalBlockPositions = new HashSet<BlockPos>();
        this.endpointsNodes = new HashSet<ITransmissionReceiver>();
        this.transmissionUpdateMap = new HashMap<IPrismTransmissionNode, Float>();
        this.handler = netHandler;
        this.sourceNode = sourceNode;
    }
    
    public static void buildNetworkChain(final Level world, final TransmissionWorldHandler handle, final IIndependentStarlightSource source, final WorldNetworkHandler netHandler, final BlockPos sourcePos) {
        final TransmissionChain chain = buildFromSource(netHandler, sourcePos);
        handle.updateNetworkChainData(world, chain, source, netHandler, sourcePos);
        SyncDataHolder.executeServer(SyncDataHolder.DATA_LIGHT_CONNECTIONS, DataLightConnections.class, data -> data.updateNewConnectionsThreaded((ResourceKey<Level>)netHandler.getWorld().dimension(), chain.getFoundConnections()));
        SyncDataHolder.executeServer(SyncDataHolder.DATA_LIGHT_BLOCK_ENDPOINTS, DataLightBlockEndpoints.class, data -> data.updateNewEndpoints((ResourceKey<Level>)netHandler.getWorld().dimension(), chain.getResolvedNormalBlockPositions()));
    }
    
    private static TransmissionChain buildFromSource(final WorldNetworkHandler netHandler, final BlockPos at) {
        TransmissionChain chain = new TransmissionChain(netHandler, null);
        final IPrismTransmissionNode node = netHandler.getTransmissionNode(at);
        if (node != null) {
            chain = new TransmissionChain(netHandler, node);
            chain.recBuildChain(node, 1.0f, new LinkedList<BlockPos>());
        }
        chain.calculateInvolvedChunks();
        chain.resolveLoadedEndpoints(netHandler.getWorld());
        return chain;
    }
    
    private void resolveLoadedEndpoints(final Level world) {
        for (final BlockPos pos : this.uncheckedEndpointsBlock) {
            MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                final BlockState state = world.getBlockState(pos);
                final Block b = state.getBlock();
                if (!(b instanceof BlockStarlightRecipient)) {
                    this.resolvedNormalBlockPositions.add(pos);
                }
            });
        }
    }
    
    protected void updatePosAsResolved(final Level world, final BlockPos pos) {
        if (this.uncheckedEndpointsBlock.contains(pos) && !this.resolvedNormalBlockPositions.contains(pos)) {
            this.resolvedNormalBlockPositions.add(pos);
            SyncDataHolder.executeServer(SyncDataHolder.DATA_LIGHT_BLOCK_ENDPOINTS, DataLightBlockEndpoints.class, data -> data.updateNewEndpoint((ResourceKey<Level>)world.dimension(), pos));
        }
    }
    
    private void recBuildChain(final IPrismTransmissionNode node, final float lossMultiplier, final LinkedList<BlockPos> prevPath) {
        if (lossMultiplier <= 0.001f) {

        }
        final CrystalAttributes lensProperties = node.getTransmissionProperties();
        final float lossPerc = lossMultiplier * CrystalCalculations.getThroughputMultiplier(lensProperties);
        final float nextHopLossPerc = lossPerc * node.getTransmissionThroughputMultiplier();
        final float transmissionPerc = lossPerc * node.getTransmissionConsumptionMultiplier() * CrystalCalculations.getThroughputEffectMultiplier(lensProperties);
        final List<NodeConnection<IPrismTransmissionNode>> next = node.queryNext(this.handler);
        final float nextLoss = nextHopLossPerc / next.size();
        prevPath.push(node.getLocationPos());
        if (node.needsTransmissionUpdate()) {
            this.transmissionUpdateMap.put(node, transmissionPerc);
        }
        for (final NodeConnection<IPrismTransmissionNode> nextNode : next) {
            final IPrismTransmissionNode trNode = nextNode.getNode();
            if (nextNode.canConnect()) {
                final BlockPos nextPos = nextNode.getTo();
                this.addIfNonExistentConnection(node.getLocationPos(), nextPos);
                if (prevPath.contains(nextPos)) {

                }
                this.remainMultiplierMap.merge(nextPos, nextLoss, Float::sum);
                if (trNode != null) {
                    if (trNode instanceof ITransmissionReceiver) {
                        if (this.endpointsNodes.contains(trNode)) {

                        }
                        this.endpointsNodes.add((ITransmissionReceiver)trNode);
                    }
                    else {
                        this.recBuildChain(trNode, nextLoss, prevPath);
                    }
                }
                else {
                    this.uncheckedEndpointsBlock.add(nextPos);
                }
            }
        }
        prevPath.pop();
    }
    
    private void calculateInvolvedChunks() {
        for (final BlockPos nodePos : this.remainMultiplierMap.keySet()) {
            this.involvedChunks.add(new ChunkPos(nodePos));
        }
    }
    
    public Set<BlockPos> getResolvedNormalBlockPositions() {
        return this.resolvedNormalBlockPositions;
    }
    
    public IPrismTransmissionNode getSourceNode() {
        return this.sourceNode;
    }
    
    private void addIfNonExistentConnection(final BlockPos start, final BlockPos end) {
        final LightConnection newCon = new LightConnection(start, end);
        if (!this.foundConnections.contains(newCon)) {
            this.foundConnections.add(newCon);
        }
    }
    
    public Map<IPrismTransmissionNode, Float> getTransmissionUpdates() {
        return this.transmissionUpdateMap;
    }
    
    public Collection<ChunkPos> getInvolvedChunks() {
        return this.involvedChunks;
    }
    
    public Map<BlockPos, Float> getLossMultipliers() {
        return this.remainMultiplierMap;
    }
    
    public List<LightConnection> getFoundConnections() {
        return this.foundConnections;
    }
    
    public Set<ITransmissionReceiver> getEndpointsNodes() {
        return this.endpointsNodes;
    }
    
    public Set<BlockPos> getUncheckedEndpointsBlock() {
        return this.uncheckedEndpointsBlock;
    }
    
    public static class LightConnection
    {
        private final BlockPos start;
        private final BlockPos end;
        
        public LightConnection(final BlockPos start, final BlockPos end) {
            this.start = start;
            this.end = end;
        }
        
        public BlockPos getStart() {
            return this.start;
        }
        
        public BlockPos getEnd() {
            return this.end;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final LightConnection that = (LightConnection)o;
            return Objects.equals(this.end, that.end) && Objects.equals(this.start, that.start);
        }
        
        @Override
        public int hashCode() {
            int result = (this.start != null) ? this.start.hashCode() : 0;
            result = 31 * result + ((this.end != null) ? this.end.hashCode() : 0);
            return result;
        }
    }
}
