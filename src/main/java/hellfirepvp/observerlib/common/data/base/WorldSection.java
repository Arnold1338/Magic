package hellfirepvp.observerlib.common.data.base;
import net.minecraft.nbt.CompoundTag;
import java.util.Objects;

public abstract class WorldSection {
    private final int sX, sZ;
    protected WorldSection(int sX, int sZ) { this.sX = sX; this.sZ = sZ; }
    public final int getSectionX() { return sX; }
    public final int getSectionZ() { return sZ; }
    public abstract void writeToNBT(CompoundTag tag);
    public abstract void readFromNBT(CompoundTag tag);
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof WorldSection)) return false; WorldSection s = (WorldSection) o; return sX == s.sX && sZ == s.sZ; }
    @Override public int hashCode() { return Objects.hash(sX, sZ); }
}
