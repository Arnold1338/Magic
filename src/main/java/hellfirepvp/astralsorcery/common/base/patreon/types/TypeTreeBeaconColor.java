package hellfirepvp.astralsorcery.common.base.patreon.types;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeTreeBeaconColor extends PatreonEffect
{
    private final Color treeBeaconColor;
    
    public TypeTreeBeaconColor(final UUID effectUUID, @Nullable final FlareColor flareColor, final Color treeBeaconColor) {
        super(effectUUID, flareColor);
        this.treeBeaconColor = treeBeaconColor;
    }
    
    public Color getTreeBeaconColor() {
        return this.treeBeaconColor;
    }
}
