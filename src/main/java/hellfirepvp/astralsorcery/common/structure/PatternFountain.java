package hellfirepvp.astralsorcery.common.structure;

import javax.annotation.Nonnull;
import net.minecraft.state.Property;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import net.minecraft.world.level.block.Block;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.observerlib.api.util.PatternBlockArray;

public class PatternFountain extends PatternBlockArray
{
    public PatternFountain() {
        super(StructureTypesAS.PTYPE_FOUNTAIN.getRegistryName());
        this.makeStructure();
    }
    
    private void makeStructure() {
        final BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
        final BlockState sooty = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
        for (int xx = -3; xx <= 3; ++xx) {
            for (int zz = -3; zz <= 3; ++zz) {
                for (int yy = -6; yy <= 3; ++yy) {
                    if (Math.abs(xx) != 3 || Math.abs(zz) != 3) {
                        if (xx != 0 || zz != 0 || Math.abs(yy) != 1) {
                            this.addBlock(MatchableState.REQUIRES_AIR, xx, yy, zz);
                        }
                    }
                }
            }
        }
        this.addBlock((Block)BlocksAS.FOUNTAIN, 0, 0, 0);
        this.addBlock(sooty, 4, 0, 0);
        this.addBlock(sooty, -4, 0, 0);
        this.addBlock(sooty, 0, 0, 4);
        this.addBlock(sooty, 0, 0, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, 1, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, 2, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 4, -1, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, -2, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, 1, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, 2, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), -4, -1, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, -2, 0);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 0, 1, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 0, 2, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 0, -1, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 0, -2, 4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 0, 1, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 0, 2, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.TOP), 0, -1, -4);
        this.addBlock(this.getPillarState(BlockMarblePillar.PillarType.BOTTOM), 0, -2, -4);
        this.addBlock(runed, 4, 0, 1);
        this.addBlock(runed, 4, 0, 2);
        this.addBlock(runed, 4, 0, -1);
        this.addBlock(runed, 4, 0, -2);
        this.addBlock(runed, -4, 0, 1);
        this.addBlock(runed, -4, 0, 2);
        this.addBlock(runed, -4, 0, -1);
        this.addBlock(runed, -4, 0, -2);
        this.addBlock(runed, 1, 0, 4);
        this.addBlock(runed, 2, 0, 4);
        this.addBlock(runed, -1, 0, 4);
        this.addBlock(runed, -2, 0, 4);
        this.addBlock(runed, 1, 0, -4);
        this.addBlock(runed, 2, 0, -4);
        this.addBlock(runed, -1, 0, -4);
        this.addBlock(runed, -2, 0, -4);
        this.addBlock(runed, 3, 0, 3);
        this.addBlock(runed, 3, 0, -3);
        this.addBlock(runed, -3, 0, -3);
        this.addBlock(runed, -3, 0, 3);
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
