package hellfirepvp.astralsorcery.common.tile.network;

import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.tile.base.network.TileReceiverBase;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.tile.altar.AltarCollectionCategory;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;

public class StarlightReceiverAltar extends SimpleTransmissionReceiver<TileAltar>
{
    public StarlightReceiverAltar(final BlockPos thisPos) {
        super(thisPos);
    }
    
    @Override
    public void onStarlightReceive(final Level world, final IWeakConstellation type, final double amount) {
        final TileAltar altar = this.getTileAtPos(world);
        if (altar != null) {
            final int altarTier = altar.getAltarType().ordinal();
            altar.collectStarlight((float)amount * Math.min(altarTier, 1) * 60.0f, AltarCollectionCategory.FOCUSED_NETWORK);
        }
    }
    
    @Override
    public boolean syncTileData(final Level world, final TileAltar tile) {
        return true;
    }
    
    @Override
    public Class<TileAltar> getTileClass() {
        return TileAltar.class;
    }
    
    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }
    
    public static class Provider extends TransmissionProvider
    {
        @Override
        public IPrismTransmissionNode get() {
            return new StarlightReceiverAltar(null);
        }
    }
}
