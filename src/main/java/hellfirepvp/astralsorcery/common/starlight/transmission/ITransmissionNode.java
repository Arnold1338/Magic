package hellfirepvp.astralsorcery.common.starlight.transmission;

import java.util.LinkedList;
import java.util.List;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;

public interface ITransmissionNode extends IPrismTransmissionNode
{
    NodeConnection<IPrismTransmissionNode> queryNextNode(final WorldNetworkHandler p0);
    
    default List<NodeConnection<IPrismTransmissionNode>> queryNext(final WorldNetworkHandler handler) {
        final List<NodeConnection<IPrismTransmissionNode>> nodes = new LinkedList<NodeConnection<IPrismTransmissionNode>>();
        final NodeConnection<IPrismTransmissionNode> next = this.queryNextNode(handler);
        if (next != null) {
            nodes.add(next);
        }
        return nodes;
    }
}
