package hellfirepvp.astralsorcery.common.perk.modifier;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import javax.annotation.Nullable;
import com.google.common.collect.HashBasedTable;
import java.util.HashMap;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import com.google.common.collect.Table;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import java.util.Map;
import java.util.UUID;

public class DynamicAttributeModifier extends PerkAttributeModifier
{
    private final UUID uuid;
    private PerkAttributeModifier actualModifier;
    private static final Map<UUID, Map<PerkConverter, Table<PerkAttributeType, ModifierType, PerkAttributeModifier>>> gemConverterCache;
    
    public DynamicAttributeModifier(final UUID uniqueId, final PerkAttributeType type, final ModifierType mode, final float value) {
        super(type, mode, value);
        this.actualModifier = null;
        this.uuid = uniqueId;
    }
    
    @Override
    protected void initModifier() {
        super.initModifier();
        this.setAbsolute();
    }
    
    @Nonnull
    @Override
    public PerkAttributeModifier convertModifier(final PerkAttributeType type, final ModifierType mode, final float value) {
        final PerkAttributeModifier mod = super.convertModifier(type, mode, value);
        return new DynamicAttributeModifier(this.getUniqueId(), mod.getAttributeType(), mod.getMode(), mod.getRawValue());
    }
    
    @Nullable
    @Override
    protected PerkAttributeModifier getCachedAttributeModifier(final PerkConverter converter, final PerkAttributeType type, final ModifierType mode) {
        final Map<PerkConverter, Table<PerkAttributeType, ModifierType, PerkAttributeModifier>> modifierCache = DynamicAttributeModifier.gemConverterCache.computeIfAbsent(this.getUniqueId(), u -> new HashMap());
        final Table<PerkAttributeType, ModifierType, PerkAttributeModifier> cachedModifiers = modifierCache.computeIfAbsent(converter, c -> HashBasedTable.create());
        return (PerkAttributeModifier)cachedModifiers.get((Object)type, (Object)mode);
    }
    
    @Override
    protected void addModifierToCache(final PerkConverter converter, final PerkAttributeType type, final ModifierType mode, final PerkAttributeModifier modifier) {
        final Map<PerkConverter, Table<PerkAttributeType, ModifierType, PerkAttributeModifier>> modifierCache = DynamicAttributeModifier.gemConverterCache.computeIfAbsent(this.getUniqueId(), u -> new HashMap());
        final Table<PerkAttributeType, ModifierType, PerkAttributeModifier> cachedModifiers = modifierCache.computeIfAbsent(converter, c -> HashBasedTable.create());
        cachedModifiers.put((Object)type, (Object)mode, (Object)modifier);
    }
    
    private boolean resolveModifier() {
        if (this.actualModifier != null) {
            return true;
        }
        (this.actualModifier = this.attributeType.createModifier(this.value, this.mode)).setAbsolute();
        return true;
    }
    
    @Override
    public float getValue(final Player player, final PlayerProgress progress) {
        if (!this.resolveModifier()) {
            return super.getValue(player, progress);
        }
        return this.actualModifier.getValue(player, progress);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getValueForDisplay(final Player player, final PlayerProgress progress) {
        if (!this.resolveModifier()) {
            return super.getValueForDisplay(player, progress);
        }
        return this.actualModifier.getValueForDisplay(player, progress);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getAttributeDisplayFormat() {
        if (!this.resolveModifier()) {
            return super.getAttributeDisplayFormat();
        }
        return this.actualModifier.getAttributeDisplayFormat();
    }
    
    @OnlyIn(Dist.CLIENT)
    public String getUnlocalizedAttributeName() {
        if (!this.resolveModifier()) {
            return super.getUnlocalizedAttributeName();
        }
        return this.actualModifier.getUnlocalizedAttributeName();
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean hasDisplayString() {
        if (!this.resolveModifier()) {
            return super.hasDisplayString();
        }
        return this.actualModifier.hasDisplayString();
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getLocalizedAttributeValue() {
        if (!this.resolveModifier()) {
            return super.getLocalizedAttributeValue();
        }
        return this.actualModifier.getLocalizedAttributeValue();
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getLocalizedDisplayString() {
        if (!this.resolveModifier()) {
            return super.getLocalizedDisplayString();
        }
        return this.actualModifier.getLocalizedDisplayString();
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getLocalizedModifierName() {
        if (!this.resolveModifier()) {
            return super.getLocalizedModifierName();
        }
        return this.actualModifier.getLocalizedModifierName();
    }
    
    public UUID getUniqueId() {
        return this.uuid;
    }
    
    public CompoundTag serialize() {
        final CompoundTag tag = new CompoundTag();
        tag.putUUID("id", this.getUniqueId());
        tag.putString("type", this.getAttributeType().getRegistryName().toString());
        tag.putInt("mode", this.getMode().ordinal());
        tag.func_74776_a("baseValue", this.value);
        return tag;
    }
    
    @Nullable
    public static DynamicAttributeModifier deserialize(final CompoundTag tag) {
        final PerkAttributeType attrType = (PerkAttributeType)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValue(new ResourceLocation(tag.getString("type")));
        if (attrType == null) {
            return null;
        }
        final UUID id = tag.getUUID("id");
        final ModifierType mode = ModifierType.values()[tag.getInt("mode")];
        final float val = tag.getFloat("baseValue");
        return new DynamicAttributeModifier(id, attrType, mode, val);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DynamicAttributeModifier that = (DynamicAttributeModifier)o;
        return this.uuid.equals(that.uuid);
    }
    
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
    
    static {
        gemConverterCache = Maps.newHashMap();
    }
}
