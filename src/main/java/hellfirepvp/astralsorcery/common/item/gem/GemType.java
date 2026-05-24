package hellfirepvp.astralsorcery.common.item.gem;

public enum GemType
{
    SKY(0.15f, 1.0f), 
    DAY(0.6f, 0.4f), 
    NIGHT(0.0f, 2.0f);
    
    public final float countModifier;
    public final float amplifierModifier;
    
    private GemType(final float countModifier, final float amplifierModifier) {
        this.countModifier = countModifier;
        this.amplifierModifier = amplifierModifier;
    }
}
