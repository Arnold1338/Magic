package hellfirepvp.astralsorcery.common.util.block.iterator;

import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import java.util.Random;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.block.BlockGeometry;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import java.util.List;

public class BlockSpherePositionGenerator extends BlockPositionGenerator
{
    private int currentRadius;
    private final List<BlockPos> currentPositions;
    
    public BlockSpherePositionGenerator() {
        this.currentRadius = 0;
        this.currentPositions = new ArrayList<BlockPos>();
    }
    
    public BlockPos genNext(final Vector3 offset, final double radius) {
        if (this.currentRadius > radius) {
            this.currentPositions.clear();
        }
        while (this.currentPositions.isEmpty()) {
            this.generatePositions(radius);
        }
        return offset.add((Vector3i)this.currentPositions.remove(0)).toBlockPos();
    }
    
    private void generatePositions(final double maxRadius) {
        if (maxRadius <= 0.0) {
            this.currentPositions.add(BlockPos.field_177992_a);
            return;
        }
        if (this.currentRadius >= maxRadius || this.currentRadius < 0) {
            this.currentRadius = 0;
        }
        ++this.currentRadius;
        this.currentPositions.addAll(BlockGeometry.getHollowSphere(this.currentRadius, this.currentRadius - 1));
        Collections.shuffle(this.currentPositions, new Random(-785629396144587751L));
    }
    
    @Override
    public void writeToNBT(final CompoundTag nbt) {
        nbt.putInt("currentRadius", this.currentRadius);
    }
    
    @Override
    public void readFromNBT(final CompoundTag nbt) {
        this.currentRadius = nbt.getInt("currentRadius");
    }
}
