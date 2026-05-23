package hellfirepvp.observerlib.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ObserverHelper {
    private static ObserverHelper helperInstance = null;
    public static Block blockAirRequirement;

    public static ObserverHelper getHelper() {
        return ObserverHelper.helperInstance;
    }

    public static void setHelper(ObserverHelper helperInstance) {
        ObserverHelper.helperInstance = helperInstance;
    }

    @Nonnull
    public abstract <T extends ChangeObserver> ChangeSubscriber<T> observeArea(Level world, BlockPos center, ObserverProvider provider);

    public abstract boolean removeObserver(Level world, BlockPos center);

    @Nullable
    public abstract ChangeSubscriber<? extends ChangeObserver> getSubscriber(Level world, BlockPos center);
}
