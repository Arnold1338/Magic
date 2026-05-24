package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.tile.base.TileFakedState;

public class TileTranslucentBlock extends TileFakedState
{
    private UUID playerUUID;
    
    public TileTranslucentBlock() {
        super(TileEntityTypesAS.TRANSLUCENT_BLOCK);
        this.playerUUID = null;
    }
    
    @Nullable
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    public void setPlayerUUID(final UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.markForUpdate();
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.playerUUID = NBTHelper.getUUID(compound, "playerUUID", null);
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        if (this.playerUUID != null) {
            compound.putUUID("playerUUID", this.playerUUID);
        }
    }
}
