package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.ai.attributes.Attributes;
import net.minecraft.world.level.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeMaxHealth extends VanillaAttributeType
{
    private static final UUID MAX_HEALTH_ADD_ID;
    private static final UUID MAX_HEALTH_ADD_MULTIPLY_ID;
    private static final UUID MAX_HEALTH_STACK_MULTIPLY_ID;
    
    public AttributeTypeMaxHealth() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_HEALTH);
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233818_a_;
    }
    
    @Override
    public String getDescription() {
        return "Perk MaxHealth";
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeMaxHealth.MAX_HEALTH_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeMaxHealth.MAX_HEALTH_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeMaxHealth.MAX_HEALTH_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        MAX_HEALTH_ADD_ID = UUID.fromString("1FA85BB6-C2CF-45A3-A880-68045A46Dc39");
        MAX_HEALTH_ADD_MULTIPLY_ID = UUID.fromString("1FA85BB6-4ECF-45A3-A880-68045A46Dc39");
        MAX_HEALTH_STACK_MULTIPLY_ID = UUID.fromString("1FA85BB6-F6CF-45A3-A880-68045A46Dc39");
    }
}
