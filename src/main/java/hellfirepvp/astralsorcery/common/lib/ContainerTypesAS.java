package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.container.ContainerAltarTrait;
import hellfirepvp.astralsorcery.common.container.ContainerAltarConstellation;
import hellfirepvp.astralsorcery.common.container.ContainerAltarAttunement;
import hellfirepvp.astralsorcery.common.container.ContainerAltarDiscovery;
import hellfirepvp.astralsorcery.common.container.ContainerObservatory;
import hellfirepvp.astralsorcery.common.container.ContainerTome;
import net.minecraft.world.level.inventory.MenuType;

public class ContainerTypesAS
{
    public static MenuType<ContainerTome> TOME;
    public static MenuType<ContainerObservatory> OBSERVATORY;
    public static MenuType<ContainerAltarDiscovery> ALTAR_DISCOVERY;
    public static MenuType<ContainerAltarAttunement> ALTAR_ATTUNEMENT;
    public static MenuType<ContainerAltarConstellation> ALTAR_CONSTELLATION;
    public static MenuType<ContainerAltarTrait> ALTAR_RADIANCE;
    
    private ContainerTypesAS() {
    }
}
