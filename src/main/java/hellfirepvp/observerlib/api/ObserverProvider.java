package hellfirepvp.observerlib.api;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// In 1.20.1 Forge removed IForgeRegistryEntry; providers are registered via DeferredRegister
public abstract class ObserverProvider {
    private final ResourceLocation registryName;

    public ObserverProvider(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Nonnull
    public abstract ChangeObserver provideObserver();

    @Nullable
    public final ResourceLocation getRegistryName() {
        return this.registryName;
    }
}
