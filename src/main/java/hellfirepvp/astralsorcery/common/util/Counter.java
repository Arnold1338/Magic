package hellfirepvp.astralsorcery.common.util;

public class Counter
{
    private int value;
    
    public Counter(final int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void increment() {
        ++this.value;
    }
}
