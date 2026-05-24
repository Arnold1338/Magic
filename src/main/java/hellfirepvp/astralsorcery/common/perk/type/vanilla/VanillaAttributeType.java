package hellfirepvp.astralsorcery.common.perk.type.vanilla;

import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.ai.attributes.Attribute;
import java.util.UUID;
import net.minecraft.world.level.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.entity.ai.attributes.AttributeModifier;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;

public abstract class VanillaAttributeType extends PerkAttributeType implements VanillaPerkAttributeType
{
    public VanillaAttributeType(final ResourceLocation name) {
        super(name);
    }
    
    @Override
    public void onApply(final Player player, final LogicalSide side, final ModifierSource source) {
        super.onApply(player, side, source);
        this.refreshAttribute(player);
    }
    
    @Override
    public void onRemove(final Player player, final LogicalSide side, final boolean removedCompletely, final ModifierSource source) {
        super.onRemove(player, side, removedCompletely, source);
        this.refreshAttribute(player);
    }
    
    @Override
    public void onModeApply(final Player player, final ModifierType mode, final LogicalSide side) {
        super.onModeApply(player, mode, side);
        final AttributeInstance attr = player.func_233645_dx_().func_233779_a_(this.getAttribute());
        if (attr == null) {
            return;
        }
        final AttributeModifier modifier;
        if (side.isClient() && (modifier = attr.func_111127_a(this.getID(mode))) != null) {
            if (modifier instanceof DynamicAttributeModifier) {
                return;
            }
            attr.func_188479_b(this.getID(mode));
        }
        switch (mode) {
            case ADDITION: {
                attr.func_233767_b_((AttributeModifier)new DynamicAttributeModifier(this.getID(mode), this.getDescription() + " Add", this, mode, player, side));
                break;
            }
            case ADDED_MULTIPLY: {
                attr.func_233767_b_((AttributeModifier)new DynamicAttributeModifier(this.getID(mode), this.getDescription() + " Multiply Add", this, mode, player, side));
                break;
            }
            case STACKING_MULTIPLY: {
                attr.func_233767_b_((AttributeModifier)new DynamicAttributeModifier(this.getID(mode), this.getDescription() + " Stack Add", this, mode, player, side));
                break;
            }
        }
    }
    
    @Override
    public void onModeRemove(final Player player, final ModifierType mode, final LogicalSide side, final boolean removedCompletely) {
        super.onModeRemove(player, mode, side, removedCompletely);
        final AttributeInstance attr = player.func_233645_dx_().func_233779_a_(this.getAttribute());
        if (attr == null) {
            return;
        }
        attr.func_188479_b(this.getID(mode));
    }
    
    @Override
    public void refreshAttribute(final Player player) {
        final AttributeInstance attr = player.func_233645_dx_().func_233779_a_(this.getAttribute());
        if (attr == null) {
            return;
        }
        final double base = attr.func_111125_b();
        if (base == 0.0) {
            attr.func_111128_a(1.0);
        }
        else {
            attr.func_111128_a(0.0);
        }
        attr.func_111128_a(base);
    }
    
    public abstract UUID getID(final ModifierType p0);
    
    public abstract String getDescription();
    
    @Nonnull
    @Override
    public abstract Attribute getAttribute();
    
    static class DynamicAttributeModifier extends AttributeModifier
    {
        private Player player;
        private LogicalSide side;
        private PerkAttributeType type;
        
        public DynamicAttributeModifier(final UUID idIn, final String nameIn, final PerkAttributeType type, final ModifierType mode, final Player player, final LogicalSide side) {
            this(idIn, nameIn, type, mode.getVanillaAttributeOperation(), player, side);
        }
        
        public DynamicAttributeModifier(final UUID idIn, final String nameIn, final PerkAttributeType type, final AttributeModifier.Operation operationIn, final Player player, final LogicalSide side) {
            super(idIn, nameIn, (operationIn == AttributeModifier.Operation.MULTIPLY_TOTAL) ? 1.0 : 0.0, operationIn);
            this.player = player;
            this.side = side;
            this.type = type;
        }
        
        public double func_111164_d() {
            final ModifierType mode = ModifierType.fromVanillaAttributeOperation(this.func_220375_c());
            return PerkAttributeHelper.getOrCreateMap(this.player, this.side).getModifier(this.player, ResearchHelper.getProgress(this.player, this.side), this.type, mode) - 1.0f;
        }
    }
}
