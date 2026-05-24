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

public class PatternAltarConstellation extends PatternBlockArray
{
    public PatternAltarConstellation() {
        super(StructureTypesAS.PTYPE_ALTAR_CONSTELLATION.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState raw = BlocksAS.MARBLE_RAW.defaultBlockState();
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState bricks = BlocksAS.MARBLE_BRICKS.defaultBlockState();
        final BlockState sootyRaw = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlockCube(bricks, -4, -1, -4, 4, -1, 4);
        this.addBlockCube(sootyRaw, -3, -1, -3, 3, -1, 3);
        this.addBlock(BlocksAS.ALTAR_CONSTELLATION.defaultBlockState(), 0, 0, 0);
        this.addBlock(raw, -4, -1, -4);
        this.addBlock(raw, -4, -1, -3);
        this.addBlock(raw, -3, -1, -4);
        this.addBlock(raw, 4, -1, -4);
        this.addBlock(raw, 4, -1, -3);
        this.addBlock(raw, 3, -1, -4);
        this.addBlock(raw, -4, -1, 4);
        this.addBlock(raw, -4, -1, 3);
        this.addBlock(raw, -3, -1, 4);
        this.addBlock(raw, 4, -1, 4);
        this.addBlock(raw, 4, -1, 3);
        this.addBlock(raw, 3, -1, 4);
        this.addBlock(bricks, -5, -1, -5);
        this.addBlock(bricks, -5, -1, -4);
        this.addBlock(bricks, -5, -1, -3);
        this.addBlock(bricks, -4, -1, -5);
        this.addBlock(bricks, -3, -1, -5);
        this.addBlock(bricks, 5, -1, -5);
        this.addBlock(bricks, 5, -1, -4);
        this.addBlock(bricks, 5, -1, -3);
        this.addBlock(bricks, 4, -1, -5);
        this.addBlock(bricks, 3, -1, -5);
        this.addBlock(bricks, -5, -1, 5);
        this.addBlock(bricks, -5, -1, 4);
        this.addBlock(bricks, -5, -1, 3);
        this.addBlock(bricks, -4, -1, 5);
        this.addBlock(bricks, -3, -1, 5);
        this.addBlock(bricks, 5, -1, 5);
        this.addBlock(bricks, 5, -1, 4);
        this.addBlock(bricks, 5, -1, 3);
        this.addBlock(bricks, 4, -1, 5);
        this.addBlock(bricks, 3, -1, 5);
        this.addBlock(runed, -4, 0, -4);
        this.addBlock(runed, -4, 0, 4);
        this.addBlock(runed, 4, 0, -4);
        this.addBlock(runed, 4, 0, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, 1, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, 1, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, 1, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, 1, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, 2, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, 2, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, 2, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, 2, -4);
        this.addBlock(chiseled, -4, 3, -4);
        this.addBlock(chiseled, -4, 3, 4);
        this.addBlock(chiseled, 4, 3, -4);
        this.addBlock(chiseled, 4, 3, 4);
    }
    
    private MatchableState getPillarState(final BlockMarblePillar.PillarType type) {
        return (MatchableState)new SimpleMatchableBlock(new Block[] { BlocksAS.MARBLE_PILLAR }) {
            @Nonnull
            public BlockState getDescriptiveState(final long tick) {
                return (BlockState)BlocksAS.MARBLE_PILLAR.defaultBlockState().func_206870_a((Property)BlockMarblePillar.PILLAR_TYPE, (Comparable)type);
            }
        };
    }
}
