package hellfirepvp.astralsorcery.common.perk.type;

import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypePerkEffect extends PerkAttributeType
{
    public AttributeTypePerkEffect() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_PERK_EFFECT, true);
    }
    
    @Override
    public void onApply(final Player player, final LogicalSide side, final ModifierSource source) {
        super.onApply(player, side, source);
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValues().stream().filter(t -> t instanceof VanillaAttributeType).forEach(t -> ((VanillaAttributeType)t).refreshAttribute(player));
    }
    
    @Override
    public void onRemove(final Player player, final LogicalSide side, final boolean removedCompletely, final ModifierSource source) {
        super.onRemove(player, side, removedCompletely, source);
        RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValues().stream().filter(t -> t instanceof VanillaAttributeType).forEach(t -> ((VanillaAttributeType)t).refreshAttribute(player));
    }
}
