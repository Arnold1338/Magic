package hellfirepvp.astralsorcery.common.crystal.calc;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class PropertySource<T, I extends SourceInstance>
{
    private final ResourceLocation registryName;
    
    public PropertySource(final ResourceLocation registryName) {
        this.registryName = registryName;
    }
    
    public final ResourceLocation getRegistryName() {
        return this.registryName;
    }
    
    public abstract I createInstance(final T p0);
    
    public Component getName() {
        return (Component)new Component(String.format("crystal.source.%s.%s.name", this.getRegistryName().func_110624_b(), this.getRegistryName().addTransientModifier()));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PropertySource that = (PropertySource)o;
        return Objects.equals(this.registryName, that.registryName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.registryName);
    }
    
    public abstract static class SourceInstance
    {
        private final PropertySource<?, ?> source;
        
        protected SourceInstance(final PropertySource<?, ?> source) {
            this.source = source;
        }
        
        public PropertySource<?, ?> getSource() {
            return this.source;
        }
    }
}
