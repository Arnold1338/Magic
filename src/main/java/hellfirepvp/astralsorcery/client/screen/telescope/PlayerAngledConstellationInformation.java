package hellfirepvp.astralsorcery.client.screen.telescope;

import java.awt.Point;
import hellfirepvp.astralsorcery.client.screen.base.ConstellationDiscoveryScreen;

public class PlayerAngledConstellationInformation extends ConstellationDiscoveryScreen.ConstellationDisplayInformation
{
    private final float yaw;
    private final float pitch;
    
    public PlayerAngledConstellationInformation(final float size, final float yaw, final float pitch) {
        super(new Point(), size);
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
}
