package hellfirepvp.astralsorcery.common.starlight.transmission.base;

import java.util.Objects;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.transmission.NodeConnection;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import net.minecraft.world.level.Level;
import java.util.HashSet;
import java.util.Set;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionNode;

public class SimpleTransmissionNode implements ITransmissionNode
{
    private boolean ignoreBlockCollision;
    private boolean nextReachable;
    private BlockPos nextPos;
    private double dstToNextSq;
    private RaytraceAssist assistNext;
    private BlockPos thisPos;
    private Set<BlockPos> sourcesToThis;
    
    public SimpleTransmissionNode(final BlockPos thisPos) {
        this.ignoreBlockCollision = false;
        this.nextReachable = false;
        this.nextPos = null;
        this.dstToNextSq = 0.0;
        this.assistNext = null;
        this.sourcesToThis = new HashSet<BlockPos>();
        this.thisPos = thisPos;
    }
    
    @Override
    public BlockPos getLocationPos() {
        return this.thisPos;
    }
    
    public void updateIgnoreBlockCollisionState(final Level world, final boolean ignoreBlockCollision) {
        this.ignoreBlockCollision = ignoreBlockCollision;
        final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(world);
        if (this.assistNext != null && handle != null) {
            final boolean oldState = this.nextReachable;
            this.nextReachable = (ignoreBlockCollision || this.assistNext.isClear(world));
            if (this.nextReachable != oldState) {
                handle.notifyTransmissionNodeChange(this);
            }
        }
    }
    
    public boolean ignoresBlockCollision() {
        return this.ignoreBlockCollision;
    }
    
    @Override
    public boolean notifyUnlink(final Level world, final BlockPos to) {
        if (to.equals((Object)this.nextPos)) {
            this.nextPos = null;
            this.assistNext = null;
            this.dstToNextSq = 0.0;
            this.nextReachable = false;
            return true;
        }
        return false;
    }
    
    @Override
    public void notifyLink(final Level world, final BlockPos pos) {
        this.addLink(world, pos, true, false);
    }
    
    private void addLink(final Level world, final BlockPos pos, final boolean doRayTest, final boolean oldRayState) {
        this.nextPos = pos;
        this.assistNext = new RaytraceAssist(this.thisPos, this.nextPos);
        if (doRayTest) {
            this.nextReachable = (this.ignoreBlockCollision || this.assistNext.isClear(world));
        }
        else {
            this.nextReachable = oldRayState;
        }
        this.dstToNextSq = pos.func_218138_a((IPosition)Vec3.func_237491_b_((Vec3i)this.thisPos), false);
    }
    
    @Override
    public boolean notifyBlockChange(final Level world, final BlockPos at) {
        if (this.nextPos == null) {
            return false;
        }
        final Vec3 bPosAt = Vec3.func_237491_b_((Vec3i)at);
        final double dstStart = this.thisPos.func_218138_a((IPosition)bPosAt, false);
        final double dstEnd = this.nextPos.func_218138_a((IPosition)bPosAt, false);
        if (dstStart > this.dstToNextSq || dstEnd > this.dstToNextSq) {
            return false;
        }
        final boolean oldState = this.nextReachable;
        this.nextReachable = (this.ignoreBlockCollision || this.assistNext.isClear(world));
        return this.nextReachable != oldState;
    }
    
    @Override
    public void notifySourceLink(final Level world, final BlockPos source) {
        this.sourcesToThis.add(source);
    }
    
    @Override
    public void notifySourceUnlink(final Level world, final BlockPos source) {
        this.sourcesToThis.remove(source);
    }
    
    @Override
    public NodeConnection<IPrismTransmissionNode> queryNextNode(final WorldNetworkHandler handler) {
        if (this.nextPos == null) {
            return null;
        }
        return new NodeConnection<IPrismTransmissionNode>(handler.getTransmissionNode(this.nextPos), this.nextPos, this.nextReachable);
    }
    
    @Override
    public List<BlockPos> getSources() {
        return new ArrayList<BlockPos>(this.sourcesToThis);
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
        if (compound.contains("nextPos")) {
            final CompoundTag tag = compound.func_74775_l("nextPos");
            final BlockPos next = NBTHelper.readBlockPosFromNBT(tag);
            final boolean oldRay = tag.getBoolean("rayState");
            this.addLink(null, next, false, oldRay);
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
        if (this.nextPos != null) {
            final CompoundTag pos = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(this.nextPos, pos);
            pos.putBoolean("rayState", this.nextReachable);
            compound.put("nextPos", (Tag)pos);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimpleTransmissionNode that = (SimpleTransmissionNode)o;
        return Objects.equals(this.thisPos, that.thisPos);
    }
    
    @Override
    public int hashCode() {
        return (this.thisPos != null) ? this.thisPos.hashCode() : 0;
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new SimpleTransmissionNode(null);
        }
    }
}
