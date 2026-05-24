package hellfirepvp.astralsorcery.client.screen.telescope;

import java.awt.Rectangle;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;

public class FullScreenDrawArea extends ConstellationDiscoveryScreen.DrawArea
{
    public FullScreenDrawArea() {
        super(new Rectangle());
    }
    
    @Override
    public boolean contains(final double mouseX, final double mouseY) {
        return true;
    }
}
