package hellfirepvp.astralsorcery.common.tile.altar;

import net.minecraft.nbt.CompoundTag;
import com.google.common.base.Preconditions;
import java.util.LinkedList;
import java.util.Deque;

public class DeferredStarlightStorage
{
    private final Deque<Integer> starlightStorage;
    private final int ticksDeferred;
    
    public DeferredStarlightStorage(final int ticksDeferred) {
        this.starlightStorage = new LinkedList<Integer>();
        Preconditions.checkArgument(ticksDeferred > 0, (Object)"DeferredStarlightStorage must be set to defer by at least 1 tick.");
        this.ticksDeferred = ticksDeferred;
    }
    
    public void setStoredStarlight(final int starlight) {
        while (this.starlightStorage.size() >= this.ticksDeferred) {
            this.starlightStorage.removeLast();
        }
        this.starlightStorage.addFirst(starlight);
    }
    
    public int getStoredStarlight() {
        if (this.starlightStorage.isEmpty()) {
            return 0;
        }
        return this.starlightStorage.getLast();
    }
    
    public void readNBT(final CompoundTag compound) {
        this.starlightStorage.clear();
        this.starlightStorage.addLast(compound.getInt("starlightStorage"));
    }
    
    public void writeNBT(final CompoundTag compound) {
        compound.putInt("starlightStorage", this.getStoredStarlight());
    }
}
