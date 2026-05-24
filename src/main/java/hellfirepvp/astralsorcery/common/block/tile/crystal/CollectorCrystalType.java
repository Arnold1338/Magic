package hellfirepvp.astralsorcery.common.block.tile.crystal;

import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.level.material.MapColor;
import java.awt.Color;

public enum CollectorCrystalType
{
    ROCK_CRYSTAL(ColorsAS.ROCK_CRYSTAL, MaterialColor.field_193561_M), 
    CELESTIAL_CRYSTAL(ColorsAS.CELESTIAL_CRYSTAL, MaterialColor.field_151679_y);
    
    private final Color displayColor;
    private final MaterialColor matColor;
    
    private CollectorCrystalType(final Color displayColor, final MaterialColor matColor) {
        this.displayColor = displayColor;
        this.matColor = matColor;
    }
    
    public Color getDisplayColor() {
        return this.displayColor;
    }
    
    public MaterialColor getMaterialColor() {
        return this.matColor;
    }
}
