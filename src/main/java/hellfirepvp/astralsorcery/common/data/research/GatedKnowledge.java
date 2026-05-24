package hellfirepvp.astralsorcery.common.data.research;

public enum GatedKnowledge
{
    CRYSTAL_SIZE(ProgressionTier.BASIC_CRAFT), 
    CRYSTAL_PURITY(ProgressionTier.BASIC_CRAFT), 
    CRYSTAL_COLLECT(ProgressionTier.BASIC_CRAFT), 
    CRYSTAL_TUNE(ProgressionTier.ATTUNEMENT), 
    CRYSTAL_TRAIT(ProgressionTier.TRAIT_CRAFT), 
    COLLECTOR_CRYSTAL(ProgressionTier.CONSTELLATION_CRAFT), 
    COLLECTOR_TYPE(ProgressionTier.CONSTELLATION_CRAFT);
    
    private final ProgressionTier capability;
    
    private GatedKnowledge(final ProgressionTier capability) {
        this.capability = capability;
    }
    
    public boolean canSee(final PlayerProgress progress) {
        return this.canSee(progress.getTierReached());
    }
    
    public boolean canSee(final ProgressionTier compCapability) {
        return this.capability.ordinal() <= compCapability.ordinal();
    }
}
