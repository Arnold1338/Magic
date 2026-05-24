package hellfirepvp.astralsorcery.common.constellation.world;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.constellation.IConstellationSpecialShowup;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import java.util.HashMap;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import net.minecraft.world.level.Level;
import com.google.common.collect.Maps;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.Map;

public class DistributionHandler
{
    private final WorldContext ctx;
    private final Map<Integer, Map<IConstellation, Float>> dayDistributionMap;
    private Map<IConstellation, Float> activeDistribution;
    private int lastRecordedDay;
    
    DistributionHandler(final WorldContext ctx) {
        this.dayDistributionMap = Maps.newHashMap();
        this.activeDistribution = Maps.newHashMap();
        this.lastRecordedDay = -1;
        this.ctx = ctx;
    }
    
    public void tick(final World world) {
        final ConstellationHandler cst = this.ctx.getConstellationHandler();
        final int tracked = cst.getLastTrackedDay();
        if (this.dayDistributionMap.isEmpty()) {
            this.initialize();
        }
        if (this.lastRecordedDay == tracked) {
            return;
        }
        this.lastRecordedDay = tracked;
        this.updateDistribution(world);
    }
    
    public float getDistribution(final IConstellation cst) {
        return this.activeDistribution.getOrDefault(cst, 0.0f);
    }
    
    private void updateDistribution(final World world) {
        final MoonPhase current = MoonPhase.fromWorld((IWorld)world);
        final Map<IConstellation, Float> distribution = new HashMap<IConstellation, Float>(this.dayDistributionMap.get(current.ordinal()));
        for (final IConstellationSpecialShowup special : ConstellationRegistry.getSpecialShowupConstellations()) {
            if (special.doesShowUp(world, this.lastRecordedDay)) {
                distribution.put(special, MathHelper.func_76131_a(special.getDistribution(world, this.lastRecordedDay, true), 0.0f, 1.0f));
            }
            else {
                distribution.put(special, MathHelper.func_76131_a(special.getDistribution(world, this.lastRecordedDay, false), 0.0f, 1.0f));
            }
        }
        this.activeDistribution = distribution;
    }
    
    private void initialize() {
        this.dayDistributionMap.clear();
        for (final MoonPhase ph : MoonPhase.values()) {
            this.dayDistributionMap.put(ph.ordinal(), Maps.newHashMap());
        }
        final int phaseCount = MoonPhase.values().length;
        for (final IConstellation cst : RegistriesAS.REGISTRY_CONSTELLATIONS) {
            if (cst instanceof IWeakConstellation) {
                final MoonPhase offsetPhase = this.ctx.getConstellationHandler().getOffset(cst);
                if (offsetPhase == null) {
                    return;
                }
                final int offset = offsetPhase.ordinal();
                for (final MoonPhase ph2 : MoonPhase.values()) {
                    final int index = (offset + ph2.ordinal()) % phaseCount;
                    final float distr = this.sineDistance(offset, index);
                    this.dayDistributionMap.get(index).put(cst, distr);
                }
            }
            else {
                for (final MoonPhase ph3 : MoonPhase.values()) {
                    this.dayDistributionMap.get(ph3.ordinal()).put(cst, 0.0f);
                }
            }
        }
    }
    
    private float sineDistance(final int dayStart, final int dayIn) {
        final int phaseCount = MoonPhase.values().length;
        final int dist = Math.min(Math.abs(dayStart - dayIn), Math.abs(dayStart - (dayIn + phaseCount)));
        final float part = dist / (float)(phaseCount / 2);
        return MathHelper.func_76134_b((float)(part / 2.0f * 3.141592653589793)) * 0.5f + 0.5f;
    }
}
