package hellfirepvp.astralsorcery.common.util.object;

import java.util.Objects;

public class ObjectReference<T>
{
    private T object;
    
    public ObjectReference(final T object) {
        this.object = object;
    }
    
    public ObjectReference() {
    }
    
    public T get() {
        return this.object;
    }
    
    public void set(final T object) {
        this.object = object;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ObjectReference<?> that = (ObjectReference<?>)o;
        return Objects.equals(this.object, that.object);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.object);
    }
}
