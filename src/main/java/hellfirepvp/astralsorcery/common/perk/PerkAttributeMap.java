package hellfirepvp.astralsorcery.common.perk;

import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.EnumSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import java.util.Collection;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.util.Map;
import net.minecraftforge.fml.LogicalSide;
import java.util.concurrent.locks.ReadWriteLock;
import hellfirepvp.astralsorcery.common.util.ReadWriteLockable;

public class PerkAttributeMap implements ReadWriteLockable
{
    private final ReadWriteLock accessLock;
    private final LogicalSide side;
    private final Map<PerkAttributeType, List<PerkAttributeModifier>> modifiers;
    private final List<PerkConverter> converters;
    
    PerkAttributeMap(final LogicalSide side) {
        this.accessLock = new ReentrantReadWriteLock(true);
        this.modifiers = Collections.synchronizedMap(new HashMap<PerkAttributeType, List<PerkAttributeModifier>>());
        this.converters = new ArrayList<PerkConverter>();
        this.side = side;
    }
    
    Collection<PerkAttributeModifier> applyModifier(@Nonnull final Player player, @Nonnull final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
        final PlayerProgress prog = ResearchHelper.getProgress(player, this.side);
        final List<PerkAttributeModifier> added = new ArrayList<PerkAttributeModifier>();
        final List<PerkAttributeModifier> modify = Lists.newArrayList();
        modify.add(modifier);
        modify.addAll(this.gainModifiers(player, prog, modifier, owningSource));
        for (final PerkAttributeModifier preMod : modify) {
            PerkAttributeModifier mod = preMod;
            LogCategory.PERKS.info(() -> "Applying unique modifier " + preMod.getComparisonKey());
            final PerkAttributeModifier postMod;
            mod = (postMod = this.convertModifier(player, prog, mod, owningSource));
            LogCategory.PERKS.info(() -> "Applying converted modifier " + postMod.getComparisonKey());
            if (this.cacheModifier(player, mod.getAttributeType(), mod)) {
                added.add(mod);
            }
            else {
                LogCategory.PERKS.warn(() -> "Could not apply modifier " + postMod.getComparisonKey() + " - already applied!");
            }
        }
        return added;
    }
    
    private boolean cacheModifier(final Player player, final PerkAttributeType type, final PerkAttributeModifier modifier) {
        final boolean noModifiers = this.getModifiersByType(type, modifier.getMode()).isEmpty();
        final List<PerkAttributeModifier> modifiers = this.modifiers.computeIfAbsent(type, t -> Lists.newArrayList());
        if (modifiers.contains(modifier)) {
            return false;
        }
        if (noModifiers) {
            type.onModeApply(player, modifier.getMode(), this.side);
        }
        return modifiers.add(modifier);
    }
    
    Collection<PerkAttributeModifier> removeModifier(@Nonnull final Player player, @Nonnull final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
        final PlayerProgress prog = ResearchHelper.getProgress(player, this.side);
        final List<PerkAttributeModifier> removed = new ArrayList<PerkAttributeModifier>();
        final List<PerkAttributeModifier> modify = Lists.newArrayList();
        modify.add(modifier);
        modify.addAll(this.gainModifiers(player, prog, modifier, owningSource));
        for (final PerkAttributeModifier preMod : modify) {
            PerkAttributeModifier mod = preMod;
            LogCategory.PERKS.info(() -> "Removing unique modifier " + preMod.getComparisonKey());
            final PerkAttributeModifier postMod;
            mod = (postMod = this.convertModifier(player, prog, mod, owningSource));
            LogCategory.PERKS.info(() -> "Removing converted modifier " + postMod.getComparisonKey());
            if (this.dropModifier(player, mod.getAttributeType(), mod)) {
                removed.add(mod);
            }
            else {
                LogCategory.PERKS.warn(() -> "Could not remove modifier " + postMod.getComparisonKey() + " - not applied!");
            }
        }
        return removed;
    }
    
    private boolean dropModifier(final Player player, final PerkAttributeType type, final PerkAttributeModifier modifier) {
        if (this.modifiers.computeIfAbsent(type, t -> Lists.newArrayList()).remove(modifier)) {
            final boolean completelyRemoved = this.modifiers.get(type).isEmpty();
            if (this.getModifiersByType(type, modifier.getMode()).isEmpty()) {
                type.onModeRemove(player, modifier.getMode(), this.side, completelyRemoved);
            }
            return true;
        }
        return false;
    }
    
    @Nonnull
    private PerkAttributeModifier convertModifier(@Nonnull final Player player, @Nonnull final PlayerProgress progress, @Nonnull PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
        for (final PerkConverter converter : this.converters) {
            modifier = converter.convertModifier(player, progress, modifier, owningSource);
        }
        return modifier;
    }
    
    @Nonnull
    private Collection<PerkAttributeModifier> gainModifiers(@Nonnull final Player player, @Nonnull final PlayerProgress progress, @Nonnull final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
        final Collection<PerkAttributeModifier> modifiers = Lists.newArrayList();
        for (final PerkConverter converter : this.converters) {
            modifiers.addAll(converter.gainExtraModifiers(player, progress, modifier, owningSource));
        }
        return modifiers;
    }
    
    boolean applyConverter(final Player player, final PerkConverter converter) {
        this.assertConvertersModifiable();
        LogCategory.PERKS.info(() -> "Try adding converter " + converter.getRegistryName() + " on " + this.side.name());
        if (this.converters.contains(converter)) {
            return false;
        }
        this.converters.add(converter);
        converter.onApply(player, this.side);
        LogCategory.PERKS.info(() -> "Added converter " + converter.getRegistryName());
        return true;
    }
    
    boolean removeConverter(final Player player, final PerkConverter converter) {
        this.assertConvertersModifiable();
        LogCategory.PERKS.info(() -> "Try removing converter " + converter.getRegistryName() + " on " + this.side.name());
        if (this.converters.remove(converter)) {
            converter.onRemove(player, this.side);
            LogCategory.PERKS.info(() -> "Removed converter " + converter.getRegistryName());
            return true;
        }
        return false;
    }
    
    void assertConvertersModifiable() {
        int appliedModifiers = 0;
        for (final List<PerkAttributeModifier> modifiers : this.modifiers.values()) {
            appliedModifiers += modifiers.size();
        }
        if (appliedModifiers > 0) {
            LogCategory.PERKS.warn(() -> "Following modifiers are still applied on " + this.side.name() + " while trying to modify converters:");
            for (final List<PerkAttributeModifier> modifiers : this.modifiers.values()) {
                for (final PerkAttributeModifier modifier : modifiers) {
                    LogCategory.PERKS.warn(() -> "Modifier: " + modifier.getComparisonKey());
                }
            }
            throw new IllegalStateException("Trying to modify PerkConverters while modifiers are applied!");
        }
    }
    
    public boolean hasModifiers(final PerkAttributeType type) {
        return this.read(() -> !this.modifiers.getOrDefault(type, Collections.emptyList()).isEmpty());
    }
    
    private List<PerkAttributeModifier> getModifiersByType(final PerkAttributeType type, final ModifierType mode) {
        return (List)this.modifiers.computeIfAbsent(type, t -> Lists.newArrayList()).stream().filter(mod -> mod.getMode() == mode).collect(Collectors.toList());
    }
    
    public float getModifier(final Player player, final PlayerProgress progress, final PerkAttributeType type) {
        return this.getModifier(player, progress, type, EnumSet.allOf(ModifierType.class));
    }
    
    public float getModifier(final Player player, final PlayerProgress progress, final PerkAttributeType type, final ModifierType mode) {
        return this.getModifier(player, progress, type, EnumSet.of(mode));
    }
    
    public float getModifier(final Player player, final PlayerProgress progress, final PerkAttributeType type, final Collection<ModifierType> applicableModes) {
        float perkEffectModifier;
        if (!type.equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT)) {
            perkEffectModifier = this.getModifier(player, progress, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        }
        else {
            perkEffectModifier = 1.0f;
        }
        return this.read(() -> {
            float mod = 1.0f;
            if (applicableModes.contains(ModifierType.ADDITION)) {
                this.getModifiersByType(type, ModifierType.ADDITION).iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final PerkAttributeModifier modifier = iterator.next();
                    mod += modifier.getValue(player, progress) * perkEffectModifier;
                }
            }
            if (applicableModes.contains(ModifierType.ADDED_MULTIPLY)) {
                final float multiply = mod;
                this.getModifiersByType(type, ModifierType.ADDED_MULTIPLY).iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final PerkAttributeModifier modifier2 = iterator2.next();
                    mod += multiply * (modifier2.getValue(player, progress) * perkEffectModifier);
                }
            }
            if (applicableModes.contains(ModifierType.STACKING_MULTIPLY)) {
                this.getModifiersByType(type, ModifierType.STACKING_MULTIPLY).iterator();
                final Iterator iterator3;
                while (iterator3.hasNext()) {
                    final PerkAttributeModifier modifier3 = iterator3.next();
                    mod *= (modifier3.getValue(player, progress) - 1.0f) * perkEffectModifier + 1.0f;
                }
            }
            return mod;
        });
    }
    
    public float modifyValue(final Player player, final PlayerProgress progress, final PerkAttributeType type, final float value) {
        float perkEffectModifier;
        if (!type.equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT)) {
            perkEffectModifier = this.modifyValue(player, progress, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, 1.0f);
        }
        else {
            perkEffectModifier = 1.0f;
        }
        return this.read(() -> {
            float val = value;
            this.getModifiersByType(type, ModifierType.ADDITION).iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final PerkAttributeModifier mod = iterator.next();
                val += mod.getValue(player, progress) * perkEffectModifier;
            }
            final float multiply = val;
            this.getModifiersByType(type, ModifierType.ADDED_MULTIPLY).iterator();
            final Iterator iterator2;
            while (iterator2.hasNext()) {
                final PerkAttributeModifier mod2 = iterator2.next();
                val += multiply * (mod2.getValue(player, progress) * perkEffectModifier);
            }
            this.getModifiersByType(type, ModifierType.STACKING_MULTIPLY).iterator();
            final Iterator iterator3;
            while (iterator3.hasNext()) {
                final PerkAttributeModifier mod3 = iterator3.next();
                val *= (mod3.getValue(player, progress) - 1.0f) * perkEffectModifier + 1.0f;
            }
            return val;
        });
    }
    
    @Override
    public ReadWriteLock getLock() {
        return this.accessLock;
    }
}
