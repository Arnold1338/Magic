package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import java.util.function.Predicate;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.world.level.entity.EntityType;
import java.util.List;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.EntityTransmutationEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class EntityTransmutationRegistry extends ConfigDataAdapter<EntityTransmutationEntry>
{
    public static final EntityTransmutationRegistry INSTANCE;
    
    private EntityTransmutationRegistry() {
    }
    
    @Override
    public List<EntityTransmutationEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new EntityTransmutationEntry[] { new EntityTransmutationEntry((EntityType<?>)EntityType.field_200741_ag, (EntityType<?>)EntityType.field_200722_aA), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200756_av, (EntityType<?>)EntityType.field_200759_ay), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200784_X, (EntityType<?>)EntityType.field_233592_ba_), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200796_j, (EntityType<?>)EntityType.field_200725_aD), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200783_W, (EntityType<?>)EntityType.field_200811_y), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200795_i, (EntityType<?>)EntityType.field_200792_f), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200737_ac, (EntityType<?>)EntityType.field_200750_ap), new EntityTransmutationEntry((EntityType<?>)EntityType.field_200762_B, (EntityType<?>)EntityType.field_200742_ah) });
    }
    
    @Nullable
    public EntityType<?> getEntityTransmuteTo(final EntityType<?> from) {
        final EntityTransmutationEntry transmutation = this.getConfiguredValues().stream().filter(t -> t.getFromEntity().equals(from)).findFirst().orElse(null);
        if (transmutation != null) {
            return transmutation.getToEntity();
        }
        return null;
    }
    
    @Nullable
    public LivingEntity transmuteEntity(final ServerLevel world, final LivingEntity entity) {
        final EntityType<?> transmute = this.getEntityTransmuteTo((EntityType<?>)entity.level());
        if (transmute != null) {
            final CompoundTag tag = new CompoundTag();
            entity.func_189511_e(tag);
            world.func_217467_h((Entity)entity);
            NBTHelper.removeUUID(tag, "UUID");
            try {
                final Entity e = transmute.func_200721_a((World)world);
                if (!(e instanceof LivingEntity)) {
                    return null;
                }
                e.func_70020_e(tag);
                return (LivingEntity)e;
            }
            catch (final Exception exc) {
                return null;
            }
        }
        return null;
    }
    
    @Override
    public String getSectionName() {
        return "entity_transmutation";
    }
    
    @Override
    public String getCommentDescription() {
        return "Defines the entity types the corrupted pelotrio ritual can transmute from and to. Format: <EntityTypeFrom>;<EntityTypeTo>";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String;
    }
    
    @Nullable
    @Override
    public EntityTransmutationEntry deserialize(final String string) throws IllegalArgumentException {
        return EntityTransmutationEntry.deserialize(string);
    }
    
    static {
        INSTANCE = new EntityTransmutationRegistry();
    }
}
