package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeArmor extends VanillaAttributeType
{
    private static final UUID ARMOR_ADD_ID;
    private static final UUID ARMOR_ADD_MULTIPLY_ID;
    private static final UUID ARMOR_STACK_MULTIPLY_ID;
    
    public AttributeTypeArmor() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_ARMOR);
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233826_i_;
    }
    
    @Override
    public String getDescription() {
        return "Perk Armor";
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeArmor.ARMOR_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeArmor.ARMOR_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeArmor.ARMOR_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        ARMOR_ADD_ID = UUID.fromString("92AAF3D7-D1CD-44CD-A721-7975FBFDB763");
        ARMOR_ADD_MULTIPLY_ID = UUID.fromString("92AAF3D7-C4CD-44CD-A721-7975FBFDB763");
        ARMOR_STACK_MULTIPLY_ID = UUID.fromString("92AAF3D7-FF4D-44CD-A721-7975FBFDB763");
    }
}
