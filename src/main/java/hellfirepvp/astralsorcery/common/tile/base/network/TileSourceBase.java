package hellfirepvp.astralsorcery.common.tile.base.network;

import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.starlight.IStarlightTransmission;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import java.util.LinkedList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import java.util.List;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;

public abstract class TileSourceBase<T extends ITransmissionSource> extends TileNetwork<T> implements IStarlightSource<T>, LinkableTileEntity
{
    protected boolean needsNetworkChainRebuild;
    private boolean linked;
    private final List<BlockPos> positions;
    
    protected TileSourceBase(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.needsNetworkChainRebuild = false;
        this.linked = false;
        this.positions = new LinkedList<BlockPos>();
    }
    
    public boolean hasBeenLinked() {
        return this.linked;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.positions.clear();
        if (compound.contains("linked")) {
            final ListTag list = compound.getList("linked", 10);
            for (int i = 0; i < list.size(); ++i) {
                final CompoundTag tag = list.getCompound(i);
                this.positions.add(NBTHelper.readBlockPosFromNBT(tag));
            }
        }
        this.linked = compound.getBoolean("wasLinkedBefore");
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        final ListTag list = new ListTag();
        for (final BlockPos pos : this.positions) {
            final CompoundTag tag = new CompoundTag();
            NBTHelper.writeBlockPosToNBT(pos, tag);
            list.add((Object)tag);
        }
        compound.put("linked", (Tag)list);
        compound.putBoolean("wasLinkedBefore", this.linked);
    }
    
    @Nonnull
    @Override
    public BlockPos getTrPos() {
        return this.func_174877_v();
    }
    
    @Nonnull
    @Override
    public World getTrWorld() {
        return this.func_145831_w();
    }
    
    @Override
    public void onBlockLinkCreate(final Player player, final BlockPos other) {
        if (other.equals((Object)this.func_174877_v())) {
            return;
        }
        if (TransmissionNetworkHelper.createTransmissionLink(this, other)) {
            if (!this.positions.contains(other)) {
                this.positions.add(other);
                this.needsNetworkChainRebuild = true;
                this.markForUpdate();
            }
            if (!this.hasBeenLinked()) {
                this.linked = true;
            }
        }
    }
    
    @Override
    public void onEntityLinkCreate(final Player player, final LivingEntity linked) {
    }
    
    @Override
    public boolean tryLinkBlock(final Player player, final BlockPos other) {
        return !other.equals((Object)this.func_174877_v()) && TransmissionNetworkHelper.canCreateTransmissionLink(this, other);
    }
    
    @Override
    public boolean tryLinkEntity(final Player player, final LivingEntity other) {
        return false;
    }
    
    @Override
    public boolean tryUnlink(final Player player, final BlockPos other) {
        if (other.equals((Object)this.func_174877_v())) {
            return false;
        }
        if (TransmissionNetworkHelper.hasTransmissionLink(this, other)) {
            TransmissionNetworkHelper.removeTransmissionLink(this, other);
            this.positions.remove(other);
            this.needsNetworkChainRebuild = true;
            this.markForUpdate();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean doesAcceptLinks() {
        return false;
    }
    
    @Override
    public List<BlockPos> getLinkedPositions() {
        return this.positions;
    }
    
    @Override
    public boolean needsToRefreshNetworkChain() {
        return this.needsNetworkChainRebuild;
    }
    
    @Override
    public void markChainRebuilt() {
        this.needsNetworkChainRebuild = false;
    }
}
