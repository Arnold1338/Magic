package hellfirepvp.astralsorcery.common.event;

import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import net.minecraftforge.eventbus.api.Event;

public class StarlightNetworkEvent
{
    public static class TransmissionRegister extends Event
    {
        private final TransmissionClassRegistry registry;
        
        public TransmissionRegister(final TransmissionClassRegistry registry) {
            this.registry = registry;
        }
        
        public TransmissionClassRegistry getRegistry() {
            return this.registry;
        }
    }
    
    public static class SourceProviderRegistry extends Event
    {
        private final SourceClassRegistry registry;
        
        public SourceProviderRegistry(final SourceClassRegistry registry) {
            this.registry = registry;
        }
        
        public SourceClassRegistry getRegistry() {
            return this.registry;
        }
    }
}
