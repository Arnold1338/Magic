package hellfirepvp.astralsorcery.common.crystal.source;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;

public class AttunedSourceInstance extends PropertySource.SourceInstance
{
    private final IWeakConstellation attunedConstellation;
    
    public AttunedSourceInstance(final PropertySource<?, ?> source, @Nullable final IWeakConstellation attunedConstellation) {
        super(source);
        this.attunedConstellation = attunedConstellation;
    }
    
    @Nullable
    public IWeakConstellation getAttunedConstellation() {
        return this.attunedConstellation;
    }
}
