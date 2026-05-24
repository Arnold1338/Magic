package hellfirepvp.astralsorcery.common.capability;

import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import javax.annotation.Nonnull;
import net.minecraft.world.level.level.material.Fluid;
import net.minecraft.world.level.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.FluidRarityEntry;
import hellfirepvp.astralsorcery.common.data.config.registry.FluidRarityRegistry;
import java.util.Random;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkFluidEntry implements INBTSerializable<CompoundTag>
{
    private FluidStack chunkFluid;
    private int mbAmount;
    private boolean initialized;
    
    public ChunkFluidEntry() {
        this.chunkFluid = FluidStack.EMPTY;
        this.mbAmount = 0;
        this.initialized = false;
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public boolean isEmpty() {
        return this.chunkFluid.isEmpty() || this.mbAmount <= 0;
    }
    
    public void setEmpty() {
        this.chunkFluid = FluidStack.EMPTY;
        this.mbAmount = 0;
    }
    
    public void generate(final long seed) {
        if (this.isInitialized()) {
            return;
        }
        final Random r = new Random(seed);
        final FluidRarityEntry fluidEntry = FluidRarityRegistry.INSTANCE.getRandomValue(r);
        if (fluidEntry != null) {
            this.mbAmount = fluidEntry.getRandomAmount(r);
            this.chunkFluid = new FluidStack(fluidEntry.getFluid(), 1000);
        }
        else {
            this.setEmpty();
        }
        this.initialized = true;
    }
    
    @Nonnull
    public FluidStack drain(final int amount, final IFluidHandler.FluidAction action) {
        if (!this.isInitialized() || this.isEmpty()) {
            return new FluidStack((Fluid)Fluids.field_204546_a, amount);
        }
        final int drainableAmount = Math.min(amount, this.mbAmount);
        final FluidStack drained = this.chunkFluid.copy();
        drained.setAmount(drainableAmount);
        if (action.execute()) {
            this.mbAmount -= drainableAmount;
            if (this.isEmpty()) {
                this.setEmpty();
            }
        }
        return drained;
    }
    
    public CompoundTag serializeNBT() {
        final CompoundTag nbt = new CompoundTag();
        NBTHelper.setFluid(nbt, "chunkFluid", this.chunkFluid);
        nbt.putInt("mbAmount", this.mbAmount);
        nbt.putBoolean("initialized", this.initialized);
        return nbt;
    }
    
    public void deserializeNBT(final CompoundTag nbt) {
        this.chunkFluid = NBTHelper.getFluid(nbt, "chunkFluid");
        this.mbAmount = nbt.getInt("mbAmount");
        this.initialized = nbt.getBoolean("initialized");
    }
}
