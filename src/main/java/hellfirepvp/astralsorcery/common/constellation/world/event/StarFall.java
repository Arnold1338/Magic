package hellfirepvp.astralsorcery.common.constellation.world.event;

import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import java.util.Random;
import net.minecraft.world.level.Level;

public class StarFall extends CelestialEvent
{
    @Override
    public void tick(final Level world, final Random rand, final WorldContext ctx) {
    }
    
    @Override
    public boolean isActiveNow() {
        return false;
    }
    
    @Override
    public boolean isActiveDay() {
        return false;
    }
    
    @Override
    public float getEffectTick(final float pTicks) {
        return 0.0f;
    }
    
    @Override
    public long getSeedModifier() {
        return 0L;
    }
}
