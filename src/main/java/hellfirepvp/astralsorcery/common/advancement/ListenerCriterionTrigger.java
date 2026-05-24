package hellfirepvp.astralsorcery.common.advancement;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.function.Predicate;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import java.util.Map;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;

public abstract class ListenerCriterionTrigger<T extends AbstractCriterionTriggerInstance> implements CriterionTrigger<T>
{
    protected final Map<PlayerAdvancements, Listeners<T>> listeners;
    private final ResourceLocation id;
    
    public ListenerCriterionTrigger(final ResourceLocation id) {
        this.listeners = Maps.newHashMap();
        this.id = id;
    }
    
    public final ResourceLocation func_192163_a() {
        return this.id;
    }
    
    public final void func_192165_a(final PlayerAdvancements playerAdvancementsIn, final CriterionTrigger.Listener<T> listener) {
        Listeners<T> listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners == null) {
            listeners = new Listeners<T>(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }
        listeners.add(listener);
    }
    
    public final void func_192164_b(final PlayerAdvancements playerAdvancementsIn, final CriterionTrigger.Listener<T> listener) {
        final Listeners<T> listeners = this.listeners.get(playerAdvancementsIn);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }
    
    public final void func_192167_a(final PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }
    
    public static class Listeners<T extends AbstractCriterionTriggerInstance>
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<CriterionTrigger.Listener<T>> listeners;
        
        public Listeners(final PlayerAdvancements playerAdvancementsIn) {
            this.listeners = Sets.newHashSet();
            this.playerAdvancements = playerAdvancementsIn;
        }
        
        public final boolean isEmpty() {
            return this.listeners.isEmpty();
        }
        
        public final void add(final CriterionTrigger.Listener<T> listener) {
            this.listeners.add(listener);
        }
        
        public final void remove(final CriterionTrigger.Listener<T> listener) {
            this.listeners.remove(listener);
        }
        
        public final void trigger(final Predicate<T> test) {
            final List<CriterionTrigger.Listener<T>> list = Lists.newArrayList();
            for (final CriterionTrigger.Listener<T> listener : this.listeners) {
                if (test.test((T)listener.func_192158_a())) {
                    list.add(listener);
                }
            }
            for (final CriterionTrigger.Listener<T> listener2 : list) {
                listener2.func_192159_a(this.playerAdvancements);
            }
        }
    }
}
