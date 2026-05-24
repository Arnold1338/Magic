package hellfirepvp.astralsorcery.common.util.tile;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.IFluidHandler;
import javax.annotation.Nonnull;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.level.level.material.Fluids;
import net.minecraft.world.level.level.material.Fluid;
import net.minecraftforge.fluids.IFluidTank;

public class SimpleSingleFluidTank implements IFluidTank
{
    private int amount;
    private Fluid fluid;
    private int maxCapacity;
    private Runnable onUpdate;
    private boolean allowInput;
    private boolean allowOutput;
    
    private SimpleSingleFluidTank() {
        this.amount = 0;
        this.fluid = Fluids.field_204541_a;
        this.onUpdate = (() -> {});
        this.allowInput = true;
        this.allowOutput = true;
    }
    
    public SimpleSingleFluidTank(final int maxCapacity) {
        this.amount = 0;
        this.fluid = Fluids.field_204541_a;
        this.onUpdate = (() -> {});
        this.allowInput = true;
        this.allowOutput = true;
        this.maxCapacity = Math.max(0, maxCapacity);
    }
    
    public void addUpdateFunction(final Runnable onUpdate) {
        final Runnable prevRunnable = this.onUpdate;
        this.onUpdate = (() -> {
            prevRunnable.run();
            onUpdate.run();
        });
    }
    
    public void setAllowInput(final boolean allowInput) {
        this.allowInput = allowInput;
    }
    
    public void setAllowOutput(final boolean allowOutput) {
        this.allowOutput = allowOutput;
    }
    
    public int getMaxAddable(final int toAdd) {
        return Math.min(toAdd, this.maxCapacity - this.amount);
    }
    
    public int getMaxDrainable(final int toDrain) {
        return Math.min(toDrain, this.amount);
    }
    
    public int addAmount(final int amount) {
        if (this.fluid == Fluids.field_204541_a) {
            return amount;
        }
        final int addable = this.getMaxAddable(amount);
        this.amount += addable;
        if (Math.abs(addable) > 0) {
            this.onUpdate.run();
        }
        return amount - addable;
    }
    
    @Nonnull
    public FluidStack drain(final int amount) {
        if (this.fluid == Fluids.field_204541_a) {
            return FluidStack.EMPTY;
        }
        final int drainable = this.getMaxDrainable(amount);
        this.amount -= drainable;
        final Fluid drainedFluid = this.fluid;
        if (this.amount <= 0) {
            this.setFluid(Fluids.field_204541_a);
        }
        if (Math.abs(drainable) > 0) {
            this.onUpdate.run();
        }
        return new FluidStack(drainedFluid, drainable);
    }
    
    public void setFluid(@Nonnull final Fluid fluid) {
        boolean update = false;
        if (fluid != this.fluid) {
            this.amount = 0;
            update = true;
        }
        this.fluid = fluid;
        if (update) {
            this.onUpdate.run();
        }
    }
    
    @Nonnull
    public FluidStack getFluid() {
        if (this.fluid == Fluids.field_204541_a) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(this.fluid, this.amount);
    }
    
    public int getFluidAmount() {
        return this.amount;
    }
    
    public int getCapacity() {
        return this.maxCapacity;
    }
    
    public boolean isFluidValid(final FluidStack stack) {
        return true;
    }
    
    public boolean canFill() {
        return this.allowInput && this.amount < this.maxCapacity;
    }
    
    public boolean canDrain() {
        return this.allowOutput && this.amount > 0 && this.fluid != Fluids.field_204541_a;
    }
    
    public boolean canFillFluidType(final FluidStack fluidStack) {
        return this.canFill() && (this.fluid == Fluids.field_204541_a || fluidStack.getFluid().equals(this.fluid));
    }
    
    public boolean canDrainFluidType(final FluidStack fluidStack) {
        return this.canDrain() && this.fluid != Fluids.field_204541_a && fluidStack.getFluid().equals(this.fluid);
    }
    
    public float getPercentageFilled() {
        return this.amount / (float)this.maxCapacity;
    }
    
    public int fill(final FluidStack resource, final IFluidHandler.FluidAction action) {
        if (!this.canFillFluidType(resource)) {
            return 0;
        }
        final int maxAdded = resource.getAmount();
        int addable = this.getMaxAddable(maxAdded);
        if (action.execute()) {
            if (addable > 0 && this.fluid == Fluids.field_204541_a) {
                this.setFluid(resource.getFluid());
            }
            addable -= this.addAmount(addable);
        }
        return addable;
    }
    
    @Nonnull
    public FluidStack drain(final FluidStack resource, final IFluidHandler.FluidAction action) {
        if (!this.canDrainFluidType(resource)) {
            return FluidStack.EMPTY;
        }
        return this.drain(resource.getAmount(), action);
    }
    
    @Nonnull
    public FluidStack drain(final int maxDrain, final IFluidHandler.FluidAction action) {
        if (!this.canDrain()) {
            return FluidStack.EMPTY;
        }
        final int maxDrainable = this.getMaxDrainable(maxDrain);
        if (action.execute()) {
            return this.drain(maxDrainable);
        }
        return new FluidStack(this.fluid, maxDrainable);
    }
    
    public CompoundTag writeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt("amt", this.amount);
        tag.putInt("capacity", this.maxCapacity);
        tag.putBoolean("aIn", this.allowInput);
        tag.putBoolean("aOut", this.allowOutput);
        tag.putString("fluid", this.fluid.getRegistryName().toString());
        return tag;
    }
    
    public void readNBT(final CompoundTag tag) {
        this.amount = tag.getInt("amt");
        this.maxCapacity = tag.getInt("capacity");
        this.allowInput = tag.getBoolean("aIn");
        this.allowOutput = tag.getBoolean("aOut");
        this.fluid = (Fluid)ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString("fluid")));
    }
    
    public static SimpleSingleFluidTank deserialize(final CompoundTag tag) {
        final SimpleSingleFluidTank tank = new SimpleSingleFluidTank();
        tank.readNBT(tag);
        return tank;
    }
}
