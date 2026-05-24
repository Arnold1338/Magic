package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class PerkAttributeEntry implements ConfigDataSet
{
    private final PerkAttributeType type;
    private final int weight;
    
    public PerkAttributeEntry(final PerkAttributeType type, final int weight) {
        this.type = type;
        this.weight = weight;
    }
    
    public PerkAttributeType getType() {
        return this.type;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    @Nullable
    public static PerkAttributeEntry deserialize(final String str) {
        final String[] split = str.split(";");
        if (split.length != 2) {
            return null;
        }
        final ResourceLocation keyAttributeType = new ResourceLocation(split[0]);
        final PerkAttributeType type = (PerkAttributeType)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValue(keyAttributeType);
        if (type == null) {
            return null;
        }
        final String strWeight = split[1];
        int weight;
        try {
            weight = Integer.parseInt(strWeight);
        }
        catch (final NumberFormatException exc) {
            return null;
        }
        return new PerkAttributeEntry(type, weight);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return this.type.getRegistryName().toString() + ";" + this.weight;
    }
}
