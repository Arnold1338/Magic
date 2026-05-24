package hellfirepvp.astralsorcery.common.perk;

import java.util.Collections;
import java.util.Objects;
import java.awt.geom.Point2D;
import net.minecraftforge.fml.LogicalSide;
import com.google.common.collect.Lists;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class PerkConverter extends ForgeRegistryEntry<PerkConverter>
{
    public PerkConverter(final ResourceLocation id) {
        this.setRegistryName(id);
    }
    
    @Nonnull
    public abstract PerkAttributeModifier convertModifier(final Player p0, final PlayerProgress p1, final PerkAttributeModifier p2, @Nullable final ModifierSource p3);
    
    @Nonnull
    public Collection<PerkAttributeModifier> gainExtraModifiers(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
        return Lists.newArrayList();
    }
    
    public void onApply(final Player player, final LogicalSide dist) {
    }
    
    public void onRemove(final Player player, final LogicalSide dist) {
    }
    
    public Radius asRangedConverter(final Point2D.Float offset, final float radius) {
        final PerkConverter thisConverter = this;
        return new Radius(this.getRegistryName(), offset, radius) {
            @Nonnull
            @Override
            public PerkAttributeModifier convertModifierInRange(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, final AbstractPerk owningPerk) {
                return thisConverter.convertModifier(player, progress, modifier, owningPerk);
            }
            
            @Nonnull
            @Override
            public Collection<PerkAttributeModifier> gainExtraModifiersInRange(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, final AbstractPerk owningPerk) {
                return thisConverter.gainExtraModifiers(player, progress, modifier, owningPerk);
            }
        };
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PerkConverter that = (PerkConverter)o;
        return this.getRegistryName() == that.getRegistryName();
    }
    
    public int hashCode() {
        return Objects.hash(this.getRegistryName());
    }
    
    public abstract static class Radius extends PerkConverter
    {
        private final float radius;
        private final Point2D.Float offset;
        
        public Radius(final ResourceLocation id, final Point2D.Float point, final float radius) {
            super(id);
            this.offset = point;
            this.radius = radius;
        }
        
        public float getRadius() {
            return this.radius;
        }
        
        public Point2D.Float getOffset() {
            return this.offset;
        }
        
        public Radius withNewRadius(final float radius) {
            final Radius thisRadius = this;
            return new Radius(this.getRegistryName(), thisRadius.getOffset(), radius) {
                @Nonnull
                @Override
                public PerkAttributeModifier convertModifierInRange(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, final AbstractPerk owningPerk) {
                    return thisRadius.convertModifierInRange(player, progress, modifier, owningPerk);
                }
                
                @Nonnull
                @Override
                public Collection<PerkAttributeModifier> gainExtraModifiersInRange(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, final AbstractPerk owningPerk) {
                    return thisRadius.gainExtraModifiersInRange(player, progress, modifier, owningPerk);
                }
            };
        }
        
        protected boolean canAffectPerk(final PlayerProgress progress, final AbstractPerk otherPerk) {
            return this.getOffset().distance(otherPerk.getOffset()) <= this.getRadius();
        }
        
        @Nonnull
        @Override
        public PerkAttributeModifier convertModifier(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
            if (!(owningSource instanceof AbstractPerk)) {
                return modifier;
            }
            final AbstractPerk owningPerk = (AbstractPerk)owningSource;
            if (!this.canAffectPerk(progress, owningPerk)) {
                return modifier;
            }
            return this.convertModifierInRange(player, progress, modifier, owningPerk);
        }
        
        @Nonnull
        @Override
        public Collection<PerkAttributeModifier> gainExtraModifiers(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
            if (!(owningSource instanceof AbstractPerk)) {
                return (Collection<PerkAttributeModifier>)Collections.emptyList();
            }
            final AbstractPerk owningPerk = (AbstractPerk)owningSource;
            if (!this.canAffectPerk(progress, owningPerk)) {
                return (Collection<PerkAttributeModifier>)Collections.emptyList();
            }
            return this.gainExtraModifiersInRange(player, progress, modifier, owningPerk);
        }
        
        @Nonnull
        public abstract PerkAttributeModifier convertModifierInRange(final Player p0, final PlayerProgress p1, final PerkAttributeModifier p2, final AbstractPerk p3);
        
        @Nonnull
        public abstract Collection<PerkAttributeModifier> gainExtraModifiersInRange(final Player p0, final PlayerProgress p1, final PerkAttributeModifier p2, final AbstractPerk p3);
    }
}
