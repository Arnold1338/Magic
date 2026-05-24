package hellfirepvp.astralsorcery.common.util;

import java.util.Optional;
import net.minecraftforge.fml.LogicalSide;

public class SidedReference<T>
{
    private T clientData;
    private T serverData;
    
    public SidedReference() {
        this.clientData = null;
        this.serverData = null;
    }
    
    public Optional<T> getData(final LogicalSide side) {
        if (side.isClient()) {
            return Optional.ofNullable(this.clientData);
        }
        return Optional.ofNullable(this.serverData);
    }
    
    public void setData(final LogicalSide side, final T data) {
        if (side.isClient()) {
            this.clientData = data;
        }
        else {
            this.serverData = data;
        }
    }
}
