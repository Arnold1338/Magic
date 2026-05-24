package hellfirepvp.astralsorcery.common.data.research;

import net.minecraft.Util;
import java.util.UUID;

public class PlayerPerkAllocation
{
    private static final UUID UNLOCK_UUID;
    private final PerkAllocationType type;
    private final UUID lockUUID;
    
    private PlayerPerkAllocation(final PerkAllocationType type, final UUID lockUUID) {
        this.type = type;
        this.lockUUID = lockUUID;
    }
    
    public static PlayerPerkAllocation unlock() {
        return new PlayerPerkAllocation(PerkAllocationType.UNLOCKED, PlayerPerkAllocation.UNLOCK_UUID);
    }
    
    public static PlayerPerkAllocation granted(final UUID lockUUID) {
        return new PlayerPerkAllocation(PerkAllocationType.GRANTED, lockUUID);
    }
    
    final PerkAllocationType getType() {
        return this.type;
    }
    
    final UUID getLockUUID() {
        return this.lockUUID;
    }
    
    static {
        UNLOCK_UUID = Util.NIL_UUID;
    }
}
