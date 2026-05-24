package hellfirepvp.astralsorcery.common.starlight;

import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;

public interface IStarlightReceiver<T extends ITransmissionReceiver> extends IStarlightTransmission<T>
{
    @Nonnull
    T provideEndpoint(final BlockPos p0);
    
    @Nonnull
    default T provideTransmissionNode(final BlockPos at) {
        return this.provideEndpoint(at);
    }
}
