package hellfirepvp.astralsorcery.common.event;

import hellfirepvp.astralsorcery.common.perk.source.ModifierSourceProvider;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.Event;

public class ASRegistryEvents
{
    public static class ModifierSourceRegister extends Event
    {
        private final Consumer<ModifierSourceProvider<?>> registrar;
        
        public ModifierSourceRegister(final Consumer<ModifierSourceProvider<?>> registrar) {
            this.registrar = registrar;
        }
        
        public void registerSource(final ModifierSourceProvider<?> sourceProvider) {
            this.registrar.accept(sourceProvider);
        }
    }
}
