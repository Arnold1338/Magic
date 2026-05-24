package hellfirepvp.astralsorcery.common.tile.network;

import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.starlight.network.TransmissionWorldHandler;
import hellfirepvp.astralsorcery.common.item.lens.LensColorType;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightTransmissionHandler;
import hellfirepvp.astralsorcery.common.tile.TilePrism;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.crystal.CrystalPrismTransmissionNode;

public class StarlightTransmissionPrism extends CrystalPrismTransmissionNode
{
    public StarlightTransmissionPrism(final BlockPos thisPos, final CrystalAttributes attributes) {
        super(thisPos, attributes);
    }
    
    public StarlightTransmissionPrism(final BlockPos thisPos) {
        super(thisPos);
    }
    
    @Override
    public <T extends BlockEntity> boolean updateFromTileEntity(final T tile) {
        if (!(tile instanceof TilePrism)) {
            return super.updateFromTileEntity(tile);
        }
        final LensColorType colorType = ((TilePrism)tile).getColorType();
        if (this.updateAdditionalLoss((colorType == null) ? 0.0f : colorType.getFlowMultiplier())) {
            final TransmissionWorldHandler handle = StarlightTransmissionHandler.getInstance().getWorldHandler(tile.func_145831_w());
            if (handle != null) {
                handle.notifyTransmissionNodeChange(this);
            }
        }
        this.updateIgnoreBlockCollisionState(tile.func_145831_w(), colorType != null && colorType.doesIgnoreBlockCollision());
        return true;
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new StarlightTransmissionPrism(null);
        }
    }
}
