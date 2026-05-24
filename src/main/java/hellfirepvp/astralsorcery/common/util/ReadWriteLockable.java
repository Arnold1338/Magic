package hellfirepvp.astralsorcery.common.util;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReadWriteLock;

public interface ReadWriteLockable
{
    ReadWriteLock getLock();
    
    default <T> T write(final Supplier<T> fn) {
        return this.lock(this.getLock()::writeLock, fn);
    }
    
    default void write(final Runnable run) {
        this.lock(this.getLock()::writeLock, MiscUtils.nullSupplier(run));
    }
    
    default <T> T read(final Supplier<T> fn) {
        return this.lock(this.getLock()::readLock, fn);
    }
    
    default void read(final Runnable run) {
        this.lock(this.getLock()::readLock, MiscUtils.nullSupplier(run));
    }
    
    default <T> T lock(final Supplier<Lock> lock, final Supplier<T> fn) {
        lock.get().lock();
        try {
            return fn.get();
        }
        finally {
            lock.get().unlock();
        }
    }
}
