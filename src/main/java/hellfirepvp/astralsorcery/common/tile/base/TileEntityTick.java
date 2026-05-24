package hellfirepvp.astralsorcery.common.tile.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.observerlib.api.ObserverHelper;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.observerlib.common.change.ChangeObserverStructure;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TileEntityTick extends TileEntitySynchronized implements ITickableTileEntity, TileRequiresMultiblock
{
    private boolean doesSeeSky;
    private int lastUpdateTick;
    private ChangeSubscriber<ChangeObserverStructure> structureMatch;
    private boolean hasMultiblock;
    protected int ticksExisted;
    
    protected TileEntityTick(final BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.doesSeeSky = false;
        this.lastUpdateTick = -1;
        this.hasMultiblock = false;
        this.ticksExisted = 0;
    }
    
    public void func_73660_a() {
        if (this.ticksExisted == 0) {
            this.onFirstTick();
        }
        ++this.ticksExisted;
    }
    
    @Nullable
    public StructureType getRequiredStructureType() {
        return null;
    }
    
    public boolean seesSkyInNoSkyWorlds() {
        return false;
    }
    
    protected void onFirstTick() {
    }
    
    public int getTicksExisted() {
        return this.ticksExisted;
    }
    
    public boolean doesSeeSky() {
        if (this.func_145831_w().func_201670_d()) {
            return this.doesSeeSky;
        }
        if (this.lastUpdateTick == -1 || this.ticksExisted - this.lastUpdateTick >= 20) {
            this.lastUpdateTick = this.ticksExisted;
            final boolean prevSky = this.doesSeeSky;
            final boolean newSky = MiscUtils.canSeeSky(this.func_145831_w(), this.func_174877_v().above(), true, this.seesSkyInNoSkyWorlds(), this.doesSeeSky);
            if (prevSky != newSky) {
                this.notifySkyStateUpdate(prevSky, newSky);
                this.doesSeeSky = newSky;
                this.markForUpdate();
            }
        }
        return this.doesSeeSky;
    }
    
    public boolean hasMultiblock() {
        if (this.func_145831_w().func_201670_d()) {
            return this.hasMultiblock;
        }
        if (this.getRequiredStructureType() == null) {
            this.refreshMatcher();
            this.resetMultiblockState();
            return false;
        }
        this.refreshMatcher();
        if (this.structureMatch == null) {
            this.structureMatch = this.getRequiredStructureType().observe(this.func_145831_w(), this.func_174877_v());
        }
        final boolean prevFound = this.hasMultiblock;
        final boolean found = this.structureMatch.isValid(this.func_145831_w());
        if (prevFound != found) {
            LogCategory.STRUCTURE_MATCH.info(() -> "Structure match updated: " + this.getClass().getName() + " at " + this.func_174877_v() + " (" + this.hasMultiblock + " -> " + found + ")");
            this.notifyMultiblockStateUpdate(prevFound, found);
            this.hasMultiblock = found;
            this.markForUpdate();
        }
        return this.hasMultiblock;
    }
    
    private void refreshMatcher() {
        final StructureType struct = this.getRequiredStructureType();
        if (this.structureMatch != null) {
            final ResourceLocation key = ((ChangeObserverStructure)this.structureMatch.getObserver()).getProviderRegistryName();
            if (struct == null || !key.equals((Object)struct.getRegistryName())) {
                ObserverHelper.getHelper().removeObserver(this.func_145831_w(), this.func_174877_v());
                this.structureMatch = null;
            }
        }
        if (struct == null && ObserverHelper.getHelper().getSubscriber(this.func_145831_w(), this.func_174877_v()) != null) {
            ObserverHelper.getHelper().removeObserver(this.func_145831_w(), this.func_174877_v());
        }
    }
    
    private void resetMultiblockState() {
        if (this.hasMultiblock) {
            this.notifyMultiblockStateUpdate(true, false);
            this.hasMultiblock = false;
            this.markForUpdate();
        }
    }
    
    protected void notifySkyStateUpdate(final boolean doesSeeSkyPrev, final boolean doesSeeSkyNow) {
    }
    
    protected void notifyMultiblockStateUpdate(final boolean hadMultiblockPrev, final boolean hasMultiblockNow) {
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.ticksExisted = compound.getInt("ticksExisted");
        this.doesSeeSky = compound.getBoolean("doesSeeSky");
        this.hasMultiblock = compound.getBoolean("hasMultiblock");
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putInt("ticksExisted", this.ticksExisted);
        compound.putBoolean("doesSeeSky", this.doesSeeSky);
        compound.putBoolean("hasMultiblock", this.hasMultiblock);
    }
}
