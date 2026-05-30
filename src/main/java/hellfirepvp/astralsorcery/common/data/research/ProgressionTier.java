package hellfirepvp.astralsorcery.common.data.research;

public enum ProgressionTier
{
    DISCOVERY, 
    BASIC_CRAFT, 
    ATTUNEMENT, 
    CONSTELLATION_CRAFT, 
    TRAIT_CRAFT, 

    public boolean hasNextTier() {
        return this.ordinal() < values().length - 1;
    }
    
    public ProgressionTier next() {
        return values()[Math.min(values().length - 1, this.ordinal() + 1)];
    }
    
    public boolean isThisLaterOrEqual(final ProgressionTier other) {
        return this.ordinal() >= other.ordinal();
    }
    
    public boolean isThisLater(final ProgressionTier other) {
        return this.ordinal() > other.ordinal();
    }
}
