package hellfirepvp.astralsorcery.common.constellation.world;

import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.level.Level;

public class DayTimeHelper
{
    public static float getCurrentDaytimeDistribution(final World world) {
        final int dLength = (int)GeneralConfig.CONFIG.dayLength.get();
        final float dayPart = (float)((world.func_72820_D() % dLength + dLength) % dLength);
        if (dayPart < dLength / 2.0f) {
            return 0.0f;
        }
        final float part = dLength / 7.0f;
        if (dayPart < dLength / 2.0f + part) {
            return (dayPart - (dLength / 2.0f + part)) / part + 1.0f;
        }
        if (dayPart > dLength - part) {
            return 1.0f - (dayPart - (dLength - part)) / part;
        }
        return 1.0f;
    }
    
    public static boolean isNight(final World world) {
        return getCurrentDaytimeDistribution(world) >= 0.55;
    }
    
    public static boolean isDay(final World world) {
        return getCurrentDaytimeDistribution(world) <= 0.05;
    }
}
