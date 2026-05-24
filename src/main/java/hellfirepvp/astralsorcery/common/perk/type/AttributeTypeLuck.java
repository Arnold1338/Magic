package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeLuck extends VanillaAttributeType
{
    private static final UUID LUCK_ADD_ID;
    private static final UUID LUCK_ADD_MULTIPLY_ID;
    private static final UUID LUCK_STACK_MULTIPLY_ID;
    
    public AttributeTypeLuck() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_LUCK);
    }
    
    @Override
    public String getDescription() {
        return "Perk Luck";
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233828_k_;
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeLuck.LUCK_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeLuck.LUCK_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeLuck.LUCK_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        LUCK_ADD_ID = UUID.fromString("84e42c15-06be-453d-a2ea-f241ddce3645");
        LUCK_ADD_MULTIPLY_ID = UUID.fromString("91e42c15-06be-453d-a2ea-f241ddce3645");
        LUCK_STACK_MULTIPLY_ID = UUID.fromString("2de42c15-06be-453d-a2ea-f241ddce3645");
    }
}
