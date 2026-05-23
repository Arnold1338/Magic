package hellfirepvp.observerlib.common.data;

import hellfirepvp.observerlib.common.util.IORunnable;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.*;
import java.util.function.Supplier;

public abstract class CachedWorldData implements IWorldRelatedData {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    protected final Random rand = new Random();
    private final WorldCacheDomain.SaveKey<?> key;

    protected CachedWorldData(WorldCacheDomain.SaveKey<?> key) { this.key = key; }

    public abstract boolean needsSaving();
    public abstract void updateTick(Level world);

    @Override
    public final WorldCacheDomain.SaveKey<?> getSaveKey() { return this.key; }
    public void onLoad(Level world) {}

    public <T> T write(Supplier<T> fn) { return lock(rwLock::writeLock, fn); }
    public void write(Runnable run) { lock(rwLock::writeLock, () -> { run.run(); return null; }); }
    public void writeIO(IORunnable run) throws IOException {
        rwLock.writeLock().lock(); try { run.run(); } finally { rwLock.writeLock().unlock(); }
    }
    public <T> T read(Supplier<T> fn) { return lock(rwLock::readLock, fn); }
    public void read(Runnable run) { lock(rwLock::readLock, () -> { run.run(); return null; }); }
    public void readIO(IORunnable run) throws IOException {
        rwLock.readLock().lock(); try { run.run(); } finally { rwLock.readLock().unlock(); }
    }

    private <T> T lock(Supplier<Lock> lock, Supplier<T> fn) {
        lock.get().lock(); try { return fn.get(); } finally { lock.get().unlock(); }
    }
}
