package hellfirepvp.astralsorcery.client.screen.telescope;

import java.awt.Rectangle;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.client.screen.ScreenTelescope;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;

public class TelescopeRotationDrawArea extends ConstellationDiscoveryScreen.DrawArea
{
    private final ScreenTelescope screenTelescope;
    private final TileTelescope.TelescopeRotation rotation;
    
    public TelescopeRotationDrawArea(final ScreenTelescope screenTelescope, final TileTelescope.TelescopeRotation rotation, final Rectangle area) {
        super(area);
        this.screenTelescope = screenTelescope;
        this.rotation = rotation;
    }
    
    @Override
    public boolean isVisible() {
        return this.rotation == this.screenTelescope.getRotation();
    }
}
