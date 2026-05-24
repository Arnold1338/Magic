package hellfirepvp.astralsorcery.client.effect;

import java.util.Objects;

public final class EffectType
{
    private static int counter;
    private final int id;
    
    public EffectType() {
        this.id = EffectType.counter++;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EffectType that = (EffectType)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    static {
        EffectType.counter = 0;
    }
}
