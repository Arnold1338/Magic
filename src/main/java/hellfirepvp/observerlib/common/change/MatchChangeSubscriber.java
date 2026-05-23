package hellfirepvp.observerlib.common.change;

import com.google.common.collect.Lists;
import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.api.block.BlockChangeSet;
import hellfirepvp.observerlib.common.api.MatcherObserverHelper;
import hellfirepvp.observerlib.common.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Collection;

public class MatchChangeSubscriber<T extends ChangeObserver> implements ChangeSubscriber<T> {
    private BlockPos center;
    private final T matcher;
    private final BlockStateChangeSet changeSet = new BlockStateChangeSet();
    private Boolean isMatching = null;
    private Collection<ChunkPos> affectedChunkCache = null;

    public MatchChangeSubscriber(BlockPos center, T matcher) { this.center = center; this.matcher = matcher; }

    public BlockPos getCenter() { return center; }
    @Nonnull @Override public BlockChangeSet getCurrentChangeSet() { return changeSet; }
    @Nonnull @Override public T getObserver() { return matcher; }

    public Collection<ChunkPos> getObservableChunks() {
        if (affectedChunkCache == null) affectedChunkCache = Lists.newArrayList(getObserver().getObservableArea().getAffectedChunks(getCenter()));
        return affectedChunkCache;
    }

    public boolean observes(BlockPos pos) {
        return getObserver().getObservableArea().observes(pos.subtract(getCenter()));
    }

    public void addChange(BlockPos pos, BlockState oldState, BlockState newState) {
        changeSet.addChange(pos.subtract(getCenter()), pos, oldState, newState);
    }

    @Override
    public boolean isValid(Level world) {
        if (isMatching != null && changeSet.isEmpty()) return isMatching;
        isMatching = matcher.notifyChange(world, getCenter(), changeSet);
        changeSet.reset();
        MatcherObserverHelper.getBuffer(world).markDirty(getCenter());
        return isMatching;
    }

    public void readFromNBT(CompoundTag tag) {
        affectedChunkCache = null;
        matcher.readFromNBT(tag.getCompound("matchData"));
        changeSet.readFromNBT(tag.getCompound("changeData"));
        center = NBTHelper.readBlockPosFromNBT(tag);
        if (tag.contains("isMatching")) isMatching = tag.getBoolean("isMatching");
        else isMatching = null;
    }

    public void writeToNBT(CompoundTag tag) {
        NBTHelper.setAsSubTag(tag, "matchData", matcher::writeToNBT);
        NBTHelper.setAsSubTag(tag, "changeData", changeSet::writeToNBT);
        NBTHelper.writeBlockPosToNBT(center, tag);
        if (isMatching != null) tag.putBoolean("isMatching", isMatching);
    }
}
