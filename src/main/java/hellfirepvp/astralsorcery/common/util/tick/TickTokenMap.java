package hellfirepvp.astralsorcery.common.util.tick;

import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraftforge.event.TickEvent;
import java.util.EnumSet;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class TickTokenMap<K, V extends TickMapToken<?>> extends TokenMap<K, V> implements ITickHandler
{
    private final EnumSet<TickEvent.Type> tickTypes;
    
    public TickTokenMap(@Nonnull final TickEvent.Type first, final TickEvent.Type... restTypes) {
        this.tickTypes = EnumSet.of(first, restTypes);
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Iterator<Map.Entry<K, V>> iteratorEntries = this.entrySet().iterator();
        while (iteratorEntries.hasNext()) {
            final Map.Entry<K, V> entry = iteratorEntries.next();
            entry.getValue().tick();
            if (entry.getValue().getRemainingTimeout() <= 0) {
                entry.getValue().onTimeout();
                iteratorEntries.remove();
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return this.tickTypes;
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "TickTokenMap";
    }
    
    public static class SimpleTickToken<E> implements TickMapToken<E>
    {
        @Nonnull
        private E value;
        private int timeout;
        
        public SimpleTickToken(@Nonnull final E value, final int initialTimeout) {
            this.value = value;
            this.timeout = initialTimeout;
        }
        
        @Override
        public int getRemainingTimeout() {
            return this.timeout;
        }
        
        public void setTimeout(final int timeout) {
            this.timeout = timeout;
        }
        
        public void addToTimeout(final int timeout) {
            this.timeout += timeout;
        }
        
        @Override
        public void tick() {
            --this.timeout;
        }
        
        @Override
        public void onTimeout() {
        }
        
        @Nonnull
        @Override
        public E getValue() {
            return this.value;
        }
        
        public void setValue(@Nonnull final E value) {
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
            final SimpleTickToken that = (SimpleTickToken)o;
            return this.value.equals(that.value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
    }
    
    public interface TickMapToken<E> extends MapToken<E>
    {
        int getRemainingTimeout();
        
        void tick();
        
        void onTimeout();
    }
}
