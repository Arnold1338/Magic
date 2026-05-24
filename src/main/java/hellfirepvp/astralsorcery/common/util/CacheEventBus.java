package hellfirepvp.astralsorcery.common.util;

import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.Event;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.eventbus.api.IEventBus;

public class CacheEventBus implements IEventBus
{
    private final List<Object> registeredListeners;
    private final IEventBus wrapped;
    
    private CacheEventBus(final IEventBus wrapped) {
        this.registeredListeners = new ArrayList<Object>();
        this.wrapped = wrapped;
    }
    
    public static CacheEventBus of(final IEventBus bus) {
        return new CacheEventBus(bus);
    }
    
    public void unregisterAll() {
        this.registeredListeners.forEach(this.wrapped::unregister);
        this.registeredListeners.clear();
    }
    
    public void register(final Object target) {
        this.wrapped.register(target);
        this.registeredListeners.add(target);
    }
    
    public <T extends Event> void addListener(final Consumer<T> consumer) {
        this.wrapped.addListener((Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends Event> void addListener(final EventPriority priority, final Consumer<T> consumer) {
        this.wrapped.addListener(priority, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends Event> void addListener(final EventPriority priority, final boolean receiveCancelled, final Consumer<T> consumer) {
        this.wrapped.addListener(priority, receiveCancelled, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends Event> void addListener(final EventPriority priority, final boolean receiveCancelled, final Class<T> eventType, final Consumer<T> consumer) {
        this.wrapped.addListener(priority, receiveCancelled, (Class)eventType, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final Consumer<T> consumer) {
        this.wrapped.addGenericListener((Class)genericClassFilter, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final Consumer<T> consumer) {
        this.wrapped.addGenericListener((Class)genericClassFilter, priority, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final boolean receiveCancelled, final Consumer<T> consumer) {
        this.wrapped.addGenericListener((Class)genericClassFilter, priority, receiveCancelled, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public <T extends GenericEvent<? extends F>, F> void addGenericListener(final Class<F> genericClassFilter, final EventPriority priority, final boolean receiveCancelled, final Class<T> eventType, final Consumer<T> consumer) {
        this.wrapped.addGenericListener((Class)genericClassFilter, priority, receiveCancelled, (Class)eventType, (Consumer)consumer);
        this.registeredListeners.add(consumer);
    }
    
    public void unregister(final Object object) {
        this.wrapped.unregister(object);
        this.registeredListeners.remove(object);
    }
    
    public boolean post(final Event event) {
        return this.wrapped.post(event);
    }
    
    public boolean post(final Event event, final IEventBusInvokeDispatcher wrapper) {
        return this.wrapped.post(event, wrapper);
    }
    
    public void shutdown() {
        this.wrapped.shutdown();
    }
    
    public void start() {
        this.wrapped.start();
    }
}
