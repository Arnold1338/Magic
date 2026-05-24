package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.ai.attributes.Attributes;
import net.minecraft.world.level.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeAttackSpeed extends VanillaAttributeType
{
    private static final UUID ATTACK_SPEED_ADD_ID;
    private static final UUID ATTACK_SPEED_ADD_MULTIPLY_ID;
    private static final UUID ATTACK_SPEED_STACK_MULTIPLY_ID;
    
    public AttributeTypeAttackSpeed() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_ATTACK_SPEED);
    }
    
    @Override
    public String getDescription() {
        return "Perk AttackSpeed";
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return Attributes.field_233825_h_;
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeAttackSpeed.ATTACK_SPEED_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeAttackSpeed.ATTACK_SPEED_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeAttackSpeed.ATTACK_SPEED_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        ATTACK_SPEED_ADD_ID = UUID.fromString("79D9A08D-3A36-45CA-BAB9-899ADE702530");
        ATTACK_SPEED_ADD_MULTIPLY_ID = UUID.fromString("79D9AFAA-3A36-45CA-BAB9-899ADE702530");
        ATTACK_SPEED_STACK_MULTIPLY_ID = UUID.fromString("8ED9ABB5-3A36-45CA-BAB9-899ADE702530");
    }
}
