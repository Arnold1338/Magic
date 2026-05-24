package hellfirepvp.astralsorcery.common.starlight.transmission;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.LinkedList;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;

public interface ITransmissionReceiver extends IPrismTransmissionNode
{
    default List<NodeConnection<IPrismTransmissionNode>> queryNext(final WorldNetworkHandler handler) {
        return new LinkedList<NodeConnection<IPrismTransmissionNode>>();
    }
    
    default void notifyLink(final World world, final BlockPos to) {
    }
    
    default boolean notifyUnlink(final World world, final BlockPos to) {
        return false;
    }
    
    void onStarlightReceive(final World p0, final IWeakConstellation p1, final double p2);
}
