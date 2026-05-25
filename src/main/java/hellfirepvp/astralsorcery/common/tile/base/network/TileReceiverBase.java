package hellfirepvp.astralsorcery.common.tile.base.network;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.starlight.IStarlightReceiver;
import hellfirepvp.astralsorcery.common.tile.base.TileNetwork;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;

public abstract class TileReceiverBase<T extends ITransmissionReceiver> extends TileNetwork<T> implements IStarlightReceiver<T>, LinkableTileEntity
{
    protected TileReceiverBase(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    
    @Nonnull
    @Override
    public BlockPos getTrPos() {
        return this.getBlockState();
    }
    
    @Nonnull
    @Override
    public Level getTrWorld() {
        return this.getLevel();
    }
    
    @Override
    public void onBlockLinkCreate(final Player player, final BlockPos other) {
    }
    
    @Override
    public void onEntityLinkCreate(final Player player, final LivingEntity linked) {
    }
    
    @Override
    public boolean tryLinkBlock(final Player player, final BlockPos other) {
        return false;
    }
    
    @Override
    public boolean tryLinkEntity(final Player player, final LivingEntity other) {
        return false;
    }
    
    @Override
    public boolean tryUnlink(final Player player, final BlockPos other) {
        return false;
    }
    
    @Override
    public List<BlockPos> getLinkedPositions() {
        return new LinkedList<BlockPos>();
    }
}
