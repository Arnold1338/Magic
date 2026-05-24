package hellfirepvp.astralsorcery.common.constellation.effect;

public class ConstellationEffectProperties
{
    private double size;
    private float potency;
    private float effectAmplifier;
    private boolean corrupted;
    
    public ConstellationEffectProperties(final double size) {
        this.potency = 1.0f;
        this.effectAmplifier = 1.0f;
        this.corrupted = false;
        this.size = size;
    }
    
    public double getSize() {
        return this.size;
    }
    
    public void multiplySize(final double multiplier) {
        this.size *= multiplier;
    }
    
    public float getPotency() {
        return this.potency;
    }
    
    public void multiplyPotency(final float multiplier) {
        this.potency *= multiplier;
    }
    
    public float getEffectAmplifier() {
        return this.effectAmplifier;
    }
    
    public void multiplyEffectAmplifier(final float multiplier) {
        this.effectAmplifier *= multiplier;
    }
    
    public boolean isCorrupted() {
        return this.corrupted;
    }
    
    public void setCorrupted(final boolean corrupted) {
        this.corrupted = corrupted;
    }
}
