package hellfirepvp.astralsorcery.common.util.tick;

import java.util.function.Consumer;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.HashMap;
import javax.annotation.Nullable;
import java.util.Map;
import net.minecraftforge.event.TickEvent;
import java.util.EnumSet;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class TimeoutListContainer<K, V> implements ITickHandler
{
    private final EnumSet<TickEvent.Type> tickTypes;
    private final ContainerTimeoutDelegate<K, V> delegate;
    private final Map<K, TimeoutList<V>> timeoutListMap;
    
    public TimeoutListContainer(final TickEvent.Type... restTypes) {
        this((ContainerTimeoutDelegate)null, restTypes);
    }
    
    public TimeoutListContainer(@Nullable final ContainerTimeoutDelegate<K, V> delegate, final TickEvent.Type... types) {
        this.timeoutListMap = new HashMap<K, TimeoutList<V>>();
        this.tickTypes = EnumSet.noneOf(TickEvent.Type.class);
        for (final TickEvent.Type type : types) {
            if (type != null) {
                this.tickTypes.add(type);
            }
        }
        this.delegate = delegate;
    }
    
    public boolean hasList(final K key) {
        return this.timeoutListMap.containsKey(key);
    }
    
    @Nullable
    public TimeoutList<V> removeList(final K key) {
        final TimeoutList<V> ret = this.timeoutListMap.remove(key);
        ret.forEach(v -> this.delegate.onContainerTimeout((K)key, (V)v));
        return ret;
    }
    
    public boolean removeList(final Predicate<V> valueTest) {
        boolean removed = false;
        for (final Map.Entry<K, TimeoutList<V>> entry : this.timeoutListMap.entrySet()) {
            final Iterator<V> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                final V value = iterator.next();
                if (valueTest.test(value)) {
                    this.delegate.onContainerTimeout(entry.getKey(), value);
                    iterator.remove();
                    removed = true;
                }
            }
        }
        return removed;
    }
    
    public TimeoutList<V> getOrCreateList(final K key) {
        TimeoutList<V> list = this.timeoutListMap.get(key);
        if (list == null) {
            list = new TimeoutList<V>(new RedirectTimeoutDelegate<Object, V>((Object)key, (ContainerTimeoutDelegate)this.delegate), new TickEvent.Type[0]);
            this.timeoutListMap.put(key, list);
        }
        return list;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Iterator<Map.Entry<K, TimeoutList<V>>> it = this.timeoutListMap.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<K, TimeoutList<V>> entry = it.next();
            final TimeoutList<V> list = entry.getValue();
            list.tick(type, context);
            if (list.isEmpty()) {
                it.remove();
            }
        }
    }
    
    public void clear() {
        Lists.newArrayList((Iterable)this.timeoutListMap.keySet()).forEach(this::removeList);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return this.tickTypes;
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "TimeoutListContainer";
    }
    
    private static class RedirectTimeoutDelegate<K, V> implements TimeoutList.TimeoutDelegate<V>
    {
        private final K key;
        private final ContainerTimeoutDelegate<K, V> delegate;
        
        private RedirectTimeoutDelegate(final K key, @Nullable final ContainerTimeoutDelegate<K, V> delegate) {
            this.key = key;
            this.delegate = delegate;
        }
        
        @Override
        public void onTimeout(final V object) {
            if (this.delegate != null) {
                this.delegate.onContainerTimeout(this.key, object);
            }
        }
    }
    
    public static class ForwardingTimeoutDelegate<K, V extends TimeoutList.TimeoutDelegate<K>> implements ContainerTimeoutDelegate<K, V>
    {
        @Override
        public void onContainerTimeout(final K key, final V timedOut) {
            timedOut.onTimeout(key);
        }
    }
    
    public interface ContainerTimeoutDelegate<K, V>
    {
        void onContainerTimeout(final K p0, final V p1);
    }
}
