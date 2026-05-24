package hellfirepvp.astralsorcery.common.util.object;

import java.util.function.Function;

public class TransformReference<T, R>
{
    private final T object;
    private final Function<T, R> transform;
    
    public TransformReference(final T object, final Function<T, R> transform) {
        this.object = object;
        this.transform = transform;
    }
    
    public T getReference() {
        return this.object;
    }
    
    public R getValue() {
        return this.transform.apply(this.object);
    }
}
