package hellfirepvp.observerlib.api.util;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlockState;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.observerlib.api.tile.MatchableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlockArray implements Structure {
    private final Map<BlockPos, MatchableState> blocks;
    private final Map<BlockPos, MatchableTile<? extends BlockEntity>> tiles;
    private Vec3i min;
    private Vec3i max;

    public BlockArray() {
        this.blocks = new HashMap<>();
        this.tiles = new HashMap<>();
        this.min = new Vec3i(0, 0, 0);
        this.max = new Vec3i(0, 0, 0);
    }

    @Nonnull
    @Override
    public Map<BlockPos, MatchableState> getContents() {
        return Collections.unmodifiableMap(this.blocks);
    }

    @Nonnull
    @Override
    public Map<BlockPos, ? extends MatchableTile<? extends BlockEntity>> getTileEntities() {
        return Collections.unmodifiableMap(this.tiles);
    }

    @Override
    public Vec3i getMaximumOffset() { return this.max; }

    @Override
    public Vec3i getMinimumOffset() { return this.min; }

    public void addTileEntity(MatchableTile<?> tile, int x, int y, int z) {
        addTileEntity(tile, new BlockPos(x, y, z));
    }

    public void addTileEntity(MatchableTile<?> tile, BlockPos pos) {
        this.tiles.put(pos, (MatchableTile<? extends BlockEntity>) tile);
        updateSize(pos);
    }

    public void addBlock(BlockState state, int x, int y, int z) { addBlock(state, new BlockPos(x, y, z)); }
    public void addBlock(Block block, int x, int y, int z) { addBlock(block, new BlockPos(x, y, z)); }
    public void addBlock(MatchableState state, int x, int y, int z) { addBlock(state, new BlockPos(x, y, z)); }

    public void addBlock(BlockState state, BlockPos pos) {
        MatchableState match = (state == Blocks.AIR.defaultBlockState()) ? MatchableState.AIR : new SimpleMatchableBlockState(state);
        addBlock(match, pos);
    }

    public void addBlock(Block block, BlockPos pos) {
        MatchableState match = (block == Blocks.AIR) ? MatchableState.AIR : new SimpleMatchableBlock(block);
        addBlock(match, pos);
    }

    public void addBlock(MatchableState state, BlockPos pos) {
        this.blocks.put(pos, state);
        updateSize(pos);
    }

    public void addAll(BlockArray other) {
        other.getContents().forEach((pos, matchState) -> addBlock(matchState, pos));
        other.getTileEntities().forEach((pos, tile) -> addTileEntity(tile, pos));
    }

    public void addBlockCube(BlockState state, int ox, int oy, int oz, int tx, int ty, int tz) {
        forAllInCube(ox, oy, oz, tx, ty, tz, (x, y, z) -> addBlock(state, x, y, z));
    }

    public void addBlockCube(MatchableState state, int ox, int oy, int oz, int tx, int ty, int tz) {
        forAllInCube(ox, oy, oz, tx, ty, tz, (x, y, z) -> addBlock(state, x, y, z));
    }

    public void forAllInCube(int ox, int oy, int oz, int tx, int ty, int tz, TriConsumer<Integer, Integer, Integer> fct) {
        int lx = Math.min(ox, tx), hx = Math.max(ox, tx);
        int ly = Math.min(oy, ty), hy = Math.max(oy, ty);
        int lz = Math.min(oz, tz), hz = Math.max(oz, tz);
        for (int xx = lx; xx <= hx; xx++)
            for (int zz = lz; zz <= hz; zz++)
                for (int yy = ly; yy <= hy; yy++)
                    fct.accept(xx, yy, zz);
    }

    private void updateSize(BlockPos addedPos) {
        int x = addedPos.getX(), y = addedPos.getY(), z = addedPos.getZ();
        int minX = Math.min(this.min.getX(), x), minY = Math.min(this.min.getY(), y), minZ = Math.min(this.min.getZ(), z);
        int maxX = Math.max(this.max.getX(), x), maxY = Math.max(this.max.getY(), y), maxZ = Math.max(this.max.getZ(), z);
        this.min = new Vec3i(minX, minY, minZ);
        this.max = new Vec3i(maxX, maxY, maxZ);
    }
}
