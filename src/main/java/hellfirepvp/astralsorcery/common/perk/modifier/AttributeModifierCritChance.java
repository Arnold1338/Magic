package hellfirepvp.astralsorcery.common.perk.modifier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;

public class AttributeModifierCritChance extends PerkAttributeModifier
{
    public AttributeModifierCritChance(final PerkAttributeType type, final ModifierType mode, final float value) {
        super(type, mode, value);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getLocalizedAttributeValue() {
        String str = super.getLocalizedAttributeValue();
        if (this.getMode() == ModifierType.ADDITION) {
            str += "%";
        }
        return str;
    }
}
