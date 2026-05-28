package hellfirepvp.astralsorcery.common.crystal;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Predicate;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;


public abstract class CrystalProperty extends ForgeRegistryEntry<CrystalProperty> implements Comparable<CrystalProperty>
{
    private static int counter;
    private final int sortingId;
    private ResearchProgression requiredResearch;
    private List<CrystalPropertyModifierFunction> modifiers;
    private Predicate<CalculationContext> usageTests;
    
    public CrystalProperty(final ResourceLocation registryName) {
        this.requiredResearch = null;
        this.modifiers = new ArrayList<CrystalPropertyModifierFunction>();
        this.usageTests = (ctx -> false);
        this.sortingId = CrystalProperty.counter++;
        this.setRegistryName(registryName);
    }
    
    public CrystalProperty setRequiredResearch(final ResearchProgression requiredResearch) {
        this.requiredResearch = requiredResearch;
        return this;
    }
    
    public CrystalProperty addModifier(final CrystalPropertyModifierFunction modifierFunction) {
        this.modifiers.add(modifierFunction);
        return this;
    }
    
    public CrystalProperty addUsage(final Predicate<CalculationContext> usage) {
        this.usageTests = this.usageTests.or(usage);
        return this;
    }
    
    public int getMaxTier() {
        return 3;
    }
    
    public boolean canSee(final PlayerProgress progress) {
        return this.requiredResearch == null || progress.hasResearch(this.requiredResearch);
    }
    
    public boolean hasUsageFor(final CalculationContext ctx) {
        return this.usageTests.test(ctx);
    }
    
    public double modify(double value, final int tier, final CalculationContext context) {
        final double originalValue = value;
        for (final CrystalPropertyModifierFunction fn : this.modifiers) {
            value = fn.modify(value, originalValue, tier, context);
        }
        return value;
    }
    
    public MutableComponent getName(final int currentTier) {
        return (MutableComponent)new Component(String.format("crystal.property.%s.%s.name", this.getRegistryName().func_110624_b(), this.getRegistryName().addTransientModifier()));
    }
    
    public int compareTo(final CrystalProperty other) {
        return this.sortingId - other.sortingId;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CrystalProperty that = (CrystalProperty)o;
        return Objects.equals(this.getRegistryName(), that.getRegistryName());
    }
    
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
    
    static {
        CrystalProperty.counter = 0;
    }
}
