package hellfirepvp.astralsorcery.common;

import java.util.EnumSet;
import java.util.Iterator;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.util.Counter;
import net.minecraft.util.Tuple;
import java.util.LinkedList;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class CommonScheduler implements ITickHandler
{
    private static final Object lock;
    private boolean inTick;
    private final LinkedList<Tuple<Runnable, Counter>> queue;
    private final LinkedList<Tuple<Runnable, Integer>> waiting;
    
    public CommonScheduler() {
        this.inTick = false;
        this.queue = new LinkedList<Tuple<Runnable, Counter>>();
        this.waiting = new LinkedList<Tuple<Runnable, Integer>>();
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        this.inTick = true;
        synchronized (CommonScheduler.lock) {
            this.inTick = true;
            final Iterator<Tuple<Runnable, Counter>> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                final Tuple<Runnable, Counter> r = iterator.next();
                ((Counter)r.getB()).decrement();
                if (((Counter)r.getB()).getValue() <= 0) {
                    ((Runnable)r.getA()).run();
                    iterator.remove();
                }
            }
            this.inTick = false;
            for (final Tuple<Runnable, Integer> wait : this.waiting) {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple(wait.getA(), (Object)new Counter((int)wait.getB())));
            }
        }
        this.waiting.clear();
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Common Scheduler";
    }
    
    public void addRunnable(final Runnable r, final int tickDelay) {
        synchronized (CommonScheduler.lock) {
            if (this.inTick) {
                this.waiting.addLast((Tuple<Runnable, Integer>)new Tuple((Object)r, (Object)tickDelay));
            }
            else {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple((Object)r, (Object)new Counter(tickDelay)));
            }
        }
    }
    
    static {
        lock = new Object();
    }
}
