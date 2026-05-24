package hellfirepvp.astralsorcery.common.perk;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;

public enum AllocationStatus
{
    UNALLOCATED, 
    ALLOCATED, 
    GRANTED, 
    UNLOCKABLE;
    
    public boolean isAllocated() {
        return this == AllocationStatus.ALLOCATED || this == AllocationStatus.GRANTED;
    }
    
    @OnlyIn(Dist.CLIENT)
    public SpriteSheetResource getPerkTreeSprite() {
        switch (this) {
            case GRANTED:
            case ALLOCATED: {
                return SpritesAS.SPR_PERK_ACTIVE;
            }
            case UNLOCKABLE: {
                return SpritesAS.SPR_PERK_ACTIVATEABLE;
            }
            default: {
                return SpritesAS.SPR_PERK_INACTIVE;
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public SpriteSheetResource getPerkTreeHaloSprite() {
        switch (this) {
            case GRANTED:
            case ALLOCATED: {
                return SpritesAS.SPR_PERK_HALO_ACTIVE;
            }
            case UNLOCKABLE: {
                return SpritesAS.SPR_PERK_HALO_ACTIVATEABLE;
            }
            default: {
                return SpritesAS.SPR_PERK_HALO_INACTIVE;
            }
        }
    }
    
    public Color getPerkConnectionColor() {
        switch (this) {
            case GRANTED:
            case ALLOCATED: {
                return ColorsAS.PERK_CONNECTION_ALLOCATED;
            }
            case UNLOCKABLE: {
                return ColorsAS.PERK_CONNECTION_UNLOCKABLE;
            }
            default: {
                return ColorsAS.PERK_CONNECTION_UNALLOCATED;
            }
        }
    }
    
    public Color getPerkColor() {
        switch (this) {
            case GRANTED:
            case ALLOCATED: {
                return ColorsAS.PERK_ALLOCATED;
            }
            case UNLOCKABLE: {
                return ColorsAS.PERK_UNLOCKABLE;
            }
            default: {
                return ColorsAS.PERK_UNALLOCATED;
            }
        }
    }
}
