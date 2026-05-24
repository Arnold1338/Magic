package hellfirepvp.astralsorcery.common.starlight;

import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import net.minecraft.core.BlockPos;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;

public interface IStarlightSource<T extends ITransmissionSource> extends IStarlightTransmission<T>
{
    @Nonnull
    IIndependentStarlightSource provideNewSourceNode();
    
    @Nonnull
    T provideSourceNode(final BlockPos p0);
    
    boolean needsToRefreshNetworkChain();
    
    void markChainRebuilt();
    
    @Nonnull
    default T provideTransmissionNode(final BlockPos at) {
        return this.provideSourceNode(at);
    }
}
