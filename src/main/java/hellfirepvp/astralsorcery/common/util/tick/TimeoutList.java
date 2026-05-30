package hellfirepvp.astralsorcery.common.util.tick;

import java.util.function.Predicate;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Iterator;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraftforge.event.TickEvent;
import java.util.EnumSet;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class TimeoutList<V> implements ITickHandler, Iterable<V>
{
    private final TimeoutDelegate<V> delegate;
    private final EnumSet<TickEvent.Type> tickTypes;
    private final List<TimeoutEntry<V>> tickEntries;
    
    public TimeoutList(@Nullable final TimeoutDelegate<V> delegate, final TickEvent.Type... types) {
        this.tickEntries = new LinkedList<TimeoutEntry<V>>();
        this.delegate = delegate;
        this.tickTypes = EnumSet.noneOf(TickEvent.Type.class);
        for (final TickEvent.Type type : types) {
            if (type != null) {
                this.tickTypes.add(type);
            }
        }
    }
    
    public void add(final V value) {
        this.add(0, value);
    }
    
    public void add(final int timeout, final V value) {
        if (value == null) {

        }
        this.tickEntries.add(new TimeoutEntry<V>(timeout, (Object)value));
    }
    
    public boolean setTimeout(final int timeout, @Nonnull final V value) {
        for (final TimeoutEntry<V> entry : this.tickEntries) {
            if (((TimeoutEntry<Object>)entry).value.equals(value)) {
                ((TimeoutEntry<Object>)entry).timeout = timeout;
                return true;
            }
        }
        return false;
    }
    
    public boolean setOrAddTimeout(final int timeout, @Nonnull final V value) {
        if (!this.contains(value)) {
            this.add(timeout, value);
            return true;
        }
        return this.setTimeout(timeout, value);
    }
    
    public boolean contains(final V value) {
        return value != null && MiscUtils.contains(this.tickEntries, entry -> entry.value.equals(value));
    }
    
    public boolean remove(final V key) {
        return this.removeIf(key::equals);
    }
    
    public boolean removeIf(final Predicate<V> test) {
        return this.tickEntries.removeIf(e -> test.test(e.value));
    }
    
    public int getTimeout(final V value) {
        for (final TimeoutEntry<V> entry : this.tickEntries) {
            if (((TimeoutEntry<Object>)entry).value.equals(value)) {
                return ((TimeoutEntry<Object>)entry).timeout;
            }
        }
        return -1;
    }
    
    public void addAll(final TimeoutList<V> entries) {
        if (entries == null) {

        }
        for (final TimeoutEntry<V> entry : entries.tickEntries) {
            this.setOrAddTimeout(((TimeoutEntry<Object>)entry).timeout, ((TimeoutEntry<Object>)entry).value);
        }
    }
    
    public boolean isEmpty() {
        return this.tickEntries.isEmpty();
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Iterator<TimeoutEntry<V>> iterator = this.tickEntries.iterator();
        while (iterator.hasNext()) {
            final TimeoutEntry<V> entry = iterator.next();
            ((TimeoutEntry<Object>)entry).timeout--;
            if (((TimeoutEntry<Object>)entry).timeout <= 0) {
                if (this.delegate != null) {
                    this.delegate.onTimeout((V)((TimeoutEntry<Object>)entry).value);
                }
                iterator.remove();
            }
        }
    }
    
    public void clear() {
        if (this.delegate != null) {
            this.tickEntries.forEach(entry -> this.delegate.onTimeout((V)entry.value));
        }
        this.tickEntries.clear();
    }
    
    public Iterator<V> iterator() {
        final Iterator<TimeoutEntry<V>> entryIterator = this.tickEntries.iterator();
        return new Iterator<V>() {
            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }
            
            @Override
            public V next() {
                return (V)entryIterator.next().value;
            }
            
            @Override
            public void remove() {
                entryIterator.remove();
            }
        };
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return this.tickTypes;
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "TimeoutList";
    }
    
    private static class TimeoutEntry<V>
    {
        @Nonnull
        private final V value;
        private int timeout;
        
        private TimeoutEntry(final int timeout, @Nonnull final V value) {
            this.timeout = timeout;
            this.value = value;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final TimeoutEntry that = (TimeoutEntry)o;
            return this.value.equals(that.value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
    }
    
    public interface TimeoutDelegate<V>
    {
        void onTimeout(final V p0);
    }
}
