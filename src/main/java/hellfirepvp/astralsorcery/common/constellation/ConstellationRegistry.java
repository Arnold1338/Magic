package hellfirepvp.astralsorcery.common.constellation;

import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.SortedSet;

public class ConstellationRegistry
{
    private static final SortedSet<IMajorConstellation> majorConstellations;
    private static final SortedSet<IWeakConstellation> weakConstellations;
    private static final SortedSet<IMinorConstellation> minorConstellations;
    private static final SortedSet<IConstellationSpecialShowup> specialShowupConstellations;
    
    public static <T extends IConstellation> void addConstellation(final T constellation) {
        if (constellation instanceof IWeakConstellation) {
            if (constellation instanceof IMajorConstellation) {
                ConstellationRegistry.majorConstellations.add((IMajorConstellation)constellation);
            }
            ConstellationRegistry.weakConstellations.add((IWeakConstellation)constellation);
        }
        else {
            if (!(constellation instanceof IMinorConstellation)) {
                AstralSorcery.log.warn("Tried to register constellation that's neither minor nor major or weak: " + constellation.toString());
                AstralSorcery.log.warn("Skipping specific constellation registration...");
                throw new IllegalArgumentException("Tried to register non-minor, non-weak and non-major constellation.");
            }
            ConstellationRegistry.minorConstellations.add((IMinorConstellation)constellation);
        }
        if (constellation instanceof IConstellationSpecialShowup) {
            ConstellationRegistry.specialShowupConstellations.add((IConstellationSpecialShowup)constellation);
        }
    }
    
    @Nullable
    public static IConstellation getConstellation(final ResourceLocation name) {
        return (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(name);
    }
    
    public static Collection<IConstellationSpecialShowup> getSpecialShowupConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends IConstellationSpecialShowup>)ConstellationRegistry.specialShowupConstellations);
    }
    
    public static Collection<IWeakConstellation> getWeakConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends IWeakConstellation>)ConstellationRegistry.weakConstellations);
    }
    
    public static Collection<IMajorConstellation> getMajorConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends IMajorConstellation>)ConstellationRegistry.majorConstellations);
    }
    
    public static Collection<IMinorConstellation> getMinorConstellations() {
        return Collections.unmodifiableCollection((Collection<? extends IMinorConstellation>)ConstellationRegistry.minorConstellations);
    }
    
    public static Collection<IConstellation> getAllConstellations() {
        final List<IConstellation> all = new ArrayList<IConstellation>(RegistriesAS.REGISTRY_CONSTELLATIONS.getValues());
        Collections.sort(all);
        return all;
    }
    
    static {
        majorConstellations = new TreeSet<IMajorConstellation>();
        weakConstellations = new TreeSet<IWeakConstellation>();
        minorConstellations = new TreeSet<IMinorConstellation>();
        specialShowupConstellations = new TreeSet<IConstellationSpecialShowup>();
    }
}
