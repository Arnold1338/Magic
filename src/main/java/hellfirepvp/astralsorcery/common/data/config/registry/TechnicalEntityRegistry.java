package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.base.Mods;
import java.util.List;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.entity.EntityType;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.EntityTechnicalEntry;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;

public class TechnicalEntityRegistry extends ConfigDataAdapter<EntityTechnicalEntry>
{
    public static final TechnicalEntityRegistry INSTANCE;
    
    public boolean canAffect(final Entity entity) {
        return this.canAffect((EntityType<?>)entity.level());
    }
    
    public boolean canAffect(final EntityType<?> type) {
        return !MiscUtils.contains(this.getConfiguredValues(), e -> e.getEntityType().equals(type));
    }
    
    private TechnicalEntityRegistry() {
    }
    
    @Override
    public List<EntityTechnicalEntry> getDefaultValues() {
        return Lists.newArrayList((Object[])new EntityTechnicalEntry[] { new EntityTechnicalEntry(Mods.MINECRAFT, "ender_pearl"), new EntityTechnicalEntry(Mods.ASTRAL_SORCERY, "observatory_helper"), new EntityTechnicalEntry(Mods.ASTRAL_SORCERY, "nocturnal_spark"), new EntityTechnicalEntry(Mods.ASTRAL_SORCERY, "illumination_spark"), new EntityTechnicalEntry(Mods.ASTRAL_SORCERY, "grappling_hook"), new EntityTechnicalEntry(Mods.BOTANIA, "mana_burst"), new EntityTechnicalEntry(Mods.BOTANIA, "spark"), new EntityTechnicalEntry(Mods.BOTANIA, "corporea_spark") });
    }
    
    @Override
    public String getSectionName() {
        return "technical_entities";
    }
    
    @Override
    public String getCommentDescription() {
        return "Defines entities whose purpose is mostly technical and less gameplay impactful. Those will be excluded from effects that manipulate entities. Add entities by their entity type name.Format: <EntityTypeName>";
    }
    
    @Override
    public String getTranslationKey() {
        return this.translationKey("data");
    }
    
    @Nullable
    @Override
    public EntityTechnicalEntry deserialize(final String string) throws IllegalArgumentException {
        return EntityTechnicalEntry.deserialize(string);
    }
    
    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String;
    }
    
    static {
        INSTANCE = new TechnicalEntityRegistry();
    }
}
