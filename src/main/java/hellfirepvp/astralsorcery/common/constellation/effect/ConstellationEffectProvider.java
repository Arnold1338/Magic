package hellfirepvp.astralsorcery.common.constellation.effect;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;



public abstract class ConstellationEffectProvider extends ForgeRegistryEntry<ConstellationEffectProvider> {
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
