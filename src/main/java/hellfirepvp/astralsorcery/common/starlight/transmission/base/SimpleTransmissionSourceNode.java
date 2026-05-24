package hellfirepvp.astralsorcery.common.starlight.transmission.base;

import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionSource;

public class SimpleTransmissionSourceNode extends SimplePrismTransmissionNode implements ITransmissionSource
{
    public SimpleTransmissionSourceNode(final BlockPos thisPos) {
        super(thisPos);
    }
    
    @Override
    public IIndependentStarlightSource provideNewIndependentSource(final IStarlightSource source) {
        return source.provideNewSourceNode();
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new SimpleTransmissionSourceNode(null);
        }
    }
}
