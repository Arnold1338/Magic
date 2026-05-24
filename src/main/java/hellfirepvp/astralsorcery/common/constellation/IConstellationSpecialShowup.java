package hellfirepvp.astralsorcery.common.constellation;

import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.level.Level;

public interface IConstellationSpecialShowup extends IConstellation
{
    boolean doesShowUp(final World p0, final long p1);
    
    float getDistribution(final World p0, final long p1, final boolean p2);
    
    default long dayToWorldTime(final long day) {
        return day * (int)GeneralConfig.CONFIG.dayLength.get();
    }
}
