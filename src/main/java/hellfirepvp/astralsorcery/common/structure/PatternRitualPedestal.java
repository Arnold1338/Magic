package hellfirepvp.astralsorcery.common.structure;

import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternRitualPedestal extends PatternBlockArray
{
    public PatternRitualPedestal() {
        super(StructureTypesAS.PTYPE_RITUAL_PEDESTAL.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState air = Blocks.AIR.defaultBlockState();
        this.addBlockCube(MatchableState.REQUIRES_AIR, -2, 0, -2, 2, 2, 2);
        this.addBlockCube(MatchableState.REQUIRES_AIR, -3, 0, -1, 3, 2, 1);
        this.addBlockCube(MatchableState.REQUIRES_AIR, -1, 0, -3, 1, 2, 3);
        this.addBlock(BlocksAS.RITUAL_PEDESTAL.defaultBlockState(), 0, 0, 0);
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState bricks = BlocksAS.MARBLE_BRICKS.defaultBlockState();
        final BlockState raw = BlocksAS.MARBLE_RAW.defaultBlockState();
        final BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
        this.addBlock(chiseled, 0, -1, 0);
        this.addBlock(bricks, 0, -1, 1);
        this.addBlock(bricks, 0, -1, 2);
        this.addBlock(bricks, 0, -1, 3);
        this.addBlock(bricks, 1, -1, 3);
        this.addBlock(bricks, -1, -1, 3);
        this.addBlock(bricks, 0, -1, -1);
        this.addBlock(bricks, 0, -1, -2);
        this.addBlock(bricks, 0, -1, -3);
        this.addBlock(bricks, 1, -1, -3);
        this.addBlock(bricks, -1, -1, -3);
        this.addBlock(bricks, 1, -1, 0);
        this.addBlock(bricks, 2, -1, 0);
        this.addBlock(bricks, 3, -1, 0);
        this.addBlock(bricks, 3, -1, 1);
        this.addBlock(bricks, 3, -1, -1);
        this.addBlock(bricks, -1, -1, 0);
        this.addBlock(bricks, -2, -1, 0);
        this.addBlock(bricks, -3, -1, 0);
        this.addBlock(bricks, -3, -1, 1);
        this.addBlock(bricks, -3, -1, -1);
        this.addBlock(bricks, 2, -1, 2);
        this.addBlock(bricks, -2, -1, 2);
        this.addBlock(bricks, 2, -1, -2);
        this.addBlock(bricks, -2, -1, -2);
        this.addBlock(raw, 1, -1, 1);
        this.addBlock(raw, 1, -1, 2);
        this.addBlock(raw, 2, -1, 1);
        this.addBlock(raw, -1, -1, 1);
        this.addBlock(raw, -1, -1, 2);
        this.addBlock(raw, -2, -1, 1);
        this.addBlock(raw, 1, -1, -1);
        this.addBlock(raw, 1, -1, -2);
        this.addBlock(raw, 2, -1, -1);
        this.addBlock(raw, -1, -1, -1);
        this.addBlock(raw, -1, -1, -2);
        this.addBlock(raw, -2, -1, -1);
        this.addBlock(arch, 0, -1, 4);
        this.addBlock(arch, 1, -1, 4);
        this.addBlock(arch, -1, -1, 4);
        this.addBlock(arch, 0, -1, -4);
        this.addBlock(arch, 1, -1, -4);
        this.addBlock(arch, -1, -1, -4);
        this.addBlock(arch, 4, -1, 0);
        this.addBlock(arch, 4, -1, 1);
        this.addBlock(arch, 4, -1, -1);
        this.addBlock(arch, -4, -1, 0);
        this.addBlock(arch, -4, -1, 1);
        this.addBlock(arch, -4, -1, -1);
        this.addBlock(arch, 3, -1, 2);
        this.addBlock(arch, 3, -1, -2);
        this.addBlock(arch, -3, -1, 2);
        this.addBlock(arch, -3, -1, -2);
        this.addBlock(arch, 2, -1, 3);
        this.addBlock(arch, -2, -1, 3);
        this.addBlock(arch, 2, -1, -3);
        this.addBlock(arch, -2, -1, -3);
    }
}
