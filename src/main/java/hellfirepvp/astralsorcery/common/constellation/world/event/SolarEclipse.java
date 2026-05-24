package hellfirepvp.astralsorcery.common.constellation.world.event;

import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import java.util.Random;
import net.minecraft.world.level.level.Level;

public class SolarEclipse extends CelestialEvent
{
    private boolean active;
    private boolean dayOfEvent;
    private int prevEventTick;
    private int eventTick;
    
    public SolarEclipse() {
        this.active = false;
        this.dayOfEvent = false;
        this.prevEventTick = 0;
        this.eventTick = 0;
    }
    
    @Override
    public void tick(final World world, final Random rand, final WorldContext ctx) {
        for (int i = 0; i < 12 + rand.nextInt(12); ++i) {
            rand.nextLong();
        }
        int rOffset = rand.nextInt(36);
        if (rOffset >= 18) {
            rOffset -= 36;
        }
        final int halfTime = this.getEventDuration() / 2;
        final int offset = 36 - rOffset;
        final int repeat = 36;
        final long wTime = world.func_72820_D();
        final int suggestedDayLength = (int)GeneralConfig.CONFIG.dayLength.get();
        final int solarTime = (int)((wTime - offset * suggestedDayLength) % (repeat * suggestedDayLength));
        this.dayOfEvent = (solarTime >= 0 && solarTime < suggestedDayLength);
        final int midSOffset = suggestedDayLength / 4;
        if (wTime > suggestedDayLength && solarTime > midSOffset - halfTime && solarTime < midSOffset + halfTime) {
            this.active = true;
            this.prevEventTick = this.eventTick;
            this.eventTick = solarTime - (midSOffset - halfTime);
        }
        else {
            this.active = false;
            this.prevEventTick = 0;
            this.eventTick = 0;
        }
    }
    
    @Override
    public boolean isActiveNow() {
        return this.active;
    }
    
    @Override
    public boolean isActiveDay() {
        return this.dayOfEvent;
    }
    
    @Override
    public float getEffectTick(final float pTicks) {
        return this.prevEventTick + (this.eventTick - this.prevEventTick) * pTicks;
    }
    
    @Override
    public long getSeedModifier() {
        return -8248744707776877485L;
    }
    
    public int getEventDuration() {
        return (int)GeneralConfig.CONFIG.dayLength.get() / 5;
    }
}
