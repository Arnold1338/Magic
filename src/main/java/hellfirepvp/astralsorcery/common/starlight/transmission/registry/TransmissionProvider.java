package hellfirepvp.astralsorcery.common.starlight.transmission.registry;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import java.util.function.Supplier;

public abstract class TransmissionProvider implements Supplier<IPrismTransmissionNode>
{
    private ResourceLocation identifierCache;
    
    public TransmissionProvider() {
        this.identifierCache = null;
    }
    
    @Nonnull
    public ResourceLocation getIdentifier() {
        if (this.identifierCache == null) {
            this.identifierCache = NameUtil.fromClass(((Supplier<Object>)this).get());
        }
        return this.identifierCache;
    }
}
