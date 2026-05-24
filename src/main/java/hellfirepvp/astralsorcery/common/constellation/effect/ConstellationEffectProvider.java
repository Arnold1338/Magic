package hellfirepvp.astralsorcery.common.constellation.effect;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ConstellationEffectProvider extends ForgeRegistryEntry<ConstellationEffectProvider> implements IForgeRegistryEntry<ConstellationEffectProvider>
{
    private final IWeakConstellation cst;
    
    protected ConstellationEffectProvider(final IWeakConstellation cst) {
        this.cst = cst;
        this.setRegistryName(cst.getRegistryName());
    }
    
    @Nonnull
    public IWeakConstellation getConstellation() {
        return this.cst;
    }
    
    public abstract ConstellationEffect createEffect(@Nullable final ILocatable p0);
}
