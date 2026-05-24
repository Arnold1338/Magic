package hellfirepvp.astralsorcery.common.util;

import net.minecraft.util.Mth;

public class EffectIncrementer
{
    private final int cap;
    private int current;
    
    public EffectIncrementer(final int max) {
        this.current = 0;
        this.cap = max;
    }
    
    public void update(final boolean increment) {
        if (increment) {
            ++this.current;
        }
        else {
            --this.current;
        }
        this.current = Mth.func_76125_a(this.current, 0, this.cap);
    }
    
    public int get() {
        return this.current;
    }
    
    public float getAsPercentage() {
        return this.current / (float)this.cap;
    }
}
