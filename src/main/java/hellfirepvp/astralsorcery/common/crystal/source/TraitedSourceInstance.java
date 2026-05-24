package hellfirepvp.astralsorcery.common.crystal.source;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;

public class TraitedSourceInstance extends AttunedSourceInstance
{
    private final IMinorConstellation traitConstellation;
    
    public TraitedSourceInstance(final PropertySource<?, ?> source, @Nullable final IWeakConstellation attunedConstellation, @Nullable final IMinorConstellation traitConstellation) {
        super(source, attunedConstellation);
        this.traitConstellation = traitConstellation;
    }
    
    @Nullable
    public IMinorConstellation getTraitConstellation() {
        return this.traitConstellation;
    }
}
