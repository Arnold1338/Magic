package hellfirepvp.astralsorcery.common.crystal.source;

import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;

public class Ritual extends TraitedSourceInstance
{
    public Ritual(final PropertySource<?, ?> source, @Nullable final IWeakConstellation attunedConstellation, @Nullable final IMinorConstellation traitConstellation) {
        super(source, attunedConstellation, traitConstellation);
    }
}
