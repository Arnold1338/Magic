package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import net.minecraft.world.level.block.Block;
import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternAltarAttunement extends PatternBlockArray
{
    public PatternAltarAttunement() {
        super(StructureTypesAS.PTYPE_ALTAR_ATTUNEMENT.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState bricks = BlocksAS.MARBLE_BRICKS.defaultBlockState();
        final BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
        final BlockState sootyRaw = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlockCube(sootyRaw, -3, -1, -3, 3, -1, 3);
        this.addBlock(BlocksAS.ALTAR_ATTUNEMENT.defaultBlockState(), 0, 0, 0);
        for (int i = -3; i <= 3; ++i) {
            this.addBlock(arch, 4, -1, i);
            this.addBlock(arch, -4, -1, i);
            this.addBlock(arch, i, -1, 4);
            this.addBlock(arch, i, -1, -4);
            this.addBlock(bricks, 3, -1, i);
            this.addBlock(bricks, -3, -1, i);
            this.addBlock(bricks, i, -1, 3);
            this.addBlock(bricks, i, -1, -3);
        }
        this.addBlock(chiseled, 3, -1, 3);
        this.addBlock(chiseled, 3, -1, -3);
        this.addBlock(chiseled, -3, -1, 3);
        this.addBlock(chiseled, -3, -1, -3);
        this.addBlock(bricks, 2, -1, 0);
        this.addBlock(bricks, -2, -1, 0);
        this.addBlock(bricks, 0, -1, 2);
        this.addBlock(bricks, 0, -1, -2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 3, 0, 3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 3, 0, -3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -3, 0, 3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -3, 0, -3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 3, 1, 3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 3, 1, -3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -3, 1, 3);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -3, 1, -3);
        this.addBlock(chiseled, 3, 2, 3);
        this.addBlock(chiseled, 3, 2, -3);
        this.addBlock(chiseled, -3, 2, 3);
        this.addBlock(chiseled, -3, 2, -3);
    }
    
    private MatchableState getPillarState(final BlockMarblePillar.PillarType type) {
        return (MatchableState)new SimpleMatchableBlock(new Block[] { BlocksAS.MARBLE_PILLAR }) {
            @Nonnull
            public BlockState getDescriptiveState(final long tick) {
                return (BlockState)BlocksAS.MARBLE_PILLAR.defaultBlockState().setValue((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)type);
            }
        };
    }
}
