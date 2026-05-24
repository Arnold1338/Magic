package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.container.ContainerAltarTrait;
import hellfirepvp.astralsorcery.common.container.ContainerAltarConstellation;
import hellfirepvp.astralsorcery.common.container.ContainerAltarAttunement;
import hellfirepvp.astralsorcery.common.container.ContainerAltarDiscovery;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;
import hellfirepvp.astralsorcery.common.container.ContainerTome;
import net.minecraft.world.inventory.MenuType;

public class ContainerTypesAS
{
    public static ContainerType<ContainerTome> TOME;
    public static ContainerType<ContainerObservatory> OBSERVATORY;
    public static ContainerType<ContainerAltarDiscovery> ALTAR_DISCOVERY;
    public static ContainerType<ContainerAltarAttunement> ALTAR_ATTUNEMENT;
    public static ContainerType<ContainerAltarConstellation> ALTAR_CONSTELLATION;
    public static ContainerType<ContainerAltarTrait> ALTAR_RADIANCE;
    
    private ContainerTypesAS() {
    }
}
