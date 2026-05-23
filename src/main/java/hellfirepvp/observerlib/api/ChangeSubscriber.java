package hellfirepvp.observerlib.api;

import hellfirepvp.observerlib.api.block.BlockChangeSet;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public interface ChangeSubscriber<T extends ChangeObserver> {
    @Nonnull
    T getObserver();

    @Nonnull
    BlockChangeSet getCurrentChangeSet();

    boolean isValid(Level world);
}
