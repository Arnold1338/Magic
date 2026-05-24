package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import net.minecraft.world.level.block.Block;
import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternInfuser extends PatternBlockArray
{
    public PatternInfuser() {
        super(StructureTypesAS.PTYPE_INFUSER.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        this.addBlock(BlocksAS.INFUSER.defaultBlockState(), 0, 0, 0);
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState raw = BlocksAS.MARBLE_RAW.defaultBlockState();
        this.addBlock(Blocks.field_150368_y.defaultBlockState(), 0, -1, 0);
        for (int i = -2; i <= 2; ++i) {
            this.addBlock(raw, i, -2, -2);
            this.addBlock(raw, i, -2, 2);
            this.addBlock(raw, -2, -2, i);
            this.addBlock(raw, 2, -2, i);
        }
        for (int i = -1; i <= 1; ++i) {
            this.addBlock(runed, i, -1, -1);
            this.addBlock(runed, i, -1, 1);
            this.addBlock(runed, -1, -1, i);
            this.addBlock(runed, 1, -1, i);
            this.addBlock(runed, i, -1, -3);
            this.addBlock(runed, i, -1, 3);
            this.addBlock(runed, 3, -1, i);
            this.addBlock(runed, -3, -1, i);
        }
        this.addBlock(chiseled, -2, -1, -2);
        this.addBlock(chiseled, 2, -1, -2);
        this.addBlock(chiseled, -2, -1, 2);
        this.addBlock(chiseled, 2, -1, 2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), -2, 0, -2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), 2, 0, -2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), -2, 0, 2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.MIDDLE), 2, 0, 2);
        this.addBlock(chiseled, -2, 1, -2);
        this.addBlock(chiseled, 2, 1, -2);
        this.addBlock(chiseled, -2, 1, 2);
        this.addBlock(chiseled, 2, 1, 2);
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
