package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.starlight.transmission.TransmissionNetworkHelper;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Random;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;

public abstract class TileNetwork<T extends IPrismTransmissionNode> extends TileEntityTick
{
    protected static final Random rand;
    private boolean isNetworkInformed;
    private T cachedNetworkNode;
    private boolean needsNetworkSync;
    
    protected TileNetwork(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.isNetworkInformed = false;
        this.cachedNetworkNode = null;
        this.needsNetworkSync = false;
    }
    
    @Nullable
    public T getNetworkNode() {
        if (this.cachedNetworkNode != null && !this.cachedNetworkNode.getLocationPos().equals((Object)this.func_174877_v())) {
            this.cachedNetworkNode = null;
        }
        if (this.cachedNetworkNode == null) {
            this.cachedNetworkNode = this.resolveNode();
        }
        return this.cachedNetworkNode;
    }
    
    @Nullable
    private T resolveNode() {
        final IPrismTransmissionNode node = WorldNetworkHandler.getNetworkHandler(this.func_145831_w()).getTransmissionNode(this.func_174877_v());
        if (node == null) {
            return null;
        }
        return (T)node;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.func_145831_w().func_201670_d()) {
            if (!this.isNetworkInformed) {
                if (!TransmissionNetworkHelper.isTileInNetwork(this)) {
                    TransmissionNetworkHelper.informNetworkTilePlacement(this);
                }
                this.isNetworkInformed = true;
            }
            if (this.needsNetworkSync) {
                this.doNetworkSync();
            }
        }
    }
    
    protected void doNetworkSync() {
        final T networkNode = this.getNetworkNode();
        if (networkNode != null && networkNode.updateFromTileEntity(this)) {
            this.needsNetworkSync = false;
            this.markForUpdate();
            this.preventNetworkSync();
        }
    }
    
    @Override
    public void markForUpdate() {
        super.markForUpdate();
        this.needsNetworkSync = true;
    }
    
    protected void preventNetworkSync() {
        this.needsNetworkSync = false;
    }
    
    public boolean needsNetworkSync() {
        return this.needsNetworkSync;
    }
    
    public void onBreak() {
    }
    
    public void func_145843_s() {
        super.func_145843_s();
        if (this.func_145831_w() == null || this.func_145831_w().func_201670_d()) {
            return;
        }
        TransmissionNetworkHelper.informNetworkTileRemoval(this);
        this.isNetworkInformed = false;
    }
    
    @Override
    public void writeSaveNBT(final CompoundTag compound) {
        super.writeSaveNBT(compound);
        compound.putBoolean("needsNetworkSync", this.needsNetworkSync);
    }
    
    @Override
    public void readSaveNBT(final CompoundTag compound) {
        super.readSaveNBT(compound);
        this.needsNetworkSync = compound.getBoolean("needsNetworkSync");
    }
    
    static {
        rand = new Random();
    }
}
