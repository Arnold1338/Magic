package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import net.minecraft.world.level.block.Block;
import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternSingularity extends PatternBlockArray
{
    public PatternSingularity() {
        super(StructureTypesAS.PTYPE_SINGULARITY.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState engraved = BlocksAS.MARBLE_ENGRAVED.defaultBlockState();
        final BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
        final BlockState sooty = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        this.addBlockCube(runed, -4, -4, -4, 4, -4, 4);
        this.addBlockCube(sooty, -3, -4, -3, 3, -4, 3);
        this.addBlockCube(runed, -4, -4, -4, -2, -4, -2);
        this.addBlockCube(runed, -4, -4, 4, -2, -4, 2);
        this.addBlockCube(runed, 4, -4, -4, 2, -4, -2);
        this.addBlockCube(runed, 4, -4, 4, 2, -4, 2);
        this.addBlock(sooty, -3, -5, -3);
        this.addBlock(sooty, -3, -5, 3);
        this.addBlock(sooty, 3, -5, -3);
        this.addBlock(sooty, 3, -5, 3);
        this.addBlock(BlocksAS.FLUID_LIQUID_STARLIGHT.defaultBlockState(), -3, -4, -3);
        this.addBlock(BlocksAS.FLUID_LIQUID_STARLIGHT.defaultBlockState(), -3, -4, 3);
        this.addBlock(BlocksAS.FLUID_LIQUID_STARLIGHT.defaultBlockState(), 3, -4, -3);
        this.addBlock(BlocksAS.FLUID_LIQUID_STARLIGHT.defaultBlockState(), 3, -4, 3);
        this.addBlock(engraved, -4, -4, -4);
        this.addBlock(engraved, -4, -4, 4);
        this.addBlock(engraved, 4, -4, -4);
        this.addBlock(engraved, 4, -4, 4);
        this.addBlock(engraved, -4, -4, -2);
        this.addBlock(engraved, -4, -4, 2);
        this.addBlock(engraved, 4, -4, -2);
        this.addBlock(engraved, 4, -4, 2);
        this.addBlock(engraved, -2, -4, -4);
        this.addBlock(engraved, -2, -4, 4);
        this.addBlock(engraved, 2, -4, -4);
        this.addBlock(engraved, 2, -4, 4);
        this.addBlock(engraved, -2, -4, -2);
        this.addBlock(engraved, -2, -4, 2);
        this.addBlock(engraved, 2, -4, -2);
        this.addBlock(engraved, 2, -4, 2);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, -3, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, -3, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, -3, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, -3, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, -2, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, -2, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, -2, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, -2, 4);
        this.addBlock(chiseled, -4, -1, -4);
        this.addBlock(chiseled, -4, -1, 4);
        this.addBlock(chiseled, 4, -1, -4);
        this.addBlock(chiseled, 4, -1, 4);
        this.addBlock(Blocks.field_150340_R, 0, -4, 0);
        this.addBlock(chiseled, 1, -4, 0);
        this.addBlock(chiseled, 0, -4, 1);
        this.addBlock(chiseled, -1, -4, 0);
        this.addBlock(chiseled, 0, -4, -1);
        this.addBlock(chiseled, -2, -3, -2);
        this.addBlock(chiseled, -2, -3, 2);
        this.addBlock(chiseled, 2, -3, -2);
        this.addBlock(chiseled, 2, -3, 2);
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
