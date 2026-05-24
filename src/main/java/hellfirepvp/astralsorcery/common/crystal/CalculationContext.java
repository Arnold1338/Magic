package hellfirepvp.astralsorcery.common.crystal;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertyUsage;
import java.util.Collection;

public class CalculationContext
{
    private final Collection<PropertyUsage> usages;
    private PropertySource.SourceInstance source;
    
    private CalculationContext() {
        this.usages = new HashSet<PropertyUsage>();
        this.source = null;
    }
    
    public boolean uses(final PropertyUsage usage) {
        return this.usages.contains(usage);
    }
    
    public double withUse(final PropertyUsage usage, final double defaultValue, final Supplier<Double> valueSupplier) {
        if (this.uses(usage)) {
            return valueSupplier.get();
        }
        return defaultValue;
    }
    
    public boolean hasSource() {
        return this.source != null;
    }
    
    public boolean isSource(final PropertySource<?, ?> source) {
        return this.isSource(source::equals);
    }
    
    public boolean isSource(final Predicate<PropertySource<?, ?>> sourceTest) {
        return this.hasSource() && sourceTest.test(this.source.getSource());
    }
    
    public <T extends PropertySource.SourceInstance> T getSource() {
        if (this.hasSource()) {
            return (T)this.source;
        }
        return null;
    }
    
    public boolean isEmpty() {
        return this.usages.isEmpty() && this.source == null;
    }
    
    public static class Builder
    {
        private CalculationContext ctx;
        
        public Builder() {
            this.ctx = new CalculationContext(null);
        }
        
        public static Builder newBuilder() {
            return new Builder();
        }
        
        public static Builder withUsage(final PropertyUsage usage) {
            return newBuilder().addUsage(usage);
        }
        
        public static Builder withSource(final PropertySource.SourceInstance source) {
            return newBuilder().fromSource(source);
        }
        
        public Builder addUsage(final PropertyUsage usage) {
            this.ctx.usages.add(usage);
            return this;
        }
        
        public Builder fromSource(final PropertySource.SourceInstance source) {
            this.ctx.source = source;
            return this;
        }
        
        public CalculationContext build() {
            return this.ctx;
        }
    }
}
