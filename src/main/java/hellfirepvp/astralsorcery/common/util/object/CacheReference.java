package hellfirepvp.astralsorcery.common.util.object;

import java.util.function.Supplier;

public class CacheReference<T> implements Supplier<T>
{
    private final Supplier<T> objectSupplier;
    private T object;
    
    public CacheReference(final Supplier<T> objectSupplier) {
        this.object = null;
        this.objectSupplier = objectSupplier;
    }
    
    @Override
    public T get() {
        if (this.object == null) {
            this.object = this.objectSupplier.get();
        }
        return this.object;
    }
}
