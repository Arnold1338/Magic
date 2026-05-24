package hellfirepvp.astralsorcery.common.util.data;

import java.util.Objects;

public class BiDiPair<K, V>
{
    private final K left;
    private final V right;
    
    public BiDiPair(final K left, final V right) {
        this.left = left;
        this.right = right;
    }
    
    public K getLeft() {
        return this.left;
    }
    
    public V getRight() {
        return this.right;
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
        return (Objects.equals(this.left, biDiPair.left) && Objects.equals(this.right, biDiPair.right)) || (Objects.equals(this.left, biDiPair.right) && Objects.equals(this.right, biDiPair.left));
    }
    
    @Override
    public int hashCode() {
        return this.left.hashCode() ^ this.right.hashCode();
    }
}
