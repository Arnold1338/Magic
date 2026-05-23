package hellfirepvp.observerlib.common.change;

import com.google.common.collect.Maps;
import hellfirepvp.observerlib.api.block.BlockChangeSet;
import hellfirepvp.observerlib.common.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class BlockStateChangeSet implements BlockChangeSet {
    private final Map<BlockPos, BlockStateChange> changes = Maps.newHashMap();

    public void addChange(BlockPos pos, BlockPos absolute, BlockState oldState, BlockState newState) {
        BlockStateChange existing = changes.get(pos);
        if (existing != null) changes.put(pos, new BlockStateChange(pos, absolute, existing.oldState, newState));
        else changes.put(pos, new BlockStateChange(pos, absolute, oldState, newState));
    }

    public final void reset() { changes.clear(); }
    @Override public boolean hasChange(BlockPos pos) { return changes.containsKey(pos); }
    @Override public boolean isEmpty() { return changes.isEmpty(); }
    @Nonnull @Override public Collection<StateChange> getChanges() { return Collections.unmodifiableCollection(changes.values()); }

    public void readFromNBT(CompoundTag cmp) {
        changes.clear();
        ListTag list = cmp.getList("changeList", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag ct = list.getCompound(i);
            BlockPos pos = NBTHelper.readBlockPosFromNBT(ct.getCompound("relPos"));
            BlockPos abs = NBTHelper.readBlockPosFromNBT(ct.getCompound("absPos"));
            BlockState oldState = NBTHelper.getBlockStateFromTag(ct.getCompound("oldState"), Blocks.AIR.defaultBlockState());
            BlockState newState = NBTHelper.getBlockStateFromTag(ct.getCompound("newState"), Blocks.AIR.defaultBlockState());
            changes.put(pos, new BlockStateChange(pos, abs, oldState, newState));
        }
    }

    public void writeToNBT(CompoundTag cmp) {
        ListTag list = new ListTag();
        for (BlockStateChange change : changes.values()) {
            CompoundTag tag = new CompoundTag();
            NBTHelper.setAsSubTag(tag, "relPos", t -> NBTHelper.writeBlockPosToNBT(change.getRelativePosition(), t));
            NBTHelper.setAsSubTag(tag, "absPos", t -> NBTHelper.writeBlockPosToNBT(change.getAbsolutePosition(), t));
            tag.put("oldState", NBTHelper.getBlockStateNBTTag(change.oldState));
            tag.put("newState", NBTHelper.getBlockStateNBTTag(change.newState));
            list.add(tag);
        }
        cmp.put("changeList", list);
    }

    public static final class BlockStateChange implements StateChange {
        private final BlockPos pos, abs;
        final BlockState oldState;
        private final BlockState newState;
        private BlockStateChange(BlockPos pos, BlockPos abs, BlockState oldState, BlockState newState) { this.pos = pos; this.abs = abs; this.oldState = oldState; this.newState = newState; }
        @Nonnull @Override public BlockPos getAbsolutePosition() { return abs; }
        @Nonnull @Override public BlockPos getRelativePosition() { return pos; }
        @Nonnull @Override public BlockState getOldState() { return oldState; }
        @Nonnull @Override public BlockState getNewState() { return newState; }
    }
}
