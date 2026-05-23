package hellfirepvp.observerlib.common.util;
import java.io.IOException;
import java.util.*;

public class AlternatingSet<T> {
    private final Object flipLock = new Object();
    private Set<T> actualSet = new HashSet<>();
    private Set<T> flippedSet = new HashSet<>();
    private boolean accessible = true;

    public void add(T entry) {
        synchronized (flipLock) {
            if (accessible) actualSet.add(entry); else flippedSet.add(entry);
        }
    }

    public void forEach(IOPredicate<T> entryConsumer) throws IOException {
        synchronized (flipLock) {
            accessible = false;
            Set<T> set = new HashSet<>();
            for (T t : actualSet) { if (entryConsumer.testUnsafe(t)) set.add(t); }
            actualSet = set;
            accessible = true;
            actualSet.addAll(flippedSet);
            flippedSet.clear();
        }
    }

    public void clear() { synchronized (flipLock) { actualSet.clear(); flippedSet.clear(); } }
    public int size() { return actualSet.size() + flippedSet.size(); }
    public boolean isEmpty() { return actualSet.isEmpty() && flippedSet.isEmpty(); }
    public boolean contains(T o) { synchronized (flipLock) { return actualSet.contains(o) || flippedSet.contains(o); } }
}
