package hellfirepvp.astralsorcery.common.constellation.world;

import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.IConstellationSpecialShowup;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import java.util.Collection;
import net.minecraft.world.level.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.level.level.Level;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import java.util.Map;

public class ConstellationHandler
{
    private final WorldContext ctx;
    private final Map<MoonPhase, LinkedList<IConstellation>> activeMap;
    private final Map<IConstellation, MoonPhase> directOffsetMap;
    private int lastRecordedDay;
    private final List<IConstellation> visibleSpecialConstellations;
    
    ConstellationHandler(final WorldContext context) {
        this.activeMap = Maps.newHashMap();
        this.directOffsetMap = Maps.newHashMap();
        this.lastRecordedDay = -1;
        this.visibleSpecialConstellations = Lists.newArrayList();
        this.ctx = context;
    }
    
    @Nullable
    public MoonPhase getOffset(final IConstellation cst) {
        return this.directOffsetMap.get(cst);
    }
    
    public boolean isActiveCurrently(final IConstellation cst, final MoonPhase phase) {
        return this.isActiveInPhase(cst, phase) || this.visibleSpecialConstellations.contains(cst);
    }
    
    public boolean isActiveInPhase(final IConstellation cst, final MoonPhase phase) {
        return this.activeMap.get(phase).contains(cst);
    }
    
    public int getLastTrackedDay() {
        return this.lastRecordedDay;
    }
    
    public void tick(final World world) {
        if (this.activeMap.isEmpty()) {
            this.initialize();
        }
        final int currentDay = (int)(world.func_72820_D() / (int)GeneralConfig.CONFIG.dayLength.get());
        final int dayDifference = currentDay - this.lastRecordedDay;
        if (dayDifference != 0) {
            this.lastRecordedDay = currentDay;
            this.updateActiveConstellations(world);
        }
    }
    
    private void updateActiveConstellations(final World world) {
        this.visibleSpecialConstellations.clear();
        final MoonPhase ph = MoonPhase.fromWorld((IWorld)world);
        final LinkedList<IConstellation> active = new LinkedList<IConstellation>(this.activeMap.computeIfAbsent(ph, p -> Lists.newLinkedList()));
        for (final IConstellationSpecialShowup cst : ConstellationRegistry.getSpecialShowupConstellations()) {
            if (cst.doesShowUp(world, this.lastRecordedDay)) {
                this.visibleSpecialConstellations.add(cst);
            }
        }
        active.addAll(this.visibleSpecialConstellations);
        this.ctx.getActiveCelestialsHandler().updatePositions(active);
    }
    
    private void initialize() {
        this.activeMap.clear();
        this.directOffsetMap.clear();
        for (final MoonPhase ph : MoonPhase.values()) {
            this.activeMap.put(ph, Lists.newLinkedList());
        }
        final Random rand = this.ctx.getRandom();
        final boolean[] occupiedSlots = new boolean[MoonPhase.values().length];
        Arrays.fill(occupiedSlots, false);
        final LinkedList<IWeakConstellation> weakAndMajor = Lists.newLinkedList((Iterable)ConstellationRegistry.getWeakConstellations());
        Collections.shuffle(weakAndMajor, rand);
        weakAndMajor.forEach(c -> this.addConstellationCycle(c, rand, occupiedSlots));
        final LinkedList<IConstellation> constellations = Lists.newLinkedList((Iterable)ConstellationRegistry.getMinorConstellations());
        Collections.shuffle(constellations, rand);
        constellations.forEach(c -> this.addConstellationCycle(c, rand, occupiedSlots));
    }
    
    private void addConstellationCycle(final IConstellation cst, final Random rand, final boolean[] slots) {
        if (cst instanceof IConstellationSpecialShowup) {
            return;
        }
        if (cst instanceof IMinorConstellation) {
            for (final MoonPhase ph : ((IMinorConstellation)cst).getShowupMoonPhases(this.ctx.getSeed())) {
                this.activeMap.get(ph).add(cst);
            }
        }
        else {
            final int start = this.searchForSpot(rand, slots);
            this.occupySlots(start, slots);
            if (this.getSlots(slots) <= 0) {
                Arrays.fill(slots, false);
            }
            for (int i = 0; i < 5; ++i) {
                final MoonPhase ph2 = this.getPhase(start + i);
                this.activeMap.get(ph2).add(cst);
            }
            this.directOffsetMap.put(cst, this.getPhase(start));
        }
    }
    
    private MoonPhase getPhase(int rIndex) {
        int moonPhaseCount;
        for (moonPhaseCount = MoonPhase.values().length; rIndex < 0; rIndex += moonPhaseCount) {}
        return MoonPhase.values()[rIndex % moonPhaseCount];
    }
    
    private int searchForSpot(final Random r, final boolean[] occupied) {
        boolean foundFree = false;
        int tries = 5;
        int start;
        do {
            --tries;
            start = r.nextInt(8);
            final int count = this.getSlots(occupied);
            if (count >= 3) {
                foundFree = true;
            }
        } while (!foundFree && tries > 0);
        return start;
    }
    
    private void occupySlots(final int start, final boolean[] occupied) {
        for (int i = 0; i < 5; ++i) {
            final int index = (start + i) % 8;
            if (!occupied[index]) {
                occupied[index] = true;
            }
        }
    }
    
    private int getSlots(final boolean[] array) {
        int it = 0;
        for (final boolean b : array) {
            if (!b) {
                ++it;
            }
        }
        return it;
    }
}
