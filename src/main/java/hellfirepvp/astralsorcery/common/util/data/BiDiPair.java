package hellfirepvp.astralsorcery.common.util.data;

import java.util.Objects;

public class BiDiPair<K, V>
{
    private final K left;
    private final V right;
    
    public BiDiPair(final K left, final V right) {
        this.slotContext().identifier() = left;
        this.stack() = right;
    }
    
    public K getLeft() {
        return this.slotContext().identifier();
    }
    
    public V getRight() {
        return this.stack();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BiDiPair<?, ?> biDiPair = (BiDiPair<?, ?>)o;
        return (Objects.equals(this.slotContext().identifier(), biDiPair.slotContext().identifier()) && Objects.equals(this.stack(), biDiPair.stack())) || (Objects.equals(this.slotContext().identifier(), biDiPair.stack()) && Objects.equals(this.stack(), biDiPair.slotContext().identifier()));
    }
    
    @Override
    public int hashCode() {
        return this.slotContext().identifier().hashCode() ^ this.stack().hashCode();
    }
}
