package hellfirepvp.astralsorcery.common.constellation;

import java.util.Iterator;
import net.minecraft.util.Mth;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import java.util.Objects;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraftforge.fml.ModContainer;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.Supplier;
import java.util.List;

public abstract class Constellation extends BaseConstellation implements IConstellation
{
    private static int counter;
    private final List<Supplier<Ingredient>> signatureItems;
    private final String name;
    private final String simpleName;
    private final Color color;
    private final int id;
    
    public Constellation(final String name) {
        this(name, ColorsAS.CONSTELLATION_TYPE_MAJOR);
    }
    
    public Constellation(final String name, final Color color) {
        this.signatureItems = new LinkedList<Supplier<Ingredient>>();
        this.id = Constellation.counter++;
        this.simpleName = name;
        final ModContainer mod = MiscUtils.getCurrentlyActiveMod();
        if (mod != null) {
            this, name));
            this.name = mod.getModId() + ".constellation." + name;
        }
        else {
            this);
            this.name = "unknown.constellation." + name;
        }
        this.color = color;
    }
    
    @Override
    public IConstellation addSignatureItem(final Supplier<Ingredient> ingredient) {
        this.signatureItems.add(ingredient);
        return this;
    }
    
    @Override
    public List<Ingredient> getConstellationSignatureItems() {
        return this.signatureItems.stream().map((Function<? super Object, ?>)Supplier::get).collect((Collector<? super Object, ?, List<Ingredient>>)Collectors.toList());
    }
    
    @Override
    public boolean canDiscover(final Player player, final PlayerProgress progress) {
        return true;
    }
    
    @Override
    public int getSortingId() {
        return this.id;
    }
    
    @Override
    public Color getConstellationColor() {
        return this.color;
    }
    
    @Override
    public String getTranslationKey() {
        return this.name;
    }
    
    @Override
    public String getSimpleName() {
        return this.simpleName;
    }
    
    public String toString() {
        return "Constellation={name:" + this.name + "}";
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Constellation that = (Constellation)o;
        return Objects.equals(this.getRegistryName(), that.getRegistryName());
    }
    
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
    
    public int compareTo(final IConstellation o) {
        return Integer.compare(this.getSortingId(), o.getSortingId());
    }
    
    static {
        Constellation.counter = 0;
    }
    
    public static class Major extends Weak implements IMajorConstellation
    {
        public Major(final String name) {
            super(name);
        }
        
        public Major(final String name, final Color color) {
            super(name, color);
        }
        
        @Override
        public boolean canDiscover(final Player player, final PlayerProgress progress) {
            return true;
        }
    }
    
    public static class Weak extends Constellation implements IWeakConstellation
    {
        public Weak(final String name) {
            super(name);
        }
        
        public Weak(final String name, final Color color) {
            super(name, color);
        }
        
        @Override
        public boolean canDiscover(final Player player, final PlayerProgress progress) {
            return super.canDiscover(player, progress) && progress.getTierReached().isThisLaterOrEqual(ProgressionTier.ATTUNEMENT) && progress.wasOnceAttuned();
        }
    }
    
    public abstract static class WeakSpecial extends Weak implements IConstellationSpecialShowup
    {
        public WeakSpecial(final String name) {
            super(name);
        }
        
        public WeakSpecial(final String name, final Color color) {
            super(name, color);
        }
    }
    
    public static class Minor extends Constellation implements IMinorConstellation
    {
        private final List<MoonPhase> phases;
        
        public Minor(final String name, final MoonPhase... applicablePhases) {
            super(name);
            this.phases = new ArrayList<MoonPhase>(applicablePhases.length);
            for (final MoonPhase ph : applicablePhases) {
                if (ph == null) {
                    throw new IllegalArgumentException("null MoonPhase passed to Minor constellation registration for " + name);
                }
                this.phases.add(ph);
            }
        }
        
        public Minor(final String name, final Color color, final MoonPhase... applicablePhases) {
            super(name, color);
            this.phases = new ArrayList<MoonPhase>(applicablePhases.length);
            for (final MoonPhase ph : applicablePhases) {
                if (ph == null) {
                    throw new IllegalArgumentException("null MoonPhase passed to Minor constellation registration for " + name);
                }
                this.phases.add(ph);
            }
        }
        
        @Override
        public List<MoonPhase> getShowupMoonPhases(final long rSeed) {
            final List<MoonPhase> shifted = new ArrayList<MoonPhase>(this.phases.size());
            for (final MoonPhase mp : this.phases) {
                int index;
                for (index = mp.ordinal() + ((int)(rSeed % MoonPhase.values().length) + MoonPhase.values().length); index >= MoonPhase.values().length; index -= MoonPhase.values().length) {}
                index = Mth.getDescriptionId(index, 0, MoonPhase.values().length - 1);
                final MoonPhase offset = MoonPhase.values()[index];
                if (!shifted.contains(offset)) {
                    shifted.add(offset);
                }
            }
            return shifted;
        }
        
        @Override
        public boolean canDiscover(final Player player, final PlayerProgress progress) {
            return super.canDiscover(player, progress) && progress.wasOnceAttuned() && progress.getTierReached().isThisLaterOrEqual(ProgressionTier.TRAIT_CRAFT);
        }
    }
}
