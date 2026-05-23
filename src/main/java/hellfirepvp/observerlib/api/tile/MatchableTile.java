package hellfirepvp.observerlib.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MatchableTile<T extends BlockEntity> {
    @OnlyIn(Dist.CLIENT)
    void writeDisplayData(@Nonnull T tile, long tick, @Nonnull CompoundTag tag);

    void postPlacement(@Nonnull T tile, @Nonnull BlockGetter world, BlockPos pos);

    boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos absolutePosition, @Nonnull T tile);
}
