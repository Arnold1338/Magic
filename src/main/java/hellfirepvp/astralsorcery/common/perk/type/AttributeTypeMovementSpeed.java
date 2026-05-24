package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeMovementSpeed extends VanillaAttributeType
{
    private static final UUID MOVE_SPEED_ADD_ID;
    private static final UUID MOVE_SPEED_ADD_MULTIPLY_ID;
    private static final UUID MOVE_SPEED_STACK_MULTIPLY_ID;
    
    public AttributeTypeMovementSpeed() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_MOVESPEED);
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233821_d_;
    }
    
    @Override
    public String getDescription() {
        return "Perk MoveSpeed";
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeMovementSpeed.MOVE_SPEED_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeMovementSpeed.MOVE_SPEED_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeMovementSpeed.MOVE_SPEED_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        MOVE_SPEED_ADD_ID = UUID.fromString("0E769034-8C58-48A1-88ED-1908F602E127");
        MOVE_SPEED_ADD_MULTIPLY_ID = UUID.fromString("0E769034-8CDD-48A1-88ED-1908F602E127");
        MOVE_SPEED_STACK_MULTIPLY_ID = UUID.fromString("0E769034-8C14-48A1-88ED-1908F602E127");
    }
}
