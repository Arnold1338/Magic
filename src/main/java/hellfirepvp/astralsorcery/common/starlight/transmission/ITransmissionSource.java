package hellfirepvp.astralsorcery.common.starlight.transmission;

import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.IStarlightSource;

public interface ITransmissionSource extends IPrismTransmissionNode
{
    IIndependentStarlightSource provideNewIndependentSource(final IStarlightSource<?> p0);
    
    default <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        final WorldNetworkHandler handle = WorldNetworkHandler.getNetworkHandler(tile.func_145831_w());
        final IIndependentStarlightSource src = handle.getSourceAt(this.getLocationPos());
        return src == null || src.updateFromTileEntity(tile);
    }
}
