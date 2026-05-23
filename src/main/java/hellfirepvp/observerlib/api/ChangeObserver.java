package hellfirepvp.observerlib.api;

import hellfirepvp.observerlib.api.block.BlockChangeSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;

public abstract class ChangeObserver {
    private final ResourceLocation providerRegistryName;

    public ChangeObserver(ResourceLocation providerRegistryName) {
        this.providerRegistryName = providerRegistryName;
    }

    @Nonnull
    public final ResourceLocation getProviderRegistryName() {
        return this.providerRegistryName;
    }

    public abstract void initialize(LevelAccessor world, BlockPos center);

    @Nonnull
    public abstract ObservableArea getObservableArea();

    public abstract boolean notifyChange(Level world, BlockPos center, BlockChangeSet changeSet);

    public abstract void readFromNBT(CompoundTag tag);

    public abstract void writeToNBT(CompoundTag tag);
}
