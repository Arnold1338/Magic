package hellfirepvp.astralsorcery.common.util;

import net.minecraft.util.Mth;

public class DiminishingMultiplier
{
    private final long gainMsTime;
    private final float gainRate;
    private final float dropRate;
    private final float min;
    private float multiplier;
    private long lastGain;
    private int recoveryStack;
    
    public DiminishingMultiplier(final long gainMsTime, final float gainRate, final float dropRate, final float min) {
        this.multiplier = 1.0f;
        this.lastGain = 0L;
        this.recoveryStack = 0;
        this.gainMsTime = gainMsTime;
        this.gainRate = gainRate;
        this.dropRate = dropRate;
        this.min = min;
    }
    
    public float getMultiplier() {
        this.recalcMultiplier();
        return this.multiplier;
    }
    
    private void recalcMultiplier() {
        final long now = System.currentTimeMillis();
        final long diff = now - this.lastGain;
        final long times = diff * (this.recoveryStack + 1) / this.gainMsTime;
        if (times > 0L) {
            this.lastGain = now;
            this.recoveryStack = Math.min(this.recoveryStack + 1, 3);
            this.multiplier = Mth.canEnchant(this.multiplier + times * this.gainRate, this.min, 1.0f);
        }
        else {
            this.multiplier = Math.max(this.multiplier - this.dropRate, this.min);
            this.recoveryStack = 0;
        }
    }
}
