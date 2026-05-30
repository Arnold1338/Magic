package hellfirepvp.astralsorcery.common.perk.type;

import java.util.Locale;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.text.DecimalFormat;

public enum ModifierType
{
    ADDITION, 
    ADDED_MULTIPLY, 

    private static final DecimalFormat DISPLAY_NUMBER_FORMAT;
    
    public static ModifierType fromVanillaAttributeOperation(final AttributeModifier.Operation op) {
        return MiscUtils.getEnumEntry(ModifierType.class, op.func_220371_a());
    }
    
    public AttributeModifier.Operation getVanillaAttributeOperation() {
        return AttributeModifier.Operation.values()[this.ordinal()];
    }
    
    public String stringifyValue(final float number) {
        if (this == ModifierType.ADDITION) {
            String str = ModifierType.DISPLAY_NUMBER_FORMAT.format(number);
            if (number > 0.0f) {
                str = "+" + str;
            }
            return str;
        }
        final int nbr = Math.round(number * 100.0f);
        return ModifierType.DISPLAY_NUMBER_FORMAT.format(Math.abs((this == ModifierType.STACKING_MULTIPLY) ? (100 - nbr) : nbr));
    }
    
    public String getUnlocalizedModifierName(final float number) {
        boolean positive;
        if (this == ModifierType.ADDITION) {
            positive = (number > 0.0f);
        }
        else {
            final int nbr = Math.round(number * 100.0f);
            positive = ((this == ModifierType.STACKING_MULTIPLY) ? (nbr > 100) : (nbr > 0));
        }
        return this.getUnlocalizedModifierName(positive);
    }
    
    public String getUnlocalizedModifierName(final boolean positive) {
        final String base = positive ? "perk.modifier.astralsorcery.%s.add" : "perk.modifier.astralsorcery.%s.sub";
        return String.format(base, this.name().toLowerCase(Locale.ROOT));
    }
    
    static {
        DISPLAY_NUMBER_FORMAT = new DecimalFormat("0.##");
    }
}
