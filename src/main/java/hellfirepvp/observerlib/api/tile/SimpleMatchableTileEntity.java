package hellfirepvp.observerlib.api.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SimpleMatchableTileEntity<T extends BlockEntity> implements MatchableTile<T> {
    private final BlockEntityType<T> tileType;
    private final BiConsumer<T, CompoundTag> writeDisplayData;
    private final Consumer<T> writePlacement;

    public SimpleMatchableTileEntity(BlockEntityType<T> tileType,
                                     BiConsumer<T, CompoundTag> writeDisplayData,
                                     Consumer<T> writePlacement) {
        this.tileType = tileType;
        this.writeDisplayData = writeDisplayData;
        this.writePlacement = writePlacement;
    }

    @Override
    public void writeDisplayData(@Nonnull T tile, long tick, @Nonnull CompoundTag tag) {
        this.writeDisplayData.accept(tile, tag);
    }

    @Override
    public void postPlacement(@Nonnull T tile, @Nonnull BlockGetter world, BlockPos pos) {
        this.writePlacement.accept(tile);
    }

    @Override
    public boolean matches(@Nullable BlockGetter reader, @Nonnull BlockPos absolutePosition, @Nonnull T tile) {
        return tile.getType().equals(this.tileType);
    }
}
