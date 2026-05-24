package hellfirepvp.astralsorcery.common.util.block.iterator;

import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.block.BlockGeometry;
import net.minecraft.core.Direction;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import java.util.LinkedList;

public class BlockLayerPositionGenerator extends BlockPositionGenerator
{
    private int layer;
    private final LinkedList<BlockPos> currentPositions;
    
    public BlockLayerPositionGenerator() {
        this.layer = 0;
        this.currentPositions = new LinkedList<BlockPos>();
    }
    
    @Override
    protected BlockPos genNext(final Vector3 offset, final double radius) {
        final int size = MathHelper.func_76128_c(radius);
        while (this.currentPositions.isEmpty()) {
            this.generatePositions(size);
        }
        return this.currentPositions.pop();
    }
    
    private void generatePositions(final int maxLayers) {
        if (maxLayers <= 0) {
            this.currentPositions.add(BlockPos.field_177992_a);
            return;
        }
        ++this.layer;
        if (this.layer > maxLayers) {
            this.layer = -maxLayers;
        }
        final Collection<BlockPos> positions = BlockGeometry.getPlane(Direction.UP, maxLayers);
        positions.forEach(pos -> this.currentPositions.add(pos.offset(0, this.layer, 0)));
        Collections.shuffle(this.currentPositions, new Random(-785629396144587751L));
    }
    
    @Override
    public void writeToNBT(final CompoundTag nbt) {
        nbt.putInt("layer", this.layer);
    }
    
    @Override
    public void readFromNBT(final CompoundTag nbt) {
        this.layer = nbt.getInt("layer");
    }
}
