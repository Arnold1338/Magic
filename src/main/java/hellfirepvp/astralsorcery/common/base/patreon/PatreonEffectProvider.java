package hellfirepvp.astralsorcery.common.base.patreon;

import java.util.List;
import java.util.UUID;

public interface PatreonEffectProvider<T extends PatreonEffect>
{
    T buildEffect(final UUID p0, final List<String> p1) throws Exception;
}
