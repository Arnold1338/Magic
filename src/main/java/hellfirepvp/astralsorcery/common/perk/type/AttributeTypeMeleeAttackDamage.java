package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeMeleeAttackDamage extends VanillaAttributeType
{
    private static final UUID MELEE_ATTACK_DAMAGE_BOOST_ADD_ID;
    private static final UUID MELEE_ATTACK_DAMAGE_BOOST_ADD_MULTIPLY_ID;
    private static final UUID MELEE_ATTACK_DAMAGE_BOOST_STACK_MULTIPLY_ID;
    
    public AttributeTypeMeleeAttackDamage() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_MELEE_DAMAGE);
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233823_f_;
    }
    
    @Override
    public String getDescription() {
        return "Perk AttackDamage";
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeMeleeAttackDamage.MELEE_ATTACK_DAMAGE_BOOST_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeMeleeAttackDamage.MELEE_ATTACK_DAMAGE_BOOST_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeMeleeAttackDamage.MELEE_ATTACK_DAMAGE_BOOST_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        MELEE_ATTACK_DAMAGE_BOOST_ADD_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010FF91A0");
        MELEE_ATTACK_DAMAGE_BOOST_ADD_MULTIPLY_ID = UUID.fromString("020E0DFB-87AE-4653-95D6-831010FF91A1");
        MELEE_ATTACK_DAMAGE_BOOST_STACK_MULTIPLY_ID = UUID.fromString("020E0DFB-87AE-4653-9F56-831010FF91A2");
    }
}
