package hellfirepvp.astralsorcery.common.util.order;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public abstract class OrderSortable
{
    private final List<Object> before;
    private final List<Object> after;
    
    public OrderSortable() {
        this.before = new ArrayList<Object>();
        this.after = new ArrayList<Object>();
    }
    
    public <E extends OrderSortable> E setBefore(final Object... other) {
        return this.setBefore(Arrays.asList(other));
    }
    
    public <E extends OrderSortable> E setBefore(final Collection<Object> other) {
        this.before.addAll(other);
        return (E)this;
    }
    
    public <E extends OrderSortable> E setAfter(final Object... other) {
        return this.setAfter(Arrays.asList(other));
    }
    
    public <E extends OrderSortable> E setAfter(final Collection<Object> other) {
        this.after.addAll(other);
        return (E)this;
    }
    
    public boolean isBefore(final Object other) {
        return this.before.contains(other);
    }
    
    public boolean isAfter(final Object other) {
        return this.after.contains(other);
    }
}
