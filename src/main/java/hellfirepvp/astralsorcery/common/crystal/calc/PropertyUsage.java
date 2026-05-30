package hellfirepvp.astralsorcery.common.crystal.calc;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;


public class PropertyUsage 
{
    public PropertyUsage(final ResourceLocation registryName) {

    }
    
    public MutableComponent getName() {
        return (MutableComponent)new Component(String.format("crystal.usage.%s.%s.name", this.getRegistryName().func_110624_b(), this.getRegistryName().addTransientModifier()));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PropertyUsage that = (PropertyUsage)o;
        return Objects.equals(this.getRegistryName(), that.getRegistryName());
    }
    
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
}
