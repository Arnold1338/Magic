package hellfirepvp.astralsorcery.common.constellation.world.event;

import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import java.util.Random;
import net.minecraft.world.level.Level;

public class LunarEclipse extends CelestialEvent
{
    private boolean active;
    private boolean dayOfEvent;
    private int prevEventTick;
    private int eventTick;
    
    public LunarEclipse() {
        this.active = false;
        this.dayOfEvent = false;
        this.prevEventTick = 0;
        this.eventTick = 0;
    }
    
    @Override
    public void tick(final Level world, final Random rand, final WorldContext ctx) {
        for (int i = 0; i < 12 + rand.nextInt(12); ++i) {
            rand.nextLong();
        }
        final int halfTime = this.getEventDuration() / 2;
        final int repeat = 68;
        final long wTime = world.func_72820_D();
        final int suggestedDayLength = (int)GeneralConfig.CONFIG.dayLength.get();
        final int lunarTime = (int)(wTime % (repeat * suggestedDayLength));
        this.dayOfEvent = (lunarTime >= 0 && lunarTime < suggestedDayLength);
        final int midLOffset = Math.round(suggestedDayLength * 0.75f);
        if (wTime > suggestedDayLength && lunarTime > midLOffset - halfTime && lunarTime < midLOffset + halfTime) {
            this.active = true;
            this.prevEventTick = this.eventTick;
            this.eventTick = lunarTime - (midLOffset - halfTime);
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
        return -6515086094271922247L;
    }
    
    public int getEventDuration() {
        return (int)GeneralConfig.CONFIG.dayLength.get() / 5;
    }
}
