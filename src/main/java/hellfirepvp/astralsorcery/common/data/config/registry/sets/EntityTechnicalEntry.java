package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nonnull;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class EntityTechnicalEntry implements ConfigDataSet
{
    private final ResourceLocation name;
    
    public EntityTechnicalEntry(@Nullable final Mods mod, final String name) {
        this((mod == null) ? Mods.MINECRAFT.key(name) : mod.key(name));
    }
    
    public EntityTechnicalEntry(final ResourceLocation name) {
        this.name = name;
    }
    
    public EntityType<?> getEntityType() {
        return (EntityType<?>)ForgeRegistries.ENTITIES.getValue(this.name);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return this.name.toString();
    }
    
    public static EntityTechnicalEntry deserialize(final String string) throws IllegalArgumentException {
        final ResourceLocation name = new ResourceLocation(string);
        final Mods mod = Mods.byModId(name.func_110624_b());
        if (mod != null && !mod.isPresent()) {
            throw new IllegalArgumentException("Entry " + string + ", Mod not present: " + mod.getModId());
        }
        if (ForgeRegistries.ENTITIES.getValue(name) == null) {
            throw new IllegalArgumentException("Unknown Entity Type: " + name);
        }
        return new EntityTechnicalEntry(name);
    }
}
