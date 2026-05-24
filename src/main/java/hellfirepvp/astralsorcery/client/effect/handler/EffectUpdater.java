package hellfirepvp.astralsorcery.client.effect.handler;

import java.util.EnumSet;
import java.io.IOException;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class EffectUpdater implements ITickHandler
{
    private static final EffectUpdater INSTANCE;
    
    public static EffectUpdater getInstance() {
        return EffectUpdater.INSTANCE;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        try {
            EffectHandler.getInstance().tick();
        }
        catch (final IOException ex) {}
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "EffectUpdater";
    }
    
    static {
        INSTANCE = new EffectUpdater();
    }
}
