package hellfirepvp.astralsorcery.common.constellation.world.event;

import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import java.util.Random;
import net.minecraft.world.level.level.Level;

public abstract class CelestialEvent
{
    public abstract void tick(final World p0, final Random p1, final WorldContext p2);
    
    public abstract boolean isActiveNow();
    
    public abstract boolean isActiveDay();
    
    public abstract float getEffectTick(final float p0);
    
    public abstract long getSeedModifier();
}
