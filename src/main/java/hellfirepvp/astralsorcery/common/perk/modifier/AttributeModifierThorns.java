package hellfirepvp.astralsorcery.common.perk.modifier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.resources.language.I18n;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;

public class AttributeModifierThorns extends PerkAttributeModifier
{
    public AttributeModifierThorns(final PerkAttributeType type, final ModifierType mode, final float value) {
        super(type, mode, value);
    }
    
    @Override
    protected String getUnlocalizedAttributeName() {
        return "perk.attribute.astralsorcery.thorns.modifier.name";
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getAttributeDisplayFormat() {
        return net.minecraft.client.resources.language.I18n.func_135052_a("perk.modifier.astralsorcery.format.thorns", new Object[0]);
    }
}
