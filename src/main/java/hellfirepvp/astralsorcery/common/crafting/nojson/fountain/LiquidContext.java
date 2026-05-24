package hellfirepvp.astralsorcery.common.crafting.nojson.fountain;

import net.minecraft.nbt.CompoundTag;
import java.util.Random;
import hellfirepvp.astralsorcery.common.util.block.BlockGeometry;
import net.minecraft.core.BlockPos;
import java.util.List;

public class LiquidContext extends FountainEffect.EffectContext
{
    private final List<BlockPos> digPositions;
    private int tickLiquidProduction;
    public Object fountainSprite;
    
    public LiquidContext(final BlockPos fountainPos) {
        this.tickLiquidProduction = 0;
        this.digPositions = BlockGeometry.getVerticalCone(fountainPos.func_177979_c(3), 5);
    }
    
    public List<BlockPos> getDigPositions() {
        return this.digPositions;
    }
    
    public void resetLiquidProductionTick(final Random rand) {
        this.tickLiquidProduction = 20 + rand.nextInt(10);
    }
    
    public boolean tickLiquidProduction() {
        --this.tickLiquidProduction;
        return this.tickLiquidProduction <= 0;
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
    }
}
