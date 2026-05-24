package hellfirepvp.astralsorcery.client;

import java.util.EnumSet;
import java.util.Iterator;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.astralsorcery.common.util.Counter;
import net.minecraft.util.Tuple;
import java.util.LinkedList;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class ClientScheduler implements ITickHandler
{
    private static long clientTick;
    private static final Object lock;
    private boolean inTick;
    private final LinkedList<Tuple<Runnable, Counter>> queue;
    private final LinkedList<Tuple<Runnable, Integer>> waiting;
    
    public ClientScheduler() {
        this.inTick = false;
        this.queue = new LinkedList<Tuple<Runnable, Counter>>();
        this.waiting = new LinkedList<Tuple<Runnable, Integer>>();
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        ++ClientScheduler.clientTick;
        this.inTick = true;
        synchronized (ClientScheduler.lock) {
            this.inTick = true;
            final Iterator<Tuple<Runnable, Counter>> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                final Tuple<Runnable, Counter> r = iterator.next();
                ((Counter)r.func_76340_b()).decrement();
                if (((Counter)r.func_76340_b()).getValue() <= 0) {
                    ((Runnable)r.func_76341_a()).run();
                    iterator.remove();
                }
            }
            this.inTick = false;
            for (final Tuple<Runnable, Integer> wait : this.waiting) {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple(wait.func_76341_a(), (Object)new Counter((int)wait.func_76340_b())));
            }
        }
        this.waiting.clear();
    }
    
    public static long getClientTick() {
        return ClientScheduler.clientTick;
    }
    
    public static long getSystemClientTick() {
        return System.currentTimeMillis() / 50L;
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Client Scheduler";
    }
    
    public void addRunnable(final Runnable r, final int tickDelay) {
        synchronized (ClientScheduler.lock) {
            if (this.inTick) {
                this.waiting.addLast((Tuple<Runnable, Integer>)new Tuple((Object)r, (Object)tickDelay));
            }
            else {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple((Object)r, (Object)new Counter(tickDelay)));
            }
        }
    }
    
    static {
        ClientScheduler.clientTick = 0L;
        lock = new Object();
    }
}
