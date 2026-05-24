package hellfirepvp.astralsorcery.common.crystal.source;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;

public class Crystal extends AttunedSourceInstance
{
    public Crystal(final PropertySource<?, ?> source, @Nullable final IWeakConstellation attunedConstellation) {
        super(source, attunedConstellation);
    }
}
