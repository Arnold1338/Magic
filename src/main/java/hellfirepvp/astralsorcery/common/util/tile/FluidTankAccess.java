package hellfirepvp.astralsorcery.common.util.tile;

import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraftforge.fluids.FluidStack;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.Capability;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.IFluidTank;
import java.util.HashSet;
import java.util.Set;

public class FluidTankAccess
{
    private final Set<AccessibleTank> tanks;
    
    public FluidTankAccess() {
        this.tanks = new HashSet<AccessibleTank>();
    }
    
    public void putTank(final int tankId, final IFluidTank tank, final Direction... sides) {
        this.tanks.add(new AccessibleTank(tankId, tank, sides));
    }
    
    public void putTank(final int tankId, final IFluidTank tank, final Predicate<Direction> accessibleSides) {
        this.tanks.add(new AccessibleTank(tankId, tank, (Predicate)accessibleSides));
    }
    
    private boolean hasTanksForSide(final Direction dir) {
        return dir == null || MiscUtils.contains(this.tanks, tank -> tank.accessibleSides.test(dir));
    }
    
    public boolean hasCapability(final Capability<?> capability, @Nullable final Direction facing) {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY == capability && this.hasTanksForSide(facing);
    }
    
    public LazyOptional<IFluidHandler> getCapability(@Nullable final Direction facing) {
        final Set<AccessibleTank> available = (Set<AccessibleTank>)((facing == null) ? this.tanks : ((Set<? super Object>)this.tanks.stream().filter(t -> t.isAccessible(facing)).collect((Collector<? super Object, ?, Set<? super Object>>)Collectors.toSet())));
        return (LazyOptional<IFluidHandler>)(available.isEmpty() ? LazyOptional.empty() : LazyOptional.of(() -> new SidedAccess(available)));
    }
    
    private static class SidedAccess implements IFluidHandler
    {
        private final Set<AccessibleTank> tanks;
        
        private SidedAccess(final Set<AccessibleTank> accessibleTanks) {
            this.tanks = accessibleTanks;
        }
        
        private Optional<AccessibleTank> getTank(final int id) {
            return this.tanks.stream().filter(tank -> tank.getId() == id).findFirst();
        }
        
        public int getTanks() {
            return this.tanks.size();
        }
        
        @Nonnull
        public FluidStack getFluidInTank(final int tank) {
            return this.getTank(tank).map(t -> t.getTank().getFluid()).orElse(FluidStack.EMPTY);
        }
        
        public int getTankCapacity(final int tank) {
            return this.getTank(tank).map(t -> t.getTank().getCapacity()).orElse(0);
        }
        
        public boolean isFluidValid(final int tank, @Nonnull final FluidStack stack) {
            return this.getTank(tank).map(t -> t.getTank().isFluidValid(stack)).orElse(false);
        }
        
        public int fill(final FluidStack resource, final IFluidHandler.FluidAction action) {
            for (final AccessibleTank tank : this.tanks) {
                final int filled = tank.getTank().fill(resource, action);
                if (filled > 0) {
                    return filled;
                }
            }
            return 0;
        }
        
        @Nonnull
        public FluidStack drain(final FluidStack resource, final IFluidHandler.FluidAction action) {
            for (final AccessibleTank tank : this.tanks) {
                final FluidStack drained = tank.getTank().drain(resource, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
            return FluidStack.EMPTY;
        }
        
        @Nonnull
        public FluidStack drain(final int maxDrain, final IFluidHandler.FluidAction action) {
            for (final AccessibleTank tank : this.tanks) {
                final FluidStack drained = tank.getTank().drain(maxDrain, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
            return FluidStack.EMPTY;
        }
    }
    
    private static class AccessibleTank
    {
        private final int id;
        private final IFluidTank tank;
        private final Predicate<Direction> accessibleSides;
        
        private AccessibleTank(final int id, final IFluidTank tank, final Direction... sides) {
            this(id, tank, side -> Arrays.asList(sides).contains(side));
        }
        
        private AccessibleTank(final int id, final IFluidTank tank, final Predicate<Direction> accessibleSides) {
            this.id = id;
            this.tank = tank;
            this.accessibleSides = accessibleSides;
        }
        
        private IFluidTank getTank() {
            return this.tank;
        }
        
        private int getId() {
            return this.id;
        }
        
        private boolean isAccessible(final Direction side) {
            return this.accessibleSides.test(side);
        }
    }
}
