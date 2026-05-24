package hellfirepvp.astralsorcery.common.data.research;

public enum PerkRemovalResult
{
    FAILURE, 
    REMOVE_ALLOCATION, 
    REMOVE_ALLOCATION_TYPE, 
    REMOVE_PERK;
    
    public boolean isFailure() {
        return this == PerkRemovalResult.FAILURE;
    }
    
    public boolean removesAllocationType() {
        return this == PerkRemovalResult.REMOVE_ALLOCATION_TYPE || this.removesPerk();
    }
    
    public boolean removesPerk() {
        return this == PerkRemovalResult.REMOVE_PERK;
    }
}
