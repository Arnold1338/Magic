package hellfirepvp.astralsorcery.common.starlight.transmission;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class NodeConnection<T extends IPrismTransmissionNode>
{
    private final T node;
    private final BlockPos to;
    private final boolean canConnect;
    
    public NodeConnection(@Nullable final T node, final BlockPos to, final boolean canConnect) {
        this.node = node;
        this.to = to;
        this.canConnect = canConnect;
    }
    
    public BlockPos getTo() {
        return this.to;
    }
    
    @Nullable
    public T getNode() {
        return this.node;
    }
    
    public boolean canConnect() {
        return this.canConnect;
    }
}
