package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.world.level.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeSwimSpeed extends VanillaAttributeType
{
    private static final UUID SWIM_SPEED_ADD_ID;
    private static final UUID SWIM_SPEED_ADD_MULTIPLY_ID;
    private static final UUID SWIM_SPEED_STACK_MULTIPLY_ID;
    
    public AttributeTypeSwimSpeed() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_SWIMSPEED);
    }
    
    @Override
    public String getDescription() {
        return "Perk SwimSpeed";
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return (Attribute)ForgeMod.SWIM_SPEED.get();
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeSwimSpeed.SWIM_SPEED_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeSwimSpeed.SWIM_SPEED_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeSwimSpeed.SWIM_SPEED_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        SWIM_SPEED_ADD_ID = UUID.fromString("0E769034-8C58-48A1-88ED-220FA604E147");
        SWIM_SPEED_ADD_MULTIPLY_ID = UUID.fromString("0E769034-8CDD-48A1-88ED-220FA604E147");
        SWIM_SPEED_STACK_MULTIPLY_ID = UUID.fromString("0E769034-8C14-48A1-88ED-220FA604E147");
    }
}
