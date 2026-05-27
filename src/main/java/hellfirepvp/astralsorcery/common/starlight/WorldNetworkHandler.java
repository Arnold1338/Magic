package hellfirepvp.astralsorcery.common.starlight;

import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.starlight.transmission.NodeConnection;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Tuple;
import java.util.Collection;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import net.minecraft.core.BlockPos;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.data.world.LightNetworkBuffer;

public class WorldNetworkHandler
{
    private final LightNetworkBuffer buffer;
    private final Level world;
    
    public WorldNetworkHandler(final LightNetworkBuffer lightNetworkBuffer, final Level world) {
        this.buffer = lightNetworkBuffer;
        this.world = world;
    }
    
    public Level getWorld() {
        return this.world;
    }
    
    public static WorldNetworkHandler getNetworkHandler(final Level world) {
        return ((LightNetworkBuffer)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_STARLIGHT_NETWORK)).getNetworkHandler(world);
    }
    
    public void informBlockChange(final BlockPos at) {
        final List<LightNetworkBuffer.ChunkSectionNetworkData> relatedData = this.getAffectedChunkSections(at);
        if (relatedData.isEmpty()) {
            return;
        }
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.getWorld());
        for (final LightNetworkBuffer.ChunkSectionNetworkData data : relatedData) {
            if (data == null) {
                continue;
            }
            final Collection<IPrismTransmissionNode> transmissionNodes = data.getAllTransmissionNodes();
            for (final IPrismTransmissionNode node : transmissionNodes) {
                if (node.notifyBlockChange(this.getWorld(), at) && handle != null) {
                    handle.notifyTransmissionNodeChange(node);
                }
            }
        }
    }
    
    public void attemptAutoLinkTo(final BlockPos at) {
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.world);
        for (final Tuple<BlockPos, IIndependentStarlightSource> source : this.getAllSources()) {
            if (!((IIndependentStarlightSource)source.getB()).providesAutoLink()) {
                continue;
            }
            if (((BlockPos)source.getA()).func_218138_a((IPosition)Vec3.func_237491_b_((Vec3i)at), false) > 256.0) {
                continue;
            }
            final IPrismTransmissionNode node = this.getTransmissionNode((BlockPos)source.getA());
            if (node == null) {
                AstralSorcery.log.warn("Didn't find a TransmissionNode at a position that's supposed to be a source!");
                AstralSorcery.log.warn("Details: Dim=" + this.getWorld().dimension().func_240901_a_() + " at " + source.getA());
            }
            else if (!(node instanceof ITransmissionSource)) {
                AstralSorcery.log.warn("Found TransmissionNode that isn't a source at a source position!");
                AstralSorcery.log.warn("Details: Dim=" + this.getWorld().dimension().func_240901_a_() + " at " + source.getA());
            }
            else {
                final ITransmissionSource sourceNode = (ITransmissionSource)node;
                if (sourceNode.getLocationPos().getY() <= at.getY()) {
                    continue;
                }
                sourceNode.notifyLink(this.getWorld(), at);
                this.markDirty((Vec3i)at, (Vec3i)source.getA());
                if (handle == null) {
                    continue;
                }
                handle.notifyTransmissionNodeChange(sourceNode);
            }
        }
    }
    
    public void removeAutoLinkTo(final BlockPos at) {
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.world);
        for (final Tuple<BlockPos, IIndependentStarlightSource> source : this.getAllSources()) {
            if (!((IIndependentStarlightSource)source.getB()).providesAutoLink()) {
                continue;
            }
            if (((BlockPos)source.getA()).func_218138_a((IPosition)Vec3.func_237491_b_((Vec3i)at), false) > 256.0) {
                continue;
            }
            final IPrismTransmissionNode node = this.getTransmissionNode((BlockPos)source.getA());
            if (node == null) {
                AstralSorcery.log.warn("Didn't find a TransmissionNode at a position that's supposed to be a source!");
                AstralSorcery.log.warn("Details: Dim=" + this.getWorld().dimension().func_240901_a_() + " at " + source.getA());
            }
            else if (!(node instanceof ITransmissionSource)) {
                AstralSorcery.log.warn("Found TransmissionNode that isn't a source at a source position!");
                AstralSorcery.log.warn("Details: Dim=" + this.getWorld().dimension().func_240901_a_() + " at " + source.getA());
            }
            else {
                final ITransmissionSource sourceNode = (ITransmissionSource)node;
                if (!sourceNode.notifyUnlink(this.getWorld(), at)) {
                    continue;
                }
                this.markDirty((Vec3i)at, (Vec3i)source.getA());
                if (handle == null) {
                    continue;
                }
                handle.notifyTransmissionNodeChange(sourceNode);
            }
        }
    }
    
    @Nullable
    public IPrismTransmissionNode getTransmissionNode(@Nullable final BlockPos pos) {
        if (pos == null) {
            return null;
        }
        final LightNetworkBuffer.ChunkSectionNetworkData section = this.getNetworkData(pos);
        if (section != null) {
            return section.getTransmissionNode(pos);
        }
        return null;
    }
    
    public void markDirty(final Vec3i... positions) {
        for (final Vec3i pos : positions) {
            this.buffer.markDirty(new net.minecraft.core.Vec3i(pos.getX(), pos.getY(), pos.getZ()));
        }
    }
    
    @Nullable
    public IIndependentStarlightSource getSourceAt(final BlockPos pos) {
        return this.buffer.getSource(pos);
    }
    
    public Collection<Tuple<BlockPos, IIndependentStarlightSource>> getAllSources() {
        return this.buffer.getAllSources();
    }
    
    public void removeSource(final IStarlightSource<?> source) {
        this.removeThisSourceFromNext(source);
        this.removeThisNextFromSources(source);
        this.buffer.removeSource(source.getTrPos());
    }
    
    public void removeTransmission(final IStarlightTransmission<?> transmission) {
        this.removeThisSourceFromNext(transmission);
        this.removeThisNextFromSources(transmission);
        this.buffer.removeTransmission(transmission.getTrPos());
    }
    
    public void addNewSourceTile(final IStarlightSource<?> source) {
        this.buffer.addSource(source, source.getTrPos());
        this.linkNextToThisSources(source);
    }
    
    public void addTransmissionTile(final IStarlightTransmission<?> transmission) {
        this.buffer.addTransmission(transmission, transmission.getTrPos());
        this.linkNextToThisSources(transmission);
    }
    
    private void removeThisNextFromSources(final IStarlightTransmission<?> tr) {
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.getWorld());
        if (handle == null) {
            return;
        }
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            new Throwable().printStackTrace();
            AstralSorcery.log.warn("Could not find transmission node for Transmission tile '" + tr.getClass().getSimpleName() + "'");
            AstralSorcery.log.warn("This is an implementation error. Report it along with the steps to create this, if you come across this.");
            return;
        }
        for (final BlockPos pos : node.getSources()) {
            final IPrismTransmissionNode sourceNode = this.getTransmissionNode(pos);
            if (sourceNode != null) {
                handle.notifyTransmissionNodeChange(sourceNode);
            }
        }
    }
    
    private void removeThisSourceFromNext(final IStarlightTransmission<?> tr) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            AstralSorcery.log.warn("Could not find transmission node for Transmission tile '" + tr.getClass().getSimpleName() + "'");
            AstralSorcery.log.warn("This is an implementation error. Report it along with the steps to create this, if you come across this.");
            return;
        }
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.getWorld());
        if (handle != null) {
            handle.notifyTransmissionNodeChange(node);
        }
        final BlockPos thisPos = tr.getTrPos();
        final List<NodeConnection<IPrismTransmissionNode>> nodeConnections = node.queryNext(this);
        for (final NodeConnection<IPrismTransmissionNode> connection : nodeConnections) {
            if (connection.getNode() != null) {
                connection.getNode().notifySourceUnlink(this.getWorld(), thisPos);
                if (handle == null) {
                    continue;
                }
                handle.notifyTransmissionNodeChange(connection.getNode());
            }
        }
    }
    
    private void linkNextToThisSources(final IStarlightTransmission<?> tr) {
        final IPrismTransmissionNode node = (IPrismTransmissionNode)tr.getNode();
        if (node == null) {
            AstralSorcery.log.warn("Previously added Transmission tile '" + tr.getClass().getSimpleName() + "' didn't create a Transmission node!");
            AstralSorcery.log.warn("This is an implementation error. Report it along with the steps to create this, if you come across this.");
            return;
        }
        final BlockPos thisPos = tr.getTrPos();
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(this.getWorld());
        final List<LightNetworkBuffer.ChunkSectionNetworkData> dataList = this.getAffectedChunkSections(tr.getTrPos());
        for (final LightNetworkBuffer.ChunkSectionNetworkData data : dataList) {
            if (data == null) {
                continue;
            }
            for (final IPrismTransmissionNode otherNode : data.getAllTransmissionNodes()) {
                final List<NodeConnection<IPrismTransmissionNode>> nodeConnections = otherNode.queryNext(this);
                for (final NodeConnection<IPrismTransmissionNode> connection : nodeConnections) {
                    if (connection.getTo().equals((Object)thisPos)) {
                        node.notifySourceLink(this.getWorld(), otherNode.getLocationPos());
                        if (handle == null) {
                            continue;
                        }
                        handle.notifyTransmissionNodeChange(otherNode);
                    }
                }
            }
        }
    }
    
    private List<LightNetworkBuffer.ChunkSectionNetworkData> getAffectedChunkSections(final BlockPos centralPos) {
        final List<LightNetworkBuffer.ChunkSectionNetworkData> dataList = new LinkedList<LightNetworkBuffer.ChunkSectionNetworkData>();
        final ChunkPos central = new ChunkPos(centralPos);
        final int posYLevel = Mth.getDescriptionId(centralPos.getY(), 0, 255);
        for (int xx = -1; xx <= 1; ++xx) {
            for (int zz = -1; zz <= 1; ++zz) {
                for (int yy = -1; yy <= 1; ++yy) {
                    BlockPos pos = central.func_206849_h();
                    pos = pos.offset(xx * 16, Mth.getDescriptionId(posYLevel + yy * 16, 0, 255), zz * 16);
                    this.queryData(pos, dataList);
                }
            }
        }
        return dataList;
    }
    
    private void queryData(final BlockPos pos, final List<LightNetworkBuffer.ChunkSectionNetworkData> out) {
        final LightNetworkBuffer.ChunkSectionNetworkData data = this.buffer.getSectionData(pos);
        if (data != null && !data.isEmpty()) {
            out.add(data);
        }
    }
    
    @Nullable
    private LightNetworkBuffer.ChunkSectionNetworkData getNetworkData(final BlockPos at) {
        final LightNetworkBuffer.ChunkSectionNetworkData data = this.buffer.getSectionData(at);
        if (data != null && !data.isEmpty()) {
            return data;
        }
        return null;
    }
}
