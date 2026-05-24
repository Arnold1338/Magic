package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nonnull;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.world.level.entity.ai.attributes.Attribute;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaAttributeType;

public class AttributeTypeMaxReach extends VanillaAttributeType
{
    private static final UUID REACH_ADD_ID;
    private static final UUID REACH_ADD_MULTIPLY_ID;
    private static final UUID REACH_STACK_MULTIPLY_ID;
    
    public AttributeTypeMaxReach() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_REACH);
    }
    
    @Override
    public String getDescription() {
        return "Perk MaxReach";
    }
    
    @Nonnull
    @Override
    public Attribute getAttribute() {
        return (Attribute)ForgeMod.REACH_DISTANCE.get();
    }
    
    @Override
    public UUID getID(final ModifierType mode) {
        switch (mode) {
            case ADDITION: {
                return AttributeTypeMaxReach.REACH_ADD_ID;
            }
            case ADDED_MULTIPLY: {
                return AttributeTypeMaxReach.REACH_ADD_MULTIPLY_ID;
            }
            case STACKING_MULTIPLY: {
                return AttributeTypeMaxReach.REACH_STACK_MULTIPLY_ID;
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        REACH_ADD_ID = UUID.fromString("E5416922-E446-4E1B-AEE5-04A6B83E17AA");
        REACH_ADD_MULTIPLY_ID = UUID.fromString("E5DD6922-A49F-4E1B-AEE5-04A6B83E17AA");
        REACH_STACK_MULTIPLY_ID = UUID.fromString("E5DD6922-11D4-4E1B-AEE5-04A6B83E17AA");
    }
}
