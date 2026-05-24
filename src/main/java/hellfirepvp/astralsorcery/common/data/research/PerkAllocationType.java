package hellfirepvp.astralsorcery.common.data.research;

public enum PerkAllocationType
{
    UNLOCKED("unlocked"), 
    GRANTED("granted");
    
    private final String saveKey;
    
    private PerkAllocationType(final String saveKey) {
        this.saveKey = saveKey;
    }
    
    public final String getSaveKey() {
        return this.saveKey;
    }
}
