package hellfirepvp.astralsorcery.common.tile.base.network;

import net.minecraft.world.level.level.Level;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import com.google.common.collect.Lists;
import net.minecraft.world.level.entity.player.Player;
import java.util.LinkedList;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import java.util.List;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.starlight.IStarlightTransmission;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;

public abstract class TileTransmissionBase<T extends IPrismTransmissionNode> extends TileNetwork<T> implements IStarlightTransmission<T>, LinkableTileEntity
{
    private final List<BlockPos> positions;
    
    protected TileTransmissionBase(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.positions = new LinkedList<BlockPos>();
    }
    
    @Override
    public boolean onSelect(final Player player) {
        if (player.func_225608_bj_()) {
            for (final BlockPos linkTo : Lists.newArrayList((Iterable)this.getLinkedPositions())) {
                this.tryUnlink(player, linkTo);
            }
            player.func_145747_a((Component)new Component("astralsorcery.misc.link.unlink.all").func_240699_a_(ChatFormatting.GREEN), Util.NIL_UUID);
            return false;
        }
        return true;
    }
    
    public abstract boolean isSingleLink();
    
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
    }
    
    @Override
    public void onBlockLinkCreate(final Player player, final BlockPos other) {
        if (other.equals((Object)this.func_174877_v())) {
            return;
        }
        if (TransmissionNetworkHelper.createTransmissionLink(this, other)) {
            if (this.isSingleLink()) {
                this.positions.clear();
            }
            if (this.isSingleLink() || !this.positions.contains(other)) {
                this.positions.add(other);
                this.markForUpdate();
            }
        }
    }
    
    @Override
    public void onEntityLinkCreate(final Player player, final LivingEntity linked) {
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
            this.markForUpdate();
            return true;
        }
        return false;
    }
    
    @Override
    public List<BlockPos> getLinkedPositions() {
        return this.positions;
    }
}
