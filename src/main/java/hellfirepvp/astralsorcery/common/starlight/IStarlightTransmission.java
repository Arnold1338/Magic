package hellfirepvp.astralsorcery.common.starlight;

import net.minecraft.world.level.level.Level;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;

public interface IStarlightTransmission<T extends IPrismTransmissionNode>
{
    @Nullable
    default T getNode() {
        final WorldNetworkHandler netHandler = WorldNetworkHandler.getNetworkHandler(this.getTrWorld());
        return (T)netHandler.getTransmissionNode(this.getTrPos());
    }
    
    @Nonnull
    BlockPos getTrPos();
    
    @Nonnull
    World getTrWorld();
    
    @Nonnull
    T provideTransmissionNode(final BlockPos p0);
}
