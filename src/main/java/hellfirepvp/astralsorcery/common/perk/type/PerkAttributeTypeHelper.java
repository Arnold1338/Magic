package hellfirepvp.astralsorcery.common.perk.type;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.type.vanilla.VanillaPerkAttributeType;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class PerkAttributeTypeHelper
{
    @Nullable
    public static PerkAttributeType findVanillaType(final Attribute attribute) {
        return RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValues().stream().filter(type -> type instanceof VanillaPerkAttributeType).map(type -> (VanillaPerkAttributeType)type).filter(type -> type.getAttribute().equals(attribute)).findFirst().map(type -> (PerkAttributeType)type).orElse(null);
    }
}
