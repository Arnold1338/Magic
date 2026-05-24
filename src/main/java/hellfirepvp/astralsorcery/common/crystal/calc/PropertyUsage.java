package hellfirepvp.astralsorcery.common.crystal.calc;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PropertyUsage extends ForgeRegistryEntry<PropertyUsage>
{
    public PropertyUsage(final ResourceLocation registryName) {
        this.setRegistryName(registryName);
    }
    
    public MutableComponent getName() {
        return (MutableComponent)new Component(String.format("crystal.usage.%s.%s.name", this.getRegistryName().func_110624_b(), this.getRegistryName().func_110623_a()));
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
