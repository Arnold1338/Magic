package hellfirepvp.observerlib.common.change;

import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ObservableArea;
import hellfirepvp.observerlib.api.ObservableAreaBoundingBox;
import hellfirepvp.observerlib.api.block.BlockChangeSet;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.observerlib.common.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class ChangeObserverStructure extends ChangeObserver {
    private final MatchableStructure structure;
    private final ObservableArea observedArea;
    private Set<BlockPos> mismatches = new HashSet<>();

    public ChangeObserverStructure(MatchableStructure structure) {
        super(structure.getRegistryName());
        this.structure = structure;
        this.observedArea = new ObservableAreaBoundingBox(structure.getMinimumOffset(), structure.getMaximumOffset());
    }

    @Override
    public void initialize(LevelAccessor world, BlockPos center) {
        for (BlockPos offset : structure.getContents().keySet()) {
            if (!structure.matchesSingleBlock(world, center, offset)) mismatches.add(offset);
        }
    }

    @Nonnull @Override public ObservableArea getObservableArea() { return observedArea; }

    @Override
    public boolean notifyChange(Level world, BlockPos center, BlockChangeSet changeSet) {
        for (BlockChangeSet.StateChange change : changeSet.getChanges()) {
            if (structure.hasBlockAt(change.getRelativePosition()) &&
                !structure.matchesSingleBlock(world, center, change.getRelativePosition(),
                    change.getNewState(), world.getBlockEntity(center.offset(change.getRelativePosition())))) {
                mismatches.add(change.getRelativePosition());
            } else {
                mismatches.remove(change.getRelativePosition());
            }
        }
        mismatches.removeIf(p -> !structure.hasBlockAt(p));
        return mismatches.isEmpty();
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        mismatches.clear();
        ListTag list = tag.getList("mismatchList", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) mismatches.add(NBTHelper.readBlockPosFromNBT(list.getCompound(i)));
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos pos : mismatches) { CompoundTag t = new CompoundTag(); NBTHelper.writeBlockPosToNBT(pos, t); list.add(t); }
        tag.put("mismatchList", list);
    }
}
