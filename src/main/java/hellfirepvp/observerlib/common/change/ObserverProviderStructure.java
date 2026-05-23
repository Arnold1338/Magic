package hellfirepvp.observerlib.common.change;

import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ObserverProvider;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.observerlib.common.registry.RegistryStructures;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ObserverProviderStructure extends ObserverProvider {
    public ObserverProviderStructure(ResourceLocation registryName) { super(registryName); }

    @Nonnull @Override
    public ChangeObserver provideObserver() {
        MatchableStructure structure = RegistryStructures.getStructure(getRegistryName());
        if (structure == null) throw new IllegalStateException("Unknown structure: " + getRegistryName());
        return new ChangeObserverStructure(structure);
    }
}
