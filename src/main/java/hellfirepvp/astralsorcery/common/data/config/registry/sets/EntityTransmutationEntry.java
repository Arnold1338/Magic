package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityClassification;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class EntityTransmutationEntry implements ConfigDataSet
{
    private final EntityType<?> fromEntity;
    private final EntityType<?> toEntity;
    
    public EntityTransmutationEntry(final EntityType<?> fromEntity, final EntityType<?> toEntity) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
    }
    
    public EntityType<?> getFromEntity() {
        return this.fromEntity;
    }
    
    public EntityType<?> getToEntity() {
        return this.toEntity;
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return String.format("%s;%s", this.fromEntity.getRegistryName().toString(), this.toEntity.getRegistryName().toString());
    }
    
    @Nullable
    public static EntityTransmutationEntry deserialize(final String str) throws IllegalArgumentException {
        final String[] split = str.split(";");
        if (split.length != 2) {
            return null;
        }
        final ResourceLocation fromKey = new ResourceLocation(split[0]);
        final EntityType<?> fromType = (EntityType<?>)ForgeRegistries.ENTITIES.getValue(fromKey);
        if (fromType == null) {
            throw new IllegalArgumentException(split[0] + " is not a known EntityType.");
        }
        final ResourceLocation toKey = new ResourceLocation(split[1]);
        final EntityType<?> toType = (EntityType<?>)ForgeRegistries.ENTITIES.getValue(toKey);
        if (toType == null) {
            throw new IllegalArgumentException(split[0] + " is not a known EntityType.");
        }
        if (!toType.func_200720_b() || toType.func_220339_d() == EntityClassification.MISC) {
            throw new IllegalArgumentException("EntityType " + split[1] + " seems to be not summonable or isn't classified as creature.");
        }
        return new EntityTransmutationEntry(fromType, toType);
    }
}
