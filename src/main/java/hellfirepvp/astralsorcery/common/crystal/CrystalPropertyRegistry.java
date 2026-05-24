package hellfirepvp.astralsorcery.common.crystal;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.crystal.property.PropertyConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;

public class CrystalPropertyRegistry
{
    public static final CrystalPropertyRegistry INSTANCE;
    
    private CrystalPropertyRegistry() {
    }
    
    @Nullable
    public CrystalProperty getConstellationProperty(final IConstellation cst) {
        return RegistriesAS.REGISTRY_CRYSTAL_PROPERTIES.getValues().stream().filter(prop -> prop instanceof PropertyConstellation && ((PropertyConstellation)prop).getConstellation().equals(cst)).findFirst().orElse(null);
    }
    
    static {
        INSTANCE = new CrystalPropertyRegistry();
    }
}
