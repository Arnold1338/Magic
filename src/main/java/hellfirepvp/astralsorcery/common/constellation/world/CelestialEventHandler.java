package hellfirepvp.astralsorcery.common.constellation.world;

import java.util.Iterator;
import net.minecraft.world.level.Level;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.constellation.world.event.StarFall;
import hellfirepvp.astralsorcery.common.constellation.world.event.LunarEclipse;
import hellfirepvp.astralsorcery.common.constellation.world.event.SolarEclipse;
import hellfirepvp.astralsorcery.common.constellation.world.event.CelestialEvent;
import java.util.Set;

public class CelestialEventHandler
{
    private final WorldContext ctx;
    private final Set<CelestialEvent> events;
    private final SolarEclipse solarEclipseEvent;
    private final LunarEclipse lunarEclipseEvent;
    private final StarFall starFallNight;
    
    CelestialEventHandler(final WorldContext context) {
        this.events = new HashSet<CelestialEvent>();
        this.ctx = context;
        this.solarEclipseEvent = this.addTrackedEvent(new SolarEclipse());
        this.lunarEclipseEvent = this.addTrackedEvent(new LunarEclipse());
        this.starFallNight = this.addTrackedEvent(new StarFall());
    }
    
    public <T extends CelestialEvent> T addTrackedEvent(final T event) {
        this.events.add(event);
        return event;
    }
    
    public SolarEclipse getSolarEclipse() {
        return this.solarEclipseEvent;
    }
    
    public LunarEclipse getLunarEclipse() {
        return this.lunarEclipseEvent;
    }
    
    public StarFall getStarFallEvent() {
        return this.starFallNight;
    }
    
    public float getSolarEclipsePercent() {
        final SolarEclipse solarEclipse = this.getSolarEclipse();
        if (!solarEclipse.isActiveNow()) {
            return 0.0f;
        }
        final float halfDuration = solarEclipse.getEventDuration() / 2.0f;
        float tick = solarEclipse.getEffectTick(0.0f) - halfDuration;
        tick /= halfDuration;
        return Math.abs(tick);
    }
    
    void tick(final Level world) {
        for (final CelestialEvent event : this.events) {
            event.tick(world, this.ctx.getRandom(event.getSeedModifier()), this.ctx);
        }
    }
}
