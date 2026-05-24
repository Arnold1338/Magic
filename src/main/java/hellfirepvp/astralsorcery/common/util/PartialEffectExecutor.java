package hellfirepvp.astralsorcery.common.util;

import java.util.Random;

public class PartialEffectExecutor
{
    private static final Random random;
    private final Random rand;
    private final float amount;
    private float currentAmount;
    
    public PartialEffectExecutor(final float amount) {
        this(amount, PartialEffectExecutor.random);
    }
    
    public PartialEffectExecutor(final float amount, final Random rand) {
        this.rand = rand;
        this.amount = amount;
        this.currentAmount = amount;
    }
    
    public boolean canExecute() {
        return this.currentAmount > 1.0f || this.rand.nextFloat() < this.currentAmount;
    }
    
    public void markExecution() {
        --this.currentAmount;
    }
    
    public void reset() {
        this.currentAmount = this.amount;
    }
    
    public boolean executeAll(final Runnable run) {
        boolean ranAtLeastOnce = false;
        while (this.canExecute()) {
            this.markExecution();
            run.run();
            ranAtLeastOnce = true;
        }
        return ranAtLeastOnce;
    }
    
    static {
        random = new Random();
    }
}
