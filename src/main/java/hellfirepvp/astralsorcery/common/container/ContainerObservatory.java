package hellfirepvp.astralsorcery.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import hellfirepvp.astralsorcery.common.lib.ContainerTypesAS;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;

public class ContainerObservatory extends ContainerTileEntity<TileObservatory>
{
    public ContainerObservatory(final TileObservatory observatory, final int windowId) {
        super(observatory, ContainerTypesAS.OBSERVATORY, windowId);
    }
    
    public boolean func_75145_c(final Player playerIn) {
        return true;
    }
}
