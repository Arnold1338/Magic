package hellfirepvp.astralsorcery.common.starlight.transmission.base;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.phys.Vec3;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import java.util.Objects;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import java.util.Collection;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.starlight.transmission.NodeConnection;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;

public class SimplePrismTransmissionNode implements IPrismTransmissionNode
{
    private boolean ignoreBlockCollision;
    private BlockPos thisPos;
    private final Set<BlockPos> sourcesToThis;
    private final Map<BlockPos, PrismNext> nextNodes;
    
    public SimplePrismTransmissionNode(final BlockPos thisPos) {
        this.ignoreBlockCollision = false;
        this.sourcesToThis = new HashSet<BlockPos>();
        this.nextNodes = new HashMap<BlockPos, PrismNext>();
        this.thisPos = thisPos;
    }
    
    @Override
    public BlockPos getLocationPos() {
        return this.thisPos;
    }
    
    public void updateIgnoreBlockCollisionState(final World world, final boolean ignoreBlockCollision) {
        this.ignoreBlockCollision = ignoreBlockCollision;
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(world);
        if (handle != null) {
            boolean anyChange = false;
            for (final PrismNext next : this.nextNodes.values()) {
                final boolean oldState = next.reachable;
                next.reachable = (ignoreBlockCollision || next.rayAssist.isClear(world));
                if (next.reachable != oldState) {
                    anyChange = true;
                }
            }
            if (anyChange) {
                handle.notifyTransmissionNodeChange(this);
            }
        }
    }
    
    public boolean ignoresBlockCollision() {
        return this.ignoreBlockCollision;
    }
    
    @Override
    public boolean notifyUnlink(final World world, final BlockPos to) {
        return this.nextNodes.remove(to) != null;
    }
    
    @Override
    public void notifyLink(final World world, final BlockPos pos) {
        this.addLink(world, pos, true, false);
    }
    
    private void addLink(final World world, final BlockPos pos, final boolean doRayCheck, final boolean previousRayState) {
        final PrismNext nextNode = new PrismNext(this, world, this.thisPos, pos, doRayCheck, previousRayState);
        this.nextNodes.put(pos, nextNode);
    }
    
    @Override
    public boolean notifyBlockChange(final World world, final BlockPos at) {
        boolean anyChange = false;
        for (final PrismNext next : this.nextNodes.values()) {
            if (next.notifyBlockPlace(world, this.thisPos, at)) {
                anyChange = true;
            }
        }
        return anyChange;
    }
    
    @Override
    public void notifySourceLink(final World world, final BlockPos source) {
        this.sourcesToThis.add(source);
    }
    
    @Override
    public void notifySourceUnlink(final World world, final BlockPos source) {
        this.sourcesToThis.remove(source);
    }
    
    @Override
    public List<NodeConnection<IPrismTransmissionNode>> queryNext(final WorldNetworkHandler handler) {
        final List<NodeConnection<IPrismTransmissionNode>> nodes = new LinkedList<NodeConnection<IPrismTransmissionNode>>();
        for (final BlockPos pos : this.nextNodes.keySet()) {
            nodes.add(new NodeConnection<IPrismTransmissionNode>(handler.getTransmissionNode(pos), pos, this.nextNodes.get(pos).reachable));
        }
        return nodes;
    }
    
    @Override
    public List<BlockPos> getSources() {
        return new LinkedList<BlockPos>(this.sourcesToThis);
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
        this.thisPos = NBTHelper.readBlockPosFromNBT(compound);
        this.sourcesToThis.clear();
        this.ignoreBlockCollision = compound.getBoolean("ignoreBlockCollision");
        final ListTag list = compound.getList("sources", 10);
        for (int i = 0; i < list.size(); ++i) {
            this.sourcesToThis.add(NBTHelper.readBlockPosFromNBT(list.getCompound(i)));
        }
        final ListTag nextList = compound.getList("nextList", 10);
        for (int j = 0; j < nextList.size(); ++j) {
            final CompoundTag tag = nextList.getCompound(j);
            final BlockPos next = NBTHelper.readBlockPosFromNBT(tag);
            final boolean oldState = tag.getBoolean("rayState");
            this.addLink(null, next, false, oldState);
        }
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
        NBTHelper.writeBlockPosToNBT(this.thisPos, compound);
        compound.putBoolean("ignoreBlockCollision", this.ignoreBlockCollision);
        final ListTag sources = new ListTag();
        for (final BlockPos source : this.sourcesToThis) {
            final CompoundTag comp = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(source, comp);
            sources.add((Object)comp);
        }
        compound.put("sources", (Tag)sources);
        final ListTag nextList = new ListTag();
        for (final BlockPos next : this.nextNodes.keySet()) {
            final PrismNext prism = this.nextNodes.get(next);
            final CompoundTag pos = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(next, pos);
            pos.putBoolean("rayState", prism.reachable);
            nextList.add((Object)pos);
        }
        compound.put("nextList", (Tag)nextList);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimplePrismTransmissionNode that = (SimplePrismTransmissionNode)o;
        return Objects.equals(this.thisPos, that.thisPos);
    }
    
    @Override
    public int hashCode() {
        return (this.thisPos != null) ? this.thisPos.hashCode() : 0;
    }
    
    private static class PrismNext
    {
        private final SimplePrismTransmissionNode parent;
        private boolean reachable;
        private double distanceSq;
        private final BlockPos pos;
        private RaytraceAssist rayAssist;
        
        private PrismNext(final SimplePrismTransmissionNode parent, final World world, final BlockPos start, final BlockPos end, final boolean doRayTest, final boolean oldRayState) {
            this.parent = parent;
            this.pos = end;
            this.rayAssist = new RaytraceAssist(start, end);
            if (doRayTest) {
                this.reachable = (parent.ignoreBlockCollision || this.rayAssist.isClear(world));
            }
            else {
                this.reachable = oldRayState;
            }
            this.distanceSq = end.func_218138_a((IPosition)Vec3.func_237491_b_((Vector3i)start), false);
        }
        
        private boolean notifyBlockPlace(final World world, final BlockPos connect, final BlockPos at) {
            final Vec3 bPosAt = Vec3.func_237491_b_((Vector3i)at);
            final double dstStart = connect.func_218138_a((IPosition)bPosAt, false);
            final double dstEnd = this.pos.func_218138_a((IPosition)bPosAt, false);
            if (dstStart > this.distanceSq || dstEnd > this.distanceSq) {
                return false;
            }
            final boolean oldState = this.reachable;
            this.reachable = (this.parent.ignoreBlockCollision || this.rayAssist.isClear(world));
            return this.reachable != oldState;
        }
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new SimplePrismTransmissionNode(null);
        }
    }
}
