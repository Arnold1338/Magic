package hellfirepvp.observerlib.api.block;

import hellfirepvp.observerlib.api.ObserverHelper;
import hellfirepvp.observerlib.common.block.BlockAirRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MatchableState {
    MatchableState AIR = new MatchableState() {
        @Nonnull @Override
        public BlockState getDescriptiveState(long tick) { return Blocks.AIR.defaultBlockState(); }
        @Override
        public boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos pos, @Nonnull BlockState state) {
            return state.isAir();
        }
    };

    MatchableState REQUIRES_AIR = new MatchableState() {
        @Nonnull @Override
        public BlockState getDescriptiveState(long tick) {
            if (BlockAirRequirement.displayRequiredAir && ObserverHelper.blockAirRequirement != null) {
                return ObserverHelper.blockAirRequirement.defaultBlockState();
            }
            return Blocks.AIR.defaultBlockState();
        }
        @Override
        public boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos pos, @Nonnull BlockState state) {
            return state.isAir();
        }
    };

    @Nonnull BlockState getDescriptiveState(long tick);

    @Nullable
    default BlockEntity createBlockEntity(BlockGetter blockReader, long tick) {
        BlockState s = getDescriptiveState(tick);
        return s.hasBlockEntity() ? s.getBlock().newBlockEntity(BlockPos.ZERO, s) : null;
    }

    boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos absolutePosition, @Nonnull BlockState state);
}
