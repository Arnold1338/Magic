package hellfirepvp.astralsorcery.common.tile.network;

import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.tile.TileWell;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;

public class StarlightReceiverWell extends SimpleTransmissionReceiver<TileWell>
{
    public StarlightReceiverWell(final BlockPos thisPos) {
        super(thisPos);
    }
    
    @Override
    public void onStarlightReceive(final World world, final IWeakConstellation type, final double amount) {
        final TileWell well = this.getTileAtPos(world);
        if (well != null) {
            well.receiveStarlight(amount);
        }
    }
    
    @Override
    public boolean syncTileData(final World world, final TileWell tile) {
        return true;
    }
    
    @Override
    public Class<TileWell> getTileClass() {
        return TileWell.class;
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new StarlightReceiverWell(null);
        }
    }
}
