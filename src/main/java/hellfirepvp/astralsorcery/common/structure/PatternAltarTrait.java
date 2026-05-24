package hellfirepvp.astralsorcery.common.structure;

import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternAltarTrait extends PatternBlockArray
{
    public PatternAltarTrait() {
        super(StructureTypesAS.PTYPE_ALTAR_TRAIT.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState bricks = BlocksAS.MARBLE_BRICKS.defaultBlockState();
        StructureTypesAS.PTYPE_ALTAR_CONSTELLATION.getStructure().getContents().forEach((pos, state) -> this.addBlock(state, pos.getX(), pos.getY(), pos.getZ()));
        this.addBlock(BlocksAS.ALTAR_RADIANCE.defaultBlockState(), 0, 0, 0);
        this.addBlock(bricks, 4, 3, 3);
        this.addBlock(bricks, 4, 3, -3);
        this.addBlock(bricks, -4, 3, 3);
        this.addBlock(bricks, -4, 3, -3);
        this.addBlock(bricks, 3, 3, 4);
        this.addBlock(bricks, -3, 3, 4);
        this.addBlock(bricks, 3, 3, -4);
        this.addBlock(bricks, -3, 3, -4);
        this.addBlock(bricks, 3, 4, 3);
        this.addBlock(bricks, 3, 4, 2);
        this.addBlock(bricks, 3, 4, 1);
        this.addBlock(bricks, 3, 4, -1);
        this.addBlock(bricks, 3, 4, -2);
        this.addBlock(bricks, 3, 4, -3);
        this.addBlock(bricks, 2, 4, -3);
        this.addBlock(bricks, 1, 4, -3);
        this.addBlock(bricks, -1, 4, -3);
        this.addBlock(bricks, -2, 4, -3);
        this.addBlock(bricks, -3, 4, -3);
        this.addBlock(bricks, -3, 4, -2);
        this.addBlock(bricks, -3, 4, -1);
        this.addBlock(bricks, -3, 4, 1);
        this.addBlock(bricks, -3, 4, 2);
        this.addBlock(bricks, -3, 4, 3);
        this.addBlock(bricks, -2, 4, 3);
        this.addBlock(bricks, -1, 4, 3);
        this.addBlock(bricks, 1, 4, 3);
        this.addBlock(bricks, 2, 4, 3);
    }
}
