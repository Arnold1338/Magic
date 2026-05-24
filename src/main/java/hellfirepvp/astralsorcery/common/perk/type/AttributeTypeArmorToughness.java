package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeArmorToughness extends VanillaAttributeType
{
    private static final UUID ARMOR_TOUGHNESS_ADD_ID;
    private static final UUID ARMOR_TOUGHNESS_ADD_MULTIPLY_ID;
    private static final UUID ARMOR_TOUGHNESS_STACK_MULTIPLY_ID;
    
    public AttributeTypeArmorToughness() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_ARMOR_TOUGHNESS);
    }
    
    @Override
    public String getDescription() {
        return "Perk Armor Toughness";
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233827_j_;
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeArmorToughness.ARMOR_TOUGHNESS_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeArmorToughness.ARMOR_TOUGHNESS_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeArmorToughness.ARMOR_TOUGHNESS_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        ARMOR_TOUGHNESS_ADD_ID = UUID.fromString("36DD43BF-0ACB-94AB-809B-D07F0FB060D5");
        ARMOR_TOUGHNESS_ADD_MULTIPLY_ID = UUID.fromString("36DD43BF-0ACB-40E4-809B-D07F0FB060D5");
        ARMOR_TOUGHNESS_STACK_MULTIPLY_ID = UUID.fromString("36DD43BF-0ACB-FF51-809B-D07F0FB060D5");
    }
}
