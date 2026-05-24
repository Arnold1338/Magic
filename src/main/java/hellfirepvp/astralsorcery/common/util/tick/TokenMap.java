package hellfirepvp.astralsorcery.common.util.tick;

import java.util.HashMap;

public class TokenMap<K, V extends MapToken<?>> extends HashMap<K, V>
{
    public interface MapToken<V>
    {
        V getValue();
    }
}
