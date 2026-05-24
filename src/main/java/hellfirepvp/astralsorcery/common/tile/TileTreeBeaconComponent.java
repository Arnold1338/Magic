package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.tile.base.TileFakedState;

public class TileTreeBeaconComponent extends TileFakedState
{
    private BlockPos treeBeaconPos;
    
    public TileTreeBeaconComponent() {
        super(TileEntityTypesAS.TREE_BEACON_COMPONENT);
        this.treeBeaconPos = BlockPos.field_177992_a;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d() && this.getTicksExisted() % 200 == 0) {
            if (this.getTreeBeaconPos().equals((Object)BlockPos.field_177992_a)) {
                this.removeSelf();
            }
            else {
                final TileTreeBeacon ttb = MiscUtils.getTileAt((IBlockReader)this.func_145831_w(), this.getTreeBeaconPos(), TileTreeBeacon.class, false);
                if (ttb == null) {
                    this.removeSelf();
                }
            }
        }
    }
    
    @Nonnull
    public BlockPos getTreeBeaconPos() {
        return this.treeBeaconPos;
    }
    
    public void setTreeBeaconPos(final BlockPos treeBeaconPos) {
        this.treeBeaconPos = treeBeaconPos;
        this.markForUpdate();
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.treeBeaconPos = NBTHelper.readFromSubTag(compound, "treeBeaconPos", NBTHelper::readBlockPosFromNBT);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        NBTHelper.setAsSubTag(compound, "treeBeaconPos", tag -> NBTHelper.writeBlockPosToNBT(this.treeBeaconPos, tag));
    }
}
